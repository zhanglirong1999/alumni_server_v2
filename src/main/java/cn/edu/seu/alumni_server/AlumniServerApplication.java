package cn.edu.seu.alumni_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(basePackages = "cn.edu.seu.alumni_server")
public class AlumniServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlumniServerApplication.class, args);
    }

}
