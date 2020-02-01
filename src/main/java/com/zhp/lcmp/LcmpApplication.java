package com.zhp.lcmp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author ZhaoHP
 * @ClassName LcmpApplication
 * @date 2020/1/23 16:11
 */
@SpringBootApplication()
@MapperScan(basePackages = {"com.zhp.lcmp.dao"})
public class LcmpApplication {

    public static void main(String[] args) {

        SpringApplication.run(LcmpApplication.class, args);
    }

}
