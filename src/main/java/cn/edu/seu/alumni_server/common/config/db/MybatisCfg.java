package cn.edu.seu.alumni_server.common.config.db;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.Properties;

//@Configuration
public class MybatisCfg {
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
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
