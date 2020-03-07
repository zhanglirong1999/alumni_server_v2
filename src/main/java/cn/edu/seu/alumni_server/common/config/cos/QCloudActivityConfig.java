package cn.edu.seu.alumni_server.common.config.cos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QCloudActivityConfig {

	@Autowired
	private COSConfigInfo COSConfigInfo;
	@Value("${qCloud.cos.activities.bucketName}")
	private String bucketName;
	@Value("${qCloud.cos.activities.bucketPath}")
	private String bucketPath;

	@Bean
	public QCloudHolder qCloudHolder() {
		return new QCloudHolder(this.COSConfigInfo, this.bucketName, this.bucketPath);
	}
}