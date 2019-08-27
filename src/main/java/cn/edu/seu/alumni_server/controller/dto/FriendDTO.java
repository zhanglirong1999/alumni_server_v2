package cn.edu.seu.alumni_server.controller.dto;

import cn.edu.seu.alumni_server.dao.entity.Friend;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class FriendDTO {
    private Long friendAccountId;
    private String name;
    private String company;
    private String position;
    private String avatar;
    private String city;
    private Long status;

    public FriendDTO() {
    }

    public FriendDTO(Friend friend) {
        BeanUtils.copyProperties(friend, this);
    }

    public Friend toFriend() {
        Friend friend = new Friend();
        BeanUtils.copyProperties(this, friend);
        return friend;
    }
}
