package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.config.qcloud.QCloudCOSClientHolder;
import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import cn.edu.seu.alumni_server.service.fail.ActivityFailPrompt;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
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

	@Autowired
	ActivityFailPrompt activityFailPrompt;


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
			throw new ActivityServiceException(
				this.activityFailPrompt.getUserPrompt(
					"将原图片转换成为可上传文件", 8
				)
			);
		// 配置结果.
		File ansFile = new File(newName == null ? originalFilename : newName);
		// 使用 commons-io
		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), ansFile);
		return ansFile;
	}

	@Override
	public String uploadFileToQCloudBySuffixes(File file, String suffixKey) {
		// 创建客户端
		COSClient cosClient = this.qCloudCOSClientHolder.newCOSClient();
		// 上传图片文件到指定的桶中
		PutObjectRequest putObjectRequest = new PutObjectRequest(
			this.qCloudCOSClientHolder.getBucketName(),
			suffixKey,
			file
		);
		cosClient.putObject(putObjectRequest);
		// 关闭.
		this.qCloudCOSClientHolder.closeCOSClient(cosClient);
		return putObjectRequest.getKey();
	}

	@Override
	public String makeUrlString(String suffixKey) {
		return this.qCloudCOSClientHolder.getPath() + suffixKey;
	}
}
