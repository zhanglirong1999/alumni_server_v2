package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.config.cos.QCloudHolder;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import cn.edu.seu.alumni_server.service.fail.ActivityFailPrompt;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import javax.net.ssl.HttpsURLConnection;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Getter
@Setter
public class FileManagerImpl implements QCloudFileManager {

	@Autowired
	QCloudHolder qCloudHolder;

	@Autowired
	ActivityFailPrompt activityFailPrompt;

	@Override
	public File convertMultipartFileToFile(MultipartFile multipartFile, String newName)
		throws IOException {
		// 获取到文件源路径
		String originalFilename = multipartFile.getOriginalFilename();
		if (originalFilename == null) {
			throw new IOException(
				this.activityFailPrompt.getUserPrompt(
					"将原图片转换成为可上传文件", 8
				)
			);
		}
		// 配置结果.
		File ansFile = new File(newName == null ? originalFilename : newName);
		// 使用 commons-io
		FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), ansFile);
		return ansFile;
	}

	@Override
	public String uploadFile(
		File file,
		String... subDirs
	) {
		// 首先获取到
		String ansFileUrl = this.getBaseUrl();
		// 创建新的文件路径
		StringBuilder bucketNameBuilder = new StringBuilder();
		for (String subDir : subDirs) {
			bucketNameBuilder.append("/").append(subDir);
		}
		String uploadFileKey =
			bucketNameBuilder.append("/").append(
				file.getName()
			).toString();
		// 上传文件
		// 创建客户端
		COSClient cosClient = this.qCloudHolder.newCOSClient();
		// 上传图片文件到指定的桶中
		PutObjectRequest putObjectRequest = new PutObjectRequest(
			this.qCloudHolder.getBucketName(),
			uploadFileKey,
			file
		);
		cosClient.putObject(putObjectRequest);
		// 关闭
		this.qCloudHolder.closeCOSClient(cosClient);
		return ansFileUrl + putObjectRequest.getKey();
	}

	@Override
	public String uploadAndDeleteFile(
		File file, String... subDirs
	) {
		String ans = this.uploadFile(file, subDirs);
		Utils.deleteFileUnderProjectDir(file.getName());
		return ans;
	}

	public String buildNewFileNameWithType(MultipartFile multipartFile,
		String newFileNameWithoutType
	) {
		// 获取到原始文件的类型
		return newFileNameWithoutType + Objects.requireNonNull(
			multipartFile.getOriginalFilename()
		).substring(
			multipartFile.getOriginalFilename().lastIndexOf(".")
		);
	}

	@Override
	public String getBaseUrl() {
		return this.qCloudHolder.getBaseUrl();
	}

	@Override
	public Boolean hasObject(String objectKey) {
		Boolean ans = null;
		// 创建客户端
		COSClient cosClient = this.qCloudHolder.newCOSClient();
		// 尝试获取元数据
		try {
			ObjectMetadata objectMetadata = cosClient.getObjectMetadata(
				this.qCloudHolder.getBucketName(), objectKey
			);
			ans = objectMetadata != null;
		} catch (Exception any) {
			ans = false;
		} finally {
			if (cosClient != null) {
				cosClient.shutdown();
			}
		}
		return ans;
	}

	@Override
	public void deleteObject(String objectKey) throws IOException {
		// 创建客户端
		COSClient cosClient = this.qCloudHolder.newCOSClient();
		if (cosClient != null) {
			cosClient.deleteObject(this.qCloudHolder.getBucketName(), objectKey);
			cosClient.shutdown();
		} else {
			throw new IOException("创建客户端失败.");
		}
	}

	@Override
	public String saveAccountAvatar(
		String oAvatar,
		String newFileName,
		String... subDirs
	) throws IOException {
		// 获取 inputstream
		URL url = new URL(oAvatar);
		HttpsURLConnection httpsURLConnection =
			(HttpsURLConnection) url.openConnection();
		InputStream in = httpsURLConnection.getInputStream();
		// 转成 file 上传
		File file = new File(newFileName);
		FileUtils.copyInputStreamToFile(in, file);
		return this.uploadAndDeleteFile(file, subDirs);
	}
}
