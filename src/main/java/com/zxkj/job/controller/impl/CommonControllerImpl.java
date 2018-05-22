package com.zxkj.job.controller.impl;

import com.zxkj.job.bean.po.ProfessionalPo;
import com.zxkj.job.controller.CommonController;
import com.zxkj.job.service.DeliveryInformationService;
import com.zxkj.job.service.ProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("common")
public class CommonControllerImpl implements CommonController {

    @Autowired
    DeliveryInformationService deliveryInformationService;

}
