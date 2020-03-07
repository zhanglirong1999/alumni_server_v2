package cn.edu.seu.alumni_server.common.config.cos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QCloudHolder {

	private COSConfigInfo COSConfigInfo;
	private Region region;
	private COSCredentials cred;
	private ClientConfig clientConfig;
	private String bucketName;
	private String baseUrl;

	public QCloudHolder(
		COSConfigInfo COSConfigInfo,
		String bucketName,
		String baseUrl
	) {
		this.init(COSConfigInfo, bucketName, baseUrl);
	}

	public void init(
		COSConfigInfo COSConfigInfo,
		String bucketName,
		String baseUrl
	) {
		this.COSConfigInfo = COSConfigInfo;
		this.region = new Region(COSConfigInfo.regionString);
		this.cred = new BasicCOSCredentials(COSConfigInfo.secretId, COSConfigInfo.secretKey);
		this.clientConfig = new ClientConfig(this.region);
		this.bucketName = bucketName;
		this.baseUrl = baseUrl;
	}

	public COSClient newCOSClient() {
		return new COSClient(this.cred, this.clientConfig);
	}

	public void closeCOSClient(COSClient cosClient) {
		cosClient.shutdown();
	}
}