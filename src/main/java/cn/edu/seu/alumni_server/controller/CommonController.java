package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.annotation.web_response.WebResponseAPIMethod;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.web_response_dto.WebResponse;
import cn.edu.seu.alumni_server.interceptor.token.Acl;
import cn.edu.seu.alumni_server.service.AccountService;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Acl
public class CommonController {

	@Autowired
	QCloudFileManager qCloudFileManager;

	@Autowired
	AccountService accountService;

	@WebResponseAPIMethod
	@PostMapping("/uploadFile")
	public Object uploadFile(
		@RequestParam MultipartFile multipartFile
	) throws IOException {
		// 首先获取 newName
		String newNameWithoutType = String.valueOf(Utils.generateId());
		// 尽量保证上传的图片格式与原来的图片格式相匹配
		String newNameWithType = this.qCloudFileManager.buildNewFileNameWithType(
			multipartFile, newNameWithoutType
		);
		// 转换为可上传文件
		File file = this.qCloudFileManager.convertMultipartFileToFile(
			multipartFile, newNameWithType
		);
		// 上传并删除
		// 返回最终结果到 data 中
		return this.qCloudFileManager.uploadAndDeleteFile(file);
	}

	@DeleteMapping("/deleteFile")
	public WebResponse deleteFile(
		@RequestParam String fileUrl
	) {
		try {
			// 注意这里的删除不检查是否有这个文件, 直接就是删除请求
			this.qCloudFileManager.deleteObject(
				fileUrl.substring(fileUrl.lastIndexOf("/") + 1)
			);
			return new WebResponse<String>().success("删除成功.");
		} catch (IOException e) {
			return new WebResponse().fail("删除失败.");
		}
	}

	// 获取第几位用户
	@GetMapping("/accountNumber")
	@WebResponseAPIMethod
	public Object getAccountNumber() {
		return this.accountService.getAccountNumber();
	}

}
