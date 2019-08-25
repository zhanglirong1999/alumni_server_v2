package cn.edu.seu.alumni_server.dao.mapper;

import cn.edu.seu.alumni_server.controller.dto.FavoriteDTO;
import cn.edu.seu.alumni_server.dao.entity.Favorite;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface FavoriteMapper extends Mapper<Favorite> {
    List<FavoriteDTO> getFavoriteList(Long accountId);
}