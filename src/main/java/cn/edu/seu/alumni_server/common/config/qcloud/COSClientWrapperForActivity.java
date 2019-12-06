package cn.edu.seu.alumni_server.common.config.qcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class COSClientWrapperForActivity {

	@Autowired
	private QCloudCOSConfig qCloudCOSConfig;
	@Value("${qCloud.cos.activities.bucketName}")
	private String bucketName;
	@Value("${qCloud.cos.activities.bucketPath}")
	private String bucketPath;

	@Bean
	public QCloudCOSClientHolder qCloudCOSClientHolder() {
		return new QCloudCOSClientHolder(this.qCloudCOSConfig, this.bucketName, this.bucketPath);
	}
}