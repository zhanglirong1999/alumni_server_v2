package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.validation.annotaion.ValidWebParameter;
import java.io.IOException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Validated
public interface AccountService {

	Long getAccountNumber();

	boolean hasRegistered(@ValidWebParameter Long accountId);

	String updateAccountAvatarByFile(
		@ValidWebParameter
			Long accountId,
		@ValidWebParameter
			MultipartFile multipartFile
	) throws IOException;

	String updateAccountAvatarByURL(
		@ValidWebParameter
			Long accountId,
		@ValidWebParameter
			String url
	) throws IOException;


	void transAccountAvatarFromURLToURL() throws IOException;
}
