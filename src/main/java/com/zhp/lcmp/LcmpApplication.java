package com.zhp.lcmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 *
 * @author ZhaoHP
 * @ClassName LcmpApplication
 * @date 2020/1/23 16:11
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.zhp.lcmp.dao"})
public class LcmpApplication {

    public static void main(String[] args) {

        SpringApplication.run(LcmpApplication.class, args);
    }

}
