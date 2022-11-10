package com.hao.demo;

import com.hao.demo.config.SwaggerConfiguration;
import javafx.util.Pair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@EnableWebMvc
@SpringBootApplication
@Import(SwaggerConfiguration.class)
public class DemoApplication {

    @Bean
    public Map<Integer, Pair<Timestamp, Integer>> stockCache(){
        return new HashMap<>();
    }
    @Bean
    public Map<Integer, String> itemList(){
        return new HashMap<>();
    }
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
