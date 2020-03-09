package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.validation.annotaion.ValidWebParameter;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AccountService {

	Long getAccountNumber();

	boolean hasRegistered(@ValidWebParameter Long accountId);
}
