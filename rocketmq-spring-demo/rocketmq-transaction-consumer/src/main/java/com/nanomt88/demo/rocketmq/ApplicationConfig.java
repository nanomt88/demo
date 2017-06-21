package com.nanomt88.demo.rocketmq;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories({"com.nanomt88.demo.rocketmq.dao"})
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
public class ApplicationConfig {

	@Autowired
	private Environment env;

//可以使用Environment 进行取值，也可以使用 @Value 注解
//	@Value("${spring.datasource.url}")
//	private String dbUrl;
//
//	@Value("${spring.datasource.username}")
//	private String dbUsername;
//
//	@Value("${spring.datasource.password}")
//	private String dbPassword;

	@Bean(name="dataSource")
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("com.mysql.jdbc.Driver"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
		dataSource.setInitialSize(5);
		dataSource.setMaxActive(20);
		dataSource.setMaxIdle(20);
		dataSource.setMinIdle(5);
		dataSource.setTestOnBorrow(true);
		dataSource.setValidationQuery("select 1");
		return dataSource;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setGenerateDdl(false);
		adapter.setDatabase(Database.MYSQL);
		//adapter.setShowSql(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(adapter);
		factory.setPackagesToScan("com.nanomt88.demo.rocketmq.entity");
		factory.setJpaDialect(new HibernateJpaDialect());

		Properties props = new Properties();
		props.put("hibernate.show_sql",true);
		props.put("hibernate.jdbc.batch_size", 50);
		props.put("hibernate.physical_naming_strategy", "com.nanomt88.demo.rocketmq.ZLBNamingStrategy");

//		hibernate5 以前，表映射使用一下的方式，hibernate5以后该参数一分为2：
// 			hibernate.physical_naming_strategy ： org.hibernate.boot.model.naming.PhysicalNamingStrategy
//		   	hibernate.implicit_naming_strategy ： org.hibernate.boot.model.naming.ImplicitNamingStrategy
//		props.put("hibernate.ejb.naming_strategy", "com.nanomt88.demo.rocketmq.ZLBNamingStrategy");


		factory.setJpaProperties(props);
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}

//	@Bean
//	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//		return new PropertySourcesPlaceholderConfigurer();
//	}
}
