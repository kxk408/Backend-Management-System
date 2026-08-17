package com.kxk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class KxkWebManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(KxkWebManagementApplication.class, args);
    }

}
