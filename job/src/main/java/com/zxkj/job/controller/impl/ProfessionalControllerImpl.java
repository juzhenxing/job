package com.zxkj.job.controller.impl;

import com.zxkj.job.bean.po.ProfessionalPo;
import com.zxkj.job.bean.vo.CareerTalkUpdateVo;
import com.zxkj.job.bean.vo.EnterpriseVo;
import com.zxkj.job.common.BaseControllerImpl;
import com.zxkj.job.common.bean.PagedResult;
import com.zxkj.job.controller.EnterpriseController;
import com.zxkj.job.controller.ProfessionalController;
import com.zxkj.job.enums.CheckStateType;
import com.zxkj.job.exp.JobException;
import com.zxkj.job.service.CareerTalkService;
import com.zxkj.job.service.EnterpriseService;
import com.zxkj.job.service.ProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("job")
public class ProfessionalControllerImpl extends BaseControllerImpl<ProfessionalService, ProfessionalPo> implements ProfessionalController {


}
