package com.gwghk.crm.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.gwghk.crm.dao", sqlSessionFactoryRef = "DB1Factory")
@EnableTransactionManagement
@ConfigurationProperties("mybatis")
public class MyBatisConfig1 implements TransactionManagementConfigurer {
    // 配置类型别名
    private String typeAliasesPackage;
    // 配置mapper的扫描，找到所有的mapper.xml映射文件
    private String mapperLocations;
    // 加载全局的配置文件
    private String configLocation;

    @Bean(name = "DB1dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource1")
    public DataSource DB1dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "DB1Factory")
    @Primary
    public SqlSessionFactory DB1Factory(@Qualifier("DB1dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        //指定起别名的包
        bean.setTypeAliasesPackage(typeAliasesPackage);
        bean.setDataSource(dataSource);
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocations);
        bean.setMapperLocations(resources);
        bean.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));
        return bean.getObject();
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(DB1dataSource());
    }


    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }


    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }


    public String getMapperLocations() {
        return mapperLocations;
    }


    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }


    public String getConfigLocation() {
        return configLocation;
    }


    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

}
