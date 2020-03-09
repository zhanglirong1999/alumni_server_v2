package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.service.AccountService;
import cn.edu.seu.alumni_server.validation.annotaion.ValidWebParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountMapper accountMapper;

	@Override
	public Long getAccountNumber() {
		return this.accountMapper.getAccountNumber() + 1 + 2000;  // 老板要求加两千
	}

	@Override
	public boolean hasRegistered(@ValidWebParameter Long accountId) {
		return this.accountMapper.selectByPrimaryKey(accountId).getRegistered();
	}
}
