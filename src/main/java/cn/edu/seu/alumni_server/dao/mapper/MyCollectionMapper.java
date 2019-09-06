package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.dao.entity.MyCollection;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface MyCollectionMapper extends Mapper<MyCollection> {
    MyCollection queryCollenctionByAccount(Long accountId);

}