package cn.edu.seu.alumni_server.service;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;


public interface QCloudFileManager {

	// 构建新的名字
	String buildNewFileNameWithType(
		MultipartFile multipartFile,
		String newFileNameWithoutType
	);

	// 将输入的文件转换成一个标准的文件用以之后的上传
	File convertMultipartFileToFile(
		MultipartFile multipartFile,
		String newName
	) throws IOException;

	/**
	 * 完成对于一个文件的上传.
	 *
	 * @param multipartFile 原始文件.
	 * @param newFileNameWithoutType 无类型的新名字, e.g. "new-name-file"
	 * @param subDirs url 子目录, e.g. "test", "imgs", 注意没有 "/"
	 * @return "https://root-test-bucket-1258701411.cos.ap-shanghai.myqcloud.com/test/imgs/test-img-02.png"
	 * @throws IOException 不完整的异常处理.
	 */
	String uploadOneFile(
		MultipartFile multipartFile,
		String newFileNameWithoutType,
		String... subDirs
	) throws IOException;

	String getBaseUrl();

	// 判断一个桶中是否又一个对象
	Boolean hasObject(String objectKey);

	// 删除一个对象
	void deleteObject(String objectKey) throws IOException;
}
