package cn.edu.seu.alumni_server.common.config.qcloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

//@PropertySource("classpath:qCloudConfigs.yml")
@Configuration
public class QCloudCOSConfig {

	@Value("${qCloud.cos.appId}")
	public Integer appId;
	@Value("${qCloud.cos.secretId}")
	public String secretId;
	@Value("${qCloud.cos.secretKey}")
	public String secretKey;
	@Value("${qCloud.cos.regionString}")
	public String regionString;

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
