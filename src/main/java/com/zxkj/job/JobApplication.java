package com.zxkj.job;

import com.zxkj.job.interceptor.EnterpriseInterceptor;
import com.zxkj.job.interceptor.UndergraduateInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class JobApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//注册自定义拦截器，添加拦截路径和排除拦截路径
		registry.addInterceptor(new UndergraduateInterceptor()).addPathPatterns("/undergraduate/**");
		registry.addInterceptor(new EnterpriseInterceptor()).addPathPatterns("/enterprise/**").excludePathPatterns("/enterprise/login");
	}
}