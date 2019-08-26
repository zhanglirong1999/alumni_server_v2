package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.CONST;
import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.token.Acl;
import cn.edu.seu.alumni_server.controller.dto.FriendDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.controller.dto.enums.FriendStatus;
import cn.edu.seu.alumni_server.controller.dto.enums.MessageType;
import cn.edu.seu.alumni_server.dao.entity.Friend;
import cn.edu.seu.alumni_server.dao.entity.Message;
import cn.edu.seu.alumni_server.dao.mapper.FriendMapper;
import cn.edu.seu.alumni_server.dao.mapper.MessageMapper;
import cn.edu.seu.alumni_server.dao.mapper.V2ApiMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * 好友逻辑
 * 申请、拒绝、查询等
 */
@RestController
@Acl
@SuppressWarnings("ALL")
public class FriendManageController {
    @Autowired
    FriendMapper friendMapper;

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    V2ApiMapper v2ApiMapper;

    @PostMapping("/friend/apply")
    public WebResponse friendApply(@RequestBody Map<String,Long> req) {
        Friend f = new Friend();
        f.setAccountId(req.get("A"));
        f.setFriendAccountId(req.get("B"));
        f.setStatus(FriendStatus.apply.getStatus());
        friendMapper.insertSelective(f);

        Message message = new Message();
        message.setMessageId(Utils.generateId());
        message.setFromUser(req.get("A"));
        message.setToUser(req.get("B"));
        message.setType(MessageType.APPLY.getValue());
        messageMapper.insertSelective(message);

        return new WebResponse();
    }

    @PostMapping("/friend/manage")
    public WebResponse friendAction(@RequestBody Map<String,Long> req) {

        if (req.get("action") == CONST.FRIEND_ACTION_Y) {
            Friend f = new Friend();
            f.setStatus(FriendStatus.friend.getStatus());

            Example e1 = new Example(Friend.class);
            e1.createCriteria()
                    .andEqualTo("friendAccountId", req.get("A"))
                    .andEqualTo("accountId", req.get("B"));
            friendMapper.updateByExampleSelective(f, e1);

            Message message = new Message();
            message.setMessageId(Utils.generateId());
            message.setFromUser(req.get("A"));
            message.setToUser(req.get("B"));
            message.setType(MessageType.AGREE.getValue());
            messageMapper.insertSelective(message);
        }

        if (req.get("action")  == CONST.FRIEND_ACTION_N) {
            Friend f = new Friend();
            f.setStatus(FriendStatus.stranger.getStatus());

            Example e1 = new Example(Friend.class);
            e1.createCriteria()
                    .andEqualTo("friendAccountId", req.get("A"))
                    .andEqualTo("accountId", req.get("B"));
            friendMapper.updateByExampleSelective(f, e1);

            Message message = new Message();
            message.setMessageId(Utils.generateId());
            message.setFromUser(req.get("A"));
            message.setToUser(req.get("B"));
            message.setType(MessageType.REJECT.getValue());
            messageMapper.insertSelective(message);
        }

        return new WebResponse();
    }

    @GetMapping("/friends")
    public WebResponse getFriends(@RequestParam Long accountId,
                                  @RequestParam int pageIndex,
                                  @RequestParam int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<FriendDTO> friends = v2ApiMapper.getFriends(accountId);

        return new WebResponse().success(new PageResult(((Page) friends).getTotal(), friends));
    }


}
