package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.dao.entity.Collection;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CollectionMapper extends Mapper<Collection> {

    Collection queryCollenctionByAccount(Long accountId);
}
