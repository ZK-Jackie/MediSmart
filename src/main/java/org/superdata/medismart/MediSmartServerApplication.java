package org.superdata.medismart;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = "org.superdata.medismart")
@MapperScan("org.superdata.medismart.mapper.sql")
public class MediSmartServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediSmartServerApplication.class, args);
        log.info("项目启动成功！！！");
    }

}
