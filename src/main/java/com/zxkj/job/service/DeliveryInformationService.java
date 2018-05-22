package com.zxkj.job.service;

import com.baomidou.mybatisplus.service.IService;
import com.zxkj.job.bean.dto.DeliveryInformationDto;
import com.zxkj.job.bean.dto.PageDto;
import com.zxkj.job.bean.dto.ProfessionalDto;
import com.zxkj.job.bean.po.DeliveryInformationPo;
import com.zxkj.job.bean.po.ProfessionalPo;
import com.zxkj.job.bean.vo.DeliveryInformationVo;
import com.zxkj.job.bean.vo.ProfessionalUpdateVo;
import com.zxkj.job.bean.vo.ProfessionalVo;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.enums.StatusType;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface DeliveryInformationService extends IService<DeliveryInformationPo> {

    Boolean add(DeliveryInformationDto deliveryInformationDto, HttpSession httpSession);

    PagedResult list(PageDto pageDto, HttpSession httpSession);

    DeliveryInformationVo getByDeliveryInformationId(Long deliveryInformationId);

    Boolean updateById(DeliveryInformationDto deliveryInformationDto);

    List<DeliveryInformationVo> getByResumeId(Long resumeId);

    PagedResult listByEnterpriseId(PageDto pageDto, HttpSession httpSession);

    Boolean updateStatusTypeById(Long deliveryInformationId, StatusType statusType) throws MessagingException;

    List<DeliveryInformationVo> getByProfessionalId(Long professionalId);

}
