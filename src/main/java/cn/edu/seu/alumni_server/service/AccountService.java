package cn.edu.seu.alumni_server.service;

import cn.edu.seu.alumni_server.controller.dto.AccountDTO;
import cn.edu.seu.alumni_server.common.dto.WebResponse;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    public WebResponse checkAccountInfo(AccountDTO accountDTO) {

//        if(accountDTO.getRegistered())
        return new WebResponse();
    }
}
