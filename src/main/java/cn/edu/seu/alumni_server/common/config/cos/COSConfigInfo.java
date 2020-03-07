package cn.edu.seu.alumni_server.common.config.cos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:config/cos_config/qcloud-config.properties"})
public class COSConfigInfo {

	@Value("${qCloud.cos.appId}")
	public Integer appId;
	@Value("${qCloud.cos.secretId}")
	public String secretId;
	@Value("${qCloud.cos.secretKey}")
	public String secretKey;
	@Value("${qCloud.cos.regionString}")
	public String regionString;
}
