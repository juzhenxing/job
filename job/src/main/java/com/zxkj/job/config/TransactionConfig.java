package com.zxkj.job.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by chenyunan on 2017/11/27.
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {

//    @Bean
//    public DataSourceTransactionManager transactionManager(
//            @Autowired DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }

    @Bean("transactionInterceptor")
    public TransactionInterceptor transactionInterceptor(
            @Autowired DataSourceTransactionManager transactionManager) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager);
        Properties transactionAttributes = new Properties();
		/*
		 * 1.PROPAGATION_REQUIRED（加入已有事务）: 尝试加入已经存在的事务中，如果没有则开启一个新的事务。
		 * 2.RROPAGATION_REQUIRES_NEW（独立事务）：挂起当前存在的事务，并开启一个全新的事务，新事务与已存在的事务之间彼此没有关系。
		 * 3.PROPAGATION_NESTED（嵌套事务）：在当前事务上开启一个子事务（Savepoint），如果递交主事务。那么连同子事务一同递交。如果递交子事务则保存点之前的所有事务都会被递交。
		 * 4.PROPAGATION_SUPPORTS（跟随环境）：是指 Spring 容器中如果当前没有事务存在，就以非事务方式执行；如果有，就使用当前事务。
		 * 5.PROPAGATION_NOT_SUPPORTED（非事务方式）：是指如果存在事务则将这个事务挂起，并使用新的数据库连接。新的数据库连接不使用事务。
		 * 6.PROPAGATION_NEVER（排除事务）：当存在事务时抛出异常，否则就已非事务方式运行。
		 * 7.PROPAGATION_MANDATORY（需要事务）：如果不存在事务就抛出异常，否则就已事务方式运行。
		 *
		 * 在Service类添加 @Transactional 注解，方法为 Properties 的 key 正则匹配到的添加事务，不写 * 默认其他方法不添加事务
		 * 注意：该配置具有强制性，拦截器只能生效一个，spring默认事务配置将失效
		 */
        transactionAttributes.setProperty("multi*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("add*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("save*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("find*", "PROPAGATION_REQUIRED,readOnly");
        transactionAttributes.setProperty("get*", "PROPAGATION_REQUIRED,readOnly");
        transactionAttributes.setProperty("load*", "PROPAGATION_REQUIRED,readOnly");
        transactionAttributes.setProperty("query*", "PROPAGATION_REQUIRED,readOnly");
        transactionAttributes.setProperty("select*", "PROPAGATION_REQUIRED,readOnly");
        transactionInterceptor.setTransactionAttributes(transactionAttributes);
        return transactionInterceptor;
    }

}
