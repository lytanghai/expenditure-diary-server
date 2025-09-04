//package com.expenditure_diary.expenditure_diary.module.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.*;
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
//                classes = com.expenditure_diary.expenditure_diary.annotate.PrimaryDbRepository.class
//        ),
//        entityManagerFactoryRef = "primaryEntityManager",
//        transactionManagerRef = "primaryTransactionManager"
//)
//public class PrimaryDbConfig {
//
//    @Primary
//    @Bean(name = "primaryDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.primary")
//    public DataSource primaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Primary
//    @Bean(name = "primaryEntityManager")
//    public LocalContainerEntityManagerFactoryBean primaryEntityManager(@Autowired JpaProperties jpaProperties) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(primaryDataSource());
//        em.setPackagesToScan("com.expenditure_diary.expenditure_diary.module.model");
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        em.setJpaPropertyMap(jpaProperties.getProperties());
//        return em;
//    }
//
//    @Primary
//    @Bean(name = "primaryTransactionManager")
//    public JpaTransactionManager primaryTransactionManager(
//            @Qualifier("primaryEntityManager") LocalContainerEntityManagerFactoryBean primaryEntityManager) {
//        return new JpaTransactionManager(primaryEntityManager.getObject());
//    }
//}
