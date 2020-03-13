package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.dao.entity.Account;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AccountMapper extends Mapper<Account> {
    /**
     * 仅用做数据迁移用，补充部分字段
     */
    void dataSync();

    Long getAccountNumber();

    /**
     * 通过accountId查询acount信息
     * @param accountId
     * @return
     */
    Account selectByAccountId(long accountId);
}