package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.common.exceptions.ActivityServiceException;
import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;


public interface QCloudFileManager {

	Boolean isLegalMultipartFile(MultipartFile multipartFile);

	// 将输入的文件转换成一个标准的文件用以之后的上传
	File convertMultipartFileToFile(MultipartFile multipartFile, String newName)
		throws IOException, ActivityServiceException;

	// 上传一个 file 到指定配置的 cos 的 bucket 中
	String uploadFileToQCloudBySuffixes(File file, String suffixKey);

	// 获取一个新的 url 路径
	String makeUrlString(String suffixKey);
}
