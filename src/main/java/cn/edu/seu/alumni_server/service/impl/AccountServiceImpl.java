package cn.edu.seu.alumni_server.service.impl;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.dao.entity.Account;
import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.service.AccountService;
import cn.edu.seu.alumni_server.service.QCloudFileManager;
import cn.edu.seu.alumni_server.validation.annotaion.ValidWebParameter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service
@Validated
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private QCloudFileManager qCloudFileManager;

	@Override
	public Long getAccountNumber() {
		return this.accountMapper.getAccountNumber() + 1 + 2000;  // 老板要求加两千
	}

	@Override
	public boolean hasRegistered(@ValidWebParameter Long accountId) {
		return this.accountMapper.selectByPrimaryKey(accountId).getRegistered();
	}

	@Override
	public String updateAccountAvatarByFile(
		@ValidWebParameter
			Long accountId,
		@ValidWebParameter
			MultipartFile multipartFile
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
		String ans = this.qCloudFileManager.uploadAndDeleteFile(file);
		// 开始修改 account
		this.accountMapper.updateAccountAvatar(accountId, ans);
		return ans;
	}

	@Override
	public String updateAccountAvatarByURL(
		@ValidWebParameter
			Long accountId,
		@ValidWebParameter
			String url
	) throws IOException {
		String ans = this.qCloudFileManager.saveAccountAvatar(
			url, Utils.generateId() + ".png"
		);
		this.accountMapper.updateAccountAvatar(accountId, ans);
		return ans;
	}

	@Override
	@Transactional
	public void transAccountAvatarFromURLToURL() throws IOException {
		List<Account> accounts =
			this.accountMapper.selectAllValidAccounts();
		for (Account account : accounts) {
			String avatar = account.getAvatar();
			if (avatar != null &&
				!avatar.equals("") &&
				avatar.startsWith("https://wx.qlogo.cn/")
			) {
				String newURL = this.qCloudFileManager.saveAccountAvatar(
					avatar, Utils.generateId() + ".png"
				);
				this.accountMapper.updateAccountAvatar(
					account.getAccountId(),
					newURL
				);
			}
		}
	}
}
