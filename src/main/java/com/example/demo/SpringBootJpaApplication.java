package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching//注解开启缓存功能
@SpringBootApplication
public class SpringBootJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaApplication.class, args);
	}
}
