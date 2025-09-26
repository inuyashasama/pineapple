package com.pine.pineapple;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pine.pineapple.mapper")
public class PineappleApplication {

    public static void main(String[] args) {
        SpringApplication.run(PineappleApplication.class, args);
    }

}
