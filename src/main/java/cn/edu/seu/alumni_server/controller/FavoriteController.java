package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.controller.dto.MessageDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.Favorite;
import cn.edu.seu.alumni_server.dao.mapper.FavoriteMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * 名片收藏逻辑
 */
@RestController
@SuppressWarnings("ALL")
public class FavoriteController {

    @Autowired
    FavoriteMapper favoriteMapper;

    @GetMapping("/favorite")
    WebResponse getFavorite(@RequestParam Long accountId,
                            @RequestParam int pageIndex,
                            @RequestParam int pageSize) {
        Favorite favorite = new Favorite();
        favorite.setAccountId(accountId);
        favorite.setStatus(1);
        PageHelper.startPage(pageIndex, pageSize);
        List<Favorite> res = favoriteMapper.select(favorite);

        return new WebResponse().success(
                new PageResult<Favorite>(((Page) res).getTotal(), res));
    }

    @PostMapping("/favorite")
    WebResponse changeFavoriteStatus(@RequestBody Map req) {
        Long accountId = (Long) req.get("accountId");
        Long favoriteAccountId = (Long) req.get("favoriteAccountId");
        Integer status = (Integer) req.get("status");

        if(status!=0||status!=1){
            return new WebResponse().fail("status只能为0或1");
        }
        Favorite favorite = new Favorite();
        favorite.setAccountId(accountId);
        favorite.setFavoriteAccountId(favoriteAccountId);
        favorite.setStatus(status);
        if (favoriteMapper.select(favorite).size() == 0) {
            favoriteMapper.insertSelective(favorite);
        } else {
            Example example = new Example(Favorite.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("accountId", accountId);
            criteria.andEqualTo("favoriteAccountId", favoriteAccountId);

            favoriteMapper.updateByExampleSelective(favorite, example);
        }
        return new WebResponse();
    }
}
