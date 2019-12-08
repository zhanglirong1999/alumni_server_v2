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

	/**
	 * 根据输入的后缀, 创建生成一个完整的 qcloud-url 地址
	 * 比如, /activities/imgs/123456.1.jpg => www.qcloud.xxx/activities/imgs/123456.1.jpg
	 * @param suffixKey /activities/imgs/123456.1.jpg
	 * @return www.qcloud.xxx/activities/imgs/123456.1.jpg
	 */
	String makeUrlString(String suffixKey);
}
