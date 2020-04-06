package com.zhp.lcmp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Bean
    public Converter<String, Date> addNewConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse( source);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return date;
            }
        };
    }

}
