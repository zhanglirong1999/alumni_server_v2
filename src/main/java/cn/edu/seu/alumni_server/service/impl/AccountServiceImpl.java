package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountMapper accountMapper;

	@Override
	public Long getAccountNumber() {
		return this.accountMapper.getAccountNumber() + 1;
	}
}
