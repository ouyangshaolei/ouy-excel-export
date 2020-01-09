package com.gwghk.crm.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.gwghk.crm.dao2", sqlSessionFactoryRef = "DB2Factory")
@ConfigurationProperties("mybatis2")
public class MybatisConfig2 {
    // 配置类型别名
    private String typeAliasesPackage;
    // 配置mapper的扫描，找到所有的mapper.xml映射文件
    private String mapperLocations;
    // 加载全局的配置文件
    private String configLocation;
    @Bean(name = "DB2dataSource")
    @ConfigurationProperties(prefix = "spring.datasource2") //读取的数据源前缀, 跟yml文件对应
    public DataSource DB2dataSource(){
        return new DruidDataSource();
    }

    @Bean(name = "DB2Factory")
    public SqlSessionFactory DB2Factory(@Qualifier("DB2dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        //指定起别名的包
        bean.setTypeAliasesPackage(typeAliasesPackage);
        bean.setDataSource(dataSource);
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocations);
        bean.setMapperLocations(resources);
        bean.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));
        return bean.getObject();
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
