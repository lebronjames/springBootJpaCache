package com.example.demo.service;

import javax.transaction.Transactional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.User;

@CacheConfig(cacheNames="users")//在数据访问接口中，增加缓存配置注解
public interface UserRepository extends JpaRepository<User,Integer> {

	User findByName(String name);
	
	User findByNameAndAge(String name, Integer age);
	
	@CachePut(key="#p0")//配置于函数上，能够根据参数定义条件来进行缓存
	User save(User user);
	
	@CacheEvict(key="#p0")//用在删除方法上，用来从缓存中移除相应数据
	void delete(Integer id);
	
	@CacheEvict(key="#p0")//用在删除方法上，用来从缓存中移除相应数据
	@Transactional
	@Modifying
	@Query("delete from User u where u.name=:name")
	void deleteByName(@Param("name") String name);
	
	//key：缓存对象存储在Map集合中的key值.#p0使用函数第一个参数作为缓存的key值
	//condition：缓存对象的条件，非必需，只有满足表达式条件的内容才会被缓存
	@Cacheable(key="#p0",condition="#p0.length()<10")//配置了findUser函数的返回值将被加入缓存
	@Query("from User u where u.name=:name")
	User findUser(@Param("name") String name);
}
