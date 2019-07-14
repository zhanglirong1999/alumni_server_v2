package cn.edu.seu.alumni_server.common.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

//@Configuration
public class DruidCfg {

    @Value("db.mysql.url")
    String url;
    @Value("db.mysql.username")
    String username;
    @Value("db.mysql.password")
    String password;

    @Bean
    public DataSource dataSource() {
        DruidDataSource ds = new DruidDataSource();

        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(url);

        // 配置初始化大小、最小、最大
        ds.setInitialSize(1);
        ds.setMinIdle(10);
        ds.setMaxActive(30);
        // 配置获取连接等待超时的时间
        ds.setMaxWait(60 * 1000);
        ds.setMinEvictableIdleTimeMillis(600 * 1000);
        ds.setMaxEvictableIdleTimeMillis(900 * 1000);

        return ds;
    }
}
