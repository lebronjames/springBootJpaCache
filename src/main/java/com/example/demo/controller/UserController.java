package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.UserRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/user")
@Api(value = "user", description = "用户管理")
public class UserController {

private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/save")
	public void save(@RequestParam(value="name")String name,@RequestParam(value="age")String age) {
		userRepository.save(new User(name,Integer.valueOf(age)));
	}
	
	@PostMapping("/saveUser")
	public void saveUser(@RequestParam(value="name")String name,@RequestParam(value="age")String age) {
		userRepository.save(new User(name,Integer.valueOf(age)));
	}
	
	@GetMapping("/list")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	@GetMapping("/delete")
	public void delete(@RequestParam(value="id")String id) {
		userRepository.delete(Integer.valueOf(id));
	}
	
	@GetMapping("/deleteName")
	public void deleteByName(@RequestParam(value="name")String name) {
		userRepository.deleteByName(name);
	}
	
	@GetMapping("/findUser")
	public User findUser(@RequestParam(value="name")String name) {
		return userRepository.findUser(name);
	}
}
