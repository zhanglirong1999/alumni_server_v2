package cn.edu.seu.alumni_server.common.config.db;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

//@Configuration
public class MybatisCfg {
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        PageInterceptor pageHelper = new PageInterceptor();

        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("dialect", "mysql");
//        properties.setProperty("helperDialect", "mysql");
        pageHelper.setProperties(properties);

        bean.setPlugins(new Interceptor[]{pageHelper});

        return bean.getObject();
    }
}
