package com.zxkj.job.service.impl;

import com.zxkj.job.bean.dto.CampusRecruitmentDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ProfessionalDto;
import com.zxkj.job.bean.po.CampusRecruitmentPo;
import com.zxkj.job.bean.po.CampusRecruitmentProfessionalRPo;
import com.zxkj.job.bean.po.ProfessionalPo;
import com.zxkj.job.bean.vo.CampusRecruitmentVo;
import com.zxkj.job.bean.vo.EnterpriseVo;
import com.zxkj.job.bean.vo.ProfessionalUpdateVo;
import com.zxkj.job.bean.vo.ProfessionalVo;
import com.zxkj.job.common.BaseServiceImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.mapper.CampusRecruitmentMapper;
import com.zxkj.job.mapper.ProfessionalMapper;
import com.zxkj.job.service.CampusRecruitmentProfessionalRService;
import com.zxkj.job.service.CampusRecruitmentService;
import com.zxkj.job.service.ProfessionalService;
import com.zxkj.job.util.BeanUtil;
import com.zxkj.job.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CampusRecruitmentServiceImpl extends BaseServiceImpl<CampusRecruitmentMapper, CampusRecruitmentPo> implements CampusRecruitmentService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${job.tmp.path}")
    private String tmpPath;

    @Autowired
    CampusRecruitmentProfessionalRService campusRecruitmentProfessionalRService;

    @Autowired
    ProfessionalService professionalService;

    @Override
    public Boolean add(CampusRecruitmentDto campusRecruitmentDto, HttpSession httpSession) throws IOException {
        CampusRecruitmentPo campusRecruitmentPo = new CampusRecruitmentPo();
        BeanUtil.copyProperties(campusRecruitmentDto, campusRecruitmentPo);
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        campusRecruitmentPo.setEnterpriseId(enterpriseVo.getId());
        MultipartFile generalRegulation = campusRecruitmentDto.getGeneralRegulation();
        String fileName = saveFile(generalRegulation);
        campusRecruitmentPo.setGeneralRegulationFileName(fileName);
        campusRecruitmentPo.setId(IdUtil.nextId());
        campusRecruitmentPo.setGmtCreate(new Date());
        if(!super.insert(campusRecruitmentPo)){
            throw JobException.CAMPUS_RECRUITMENT_INSERT_EXCEPTION;
        }
        List<Long> professionalIds = campusRecruitmentDto.getProfessionalIds();
        List<CampusRecruitmentProfessionalRPo> campusRecruitmentProfessionalRPos = professionalIds.parallelStream().map(e -> {
            CampusRecruitmentProfessionalRPo campusRecruitmentProfessionalRPo = new CampusRecruitmentProfessionalRPo();
            campusRecruitmentProfessionalRPo.setProfessionalId(e);
            campusRecruitmentProfessionalRPo.setCampusRecruitmentId(campusRecruitmentPo.getId());
            return campusRecruitmentProfessionalRPo;
        }).collect(Collectors.toList());
        return campusRecruitmentProfessionalRService.insertBatch(campusRecruitmentProfessionalRPos);
    }

    @Override
    public PagedResult list(PageDto pageDto, Long enterpriseId) {
        Integer startLine = (pageDto.getPage() - 1) * pageDto.getLimit();
        List<CampusRecruitmentPo> campusRecruitmentPos = super.baseMapper.selectPageByEnterpriseId(enterpriseId, startLine, pageDto.getLimit());
        List<CampusRecruitmentVo> campusRecruitmentVos = campusRecruitmentPos.parallelStream().map(e -> campusRecruitmentPoToVo(e)).collect(Collectors.toList());
        PagedResult<CampusRecruitmentVo> pagedResult = new PagedResult<>();
        pagedResult.setData(campusRecruitmentVos);
        pagedResult.setCount(super.baseMapper.selectCountByEnterpriseId(enterpriseId));
        return pagedResult;
    }

    @Override
    public Boolean deleteByCampusRecruitmentId(Long campusRecruitmentId, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        checkCampusRecruitmentPo(campusRecruitmentId, enterpriseVo.getId());
        if(!super.deleteById(campusRecruitmentId)){
            throw JobException.CAMPUS_RECRUITMENT_DELETE_EXCEPTION;
        }
        return campusRecruitmentProfessionalRService.deleteByCampusRecruitmentId(campusRecruitmentId);
    }

    @Override
    public CampusRecruitmentVo getById(Long campusRecruitmentId, HttpSession httpSession) {
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        CampusRecruitmentPo campusRecruitmentPo = checkCampusRecruitmentPo(campusRecruitmentId, enterpriseVo.getId());
        return campusRecruitmentPoToVo(campusRecruitmentPo);
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadGeneralRegulation(String generalRegulationFileName) throws IOException {
        String filePath = tmpPath + generalRegulationFileName;
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", new String(file.getFilename().getBytes("UTF-8"),"iso-8859-1")));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }

    @Override
    public Boolean updateById(CampusRecruitmentDto campusRecruitmentDto, HttpSession httpSession) throws IOException {
        if(StringUtils.isEmpty(campusRecruitmentDto.getId())){
            throw JobException.NULL_ID_EXCEPTION;
        }
        EnterpriseVo enterpriseVo = checkEnterpriseVo(httpSession);
        CampusRecruitmentPo campusRecruitmentPo = checkCampusRecruitmentPo(campusRecruitmentDto.getId(), enterpriseVo.getId());
        BeanUtil.copyProperties(campusRecruitmentDto, campusRecruitmentPo);
        MultipartFile generalRegulation = campusRecruitmentDto.getGeneralRegulation();
        String fileName = saveFile(generalRegulation);
        campusRecruitmentPo.setGeneralRegulationFileName(fileName);
        if(!super.updateById(campusRecruitmentPo)){
            throw JobException.CAMPUS_RECRUITMENT_UPDATE_EXCEPTION;
        }
        List<Long> professionalIds = campusRecruitmentDto.getProfessionalIds();
        List<CampusRecruitmentProfessionalRPo> campusRecruitmentProfessionalRPos = campusRecruitmentProfessionalRService.listByCampusRecruitmentId(campusRecruitmentDto.getId());
        List<Long> oldProfessionalIds = campusRecruitmentProfessionalRPos.parallelStream().map(e -> e.getProfessionalId()).collect(Collectors.toList());
        List<Long> addProfessionalIds = new ArrayList<>();
        List<Long> repetitiveProfessionalIds = new ArrayList<>();
        for(Long professionalId : professionalIds){
            boolean flag = false;
            for(Long oldProfessionalId : oldProfessionalIds){
                if(professionalId.equals(oldProfessionalId)){
                    repetitiveProfessionalIds.add(professionalId);
                    flag = true;
                    break;
                }
            }
            if(!flag){
                addProfessionalIds.add(professionalId);
            }
        }
        oldProfessionalIds.removeAll(repetitiveProfessionalIds);
        if(oldProfessionalIds.size() != 0){
            if(!campusRecruitmentProfessionalRService.deleteByProfessionalIds(campusRecruitmentDto.getId(), oldProfessionalIds)){
                return false;
            }
        }
        List<CampusRecruitmentProfessionalRPo> addCampusRecruitmentProfessionalRPos = addProfessionalIds.parallelStream().map(e -> {
            CampusRecruitmentProfessionalRPo campusRecruitmentProfessionalRPo = new CampusRecruitmentProfessionalRPo();
            campusRecruitmentProfessionalRPo.setProfessionalId(e);
            campusRecruitmentProfessionalRPo.setCampusRecruitmentId(campusRecruitmentPo.getId());
            return campusRecruitmentProfessionalRPo;
        }).collect(Collectors.toList());
        if(addCampusRecruitmentProfessionalRPos.size() == 0){
            return true;
        }
        return campusRecruitmentProfessionalRService.insertBatch(addCampusRecruitmentProfessionalRPos);
    }

    private CampusRecruitmentPo checkCampusRecruitmentPo(Long campusRecruitmentId, Long enterpriseId){
        CampusRecruitmentPo campusRecruitmentPo = super.baseMapper.selectOneById(campusRecruitmentId, enterpriseId);
        if(campusRecruitmentPo == null){
            throw JobException.NULL_CAMPUS_RECRUITMENT_EXCEPTION;
        }
        return campusRecruitmentPo;
    }

    private CampusRecruitmentVo campusRecruitmentPoToVo(CampusRecruitmentPo campusRecruitmentPo){
        CampusRecruitmentVo campusRecruitmentVo = new CampusRecruitmentVo();
        BeanUtil.copyProperties(campusRecruitmentPo, campusRecruitmentVo);
        campusRecruitmentVo.setReleaseTime(campusRecruitmentPo.getGmtCreate());
        List<CampusRecruitmentProfessionalRPo> campusRecruitmentProfessionalRPos = campusRecruitmentProfessionalRService.listByCampusRecruitmentId(campusRecruitmentPo.getId());
        List<Long> professionalIds = campusRecruitmentProfessionalRPos.parallelStream().map(e -> e.getProfessionalId()).collect(Collectors.toList());
        List<ProfessionalVo> professionalVoList = professionalService.listByProfessionalIds(professionalIds);
        campusRecruitmentVo.setProfessionalVoList(professionalVoList);
//        logger.error("professionalVoList:" + professionalVoList.size());
        return campusRecruitmentVo;
    }

    private String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        int pointIndex = originalFilename.lastIndexOf(".");
        String fileType = originalFilename.substring(pointIndex + 1);
        String fileName = originalFilename.substring(0, pointIndex) + "_" + System.currentTimeMillis() + "." + fileType;
        File file = new File(tmpPath + fileName);
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            if (!file.getParentFile().mkdirs()) {
                logger.info("创建临时保存文件的目录失败");
                throw JobException.LICENSE_TEMP_DIRECTORY_EXCEPTION;
            } else {
                multipartFile.transferTo(file);
            }
        } else {
            multipartFile.transferTo(file);
        }
        return fileName;
    }

    private EnterpriseVo checkEnterpriseVo(HttpSession httpSession) {
        Object enterpriseObj = httpSession.getAttribute("enterpriseVo");
        if (StringUtils.isEmpty(enterpriseObj)) {
            throw JobException.NOT_LOGGED_IN_EXCEPTION;
        }
        return (EnterpriseVo) enterpriseObj;
    }
}