//package com.expenditure_diary.expenditure_diary.module.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableJpaRepositories(
//        basePackages = "com.expenditure_diary.expenditure_diary.module.repository",
//        includeFilters = @ComponentScan.Filter(
//                type = FilterType.ANNOTATION,
//                classes = com.expenditure_diary.expenditure_diary.annotate.SecondaryDbRepository.class
//        ),
//        entityManagerFactoryRef = "secondaryEntityManager",
//        transactionManagerRef = "secondaryTransactionManager"
//)
//public class SecondaryDbConfig {
//
//    @Bean(name = "secondaryDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.secondary")
//    public DataSource secondaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "secondaryEntityManager")
//    public LocalContainerEntityManagerFactoryBean secondaryEntityManager(@Autowired JpaProperties jpaProperties) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(secondaryDataSource());
//        em.setPackagesToScan("com.expenditure_diary.expenditure_diary.module.model");
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        em.setJpaPropertyMap(jpaProperties.getProperties());
//        return em;
//    }
//
//    @Bean(name = "secondaryTransactionManager")
//    public JpaTransactionManager secondaryTransactionManager(
//            @Qualifier("secondaryEntityManager") LocalContainerEntityManagerFactoryBean secondaryEntityManager) {
//        return new JpaTransactionManager(secondaryEntityManager.getObject());
//    }
//}
