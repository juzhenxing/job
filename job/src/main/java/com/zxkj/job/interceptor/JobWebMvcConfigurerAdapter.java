//package com.zxkj.job.interceptor;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
// * @author juzhenxing 2018/3/31 17:15
// */
//@Configuration
//@EnableWebMvc
//public class JobWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/undergraduate/find-password").setViewName("find_password");
////        registry.addViewController("/undergraduate/register").setViewName("register");
//        super.addViewControllers(registry);
//    }
//}