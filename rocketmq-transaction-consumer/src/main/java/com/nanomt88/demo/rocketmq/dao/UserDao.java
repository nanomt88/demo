package com.nanomt88.demo.rocketmq.dao;

import com.nanomt88.demo.rocketmq.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The Interface UserJpaDao.
 * @author abel
 */
public interface UserDao extends JpaRepository<User, Long> {
	
	/**
	 * Find by name.
	 *
	 * @param name the name
	 * @return the user
	 */
	User findByName(String name);

	/**
	 * Find by name and user name.
	 * 如果参数名为多个字母组成，请首字母大写。勿使用驼峰命名，jpa不识别驼峰
	 * @param name the name
	 * @param age the age
	 * @return the user
	 */
	User findByNameAndUsername(String name, Integer age);

	/**
	 * Find user.
	 * User为@Entity 的名字
	 * @param name the name
	 * @return the user
	 */
	@Query("select u from User u where u.name=:name")
	User findUser(@Param("name") String name);

}
