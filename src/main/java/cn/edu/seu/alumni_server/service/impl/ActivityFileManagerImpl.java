package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.config.qcloud.QCloudCOSClientHolder;
import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import java.io.File;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Getter
@Setter
public class ActivityFileManagerImpl implements QCloudFileManager {

	@Autowired
	QCloudCOSClientHolder qCloudCOSClientHolder;

	@Override
	public Boolean isLegalMultipartFile(MultipartFile multipartFile) {
		return multipartFile != null && !multipartFile.equals("") && multipartFile.getSize() > 0;
	}

	@Override
	public File convertMultipartFileToFile(MultipartFile multipartFile, String newName)
		throws IOException, ActivityServiceException {
		// 获取到文件源路径
		String originalFilename = multipartFile.getOriginalFilename();
		if (originalFilename == null)
			throw new ActivityServiceException("The img original name is null.");
		// 配置结果.
		File ansFile = new File(newName == null ? originalFilename : newName);
		// 使用 commons-io
		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), ansFile);
		return ansFile;
	}

	@Override
	public String uploadFileToQCloud(File file, String key) {
		// 创建客户端
		COSClient cosClient = this.qCloudCOSClientHolder.newCOSClient();
		// 上传图片文件到指定的桶中
		PutObjectRequest putObjectRequest = new PutObjectRequest(
			this.qCloudCOSClientHolder.getBucketName(),
			key,
			file
		);
		cosClient.putObject(putObjectRequest);
		// 关闭.
		this.qCloudCOSClientHolder.closeCOSClient(cosClient);
		return key;
	}
}