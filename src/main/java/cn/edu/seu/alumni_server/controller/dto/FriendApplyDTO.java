package cn.edu.seu.alumni_server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendApplyDTO {
    private Long A;
    private Long B;
    private String message;

}
