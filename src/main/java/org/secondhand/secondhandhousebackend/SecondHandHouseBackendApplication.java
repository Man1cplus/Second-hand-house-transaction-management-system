package org.secondhand.secondhandhousebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.secondhand.secondhandhousebackend.mapper")
public class SecondHandHouseBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondHandHouseBackendApplication.class, args);
    }

}
