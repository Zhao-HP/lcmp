package com.zhp.lcmp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author ZhaoHP
 * @date 2020/5/4 11:35
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.p6spy.engine.spy.P6SpyDriver");
        config.setJdbcUrl("jdbc:p6spy:mysql://127.0.0.1:3306/dblcmp?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false");
        config.setUsername("root");
        config.setPassword("root");
        config.setConnectionTimeout(30000);
        config.setMaximumPoolSize(15);
        config.setMinimumIdle(10);
        return new HikariDataSource(config);
    }
}
