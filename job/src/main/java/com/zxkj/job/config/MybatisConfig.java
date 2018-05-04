package com.zxkj.job.config;

import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by chenyunan on 2017/11/23.
 */
@Configuration
@MapperScan("com.zxkj.job.mapper*")
public class MybatisConfig {

    /**
     * 性能分析拦截器
     * @return
     */
    @Bean
    @Profile({"local"})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * SQL 执行分析拦截器
     * @return
     */
//    @Bean
//    @Profile({"prod", "dev", "test", "local"})
//    public SqlExplainInterceptor sqlExplainInterceptor() {
//        return new SqlExplainInterceptor();
//    }

    /**
     * 乐观锁，暂时不用
     * @return
     */
//    @Bean
//    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
//        return new OptimisticLockerInterceptor();
//    }

    /**
     * 插入或更新固定字段，暂时不用
     * @return
     */
//    @Bean
//    public MetaObjectHandler metaObjectHandler() {
//        return new BeanMetaObjectHandler();
//    }

}
