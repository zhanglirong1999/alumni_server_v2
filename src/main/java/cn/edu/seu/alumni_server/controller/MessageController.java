package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.controller.dto.MessageDTO;
import cn.edu.seu.alumni_server.controller.dto.PageResult;
import cn.edu.seu.alumni_server.controller.dto.common.WebResponse;
import cn.edu.seu.alumni_server.dao.entity.Message;
import cn.edu.seu.alumni_server.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_server.dao.mapper.MessageMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息列表
 */
@RestController
@SuppressWarnings("ALL")
public class MessageController {

    @Autowired
    MessageMapper messageMapper;
    @Autowired
    AccountMapper accountMapper;

    @GetMapping("/message")
    public WebResponse readMessgae(@RequestParam Long accountId,
                                   @RequestParam(required = false) Integer status,
                                   @RequestParam Integer pageIndex,
                                   @RequestParam Integer pageSize) {
        if (status != 0 || status != 1 || status != 2) {
            return new WebResponse().fail("status只能为0，1，2");
        }
        PageHelper.startPage(pageIndex, pageSize);

        Message message = new Message();
        message.setToUser(accountId);
        message.setStatus(status);
        List<Message> temp = messageMapper.select(message);

        List<MessageDTO> res = temp
                .stream().map(m -> {
                    MessageDTO messageDTO = new MessageDTO(m);
                    messageDTO.setFromUserName(
                            accountMapper.selectByPrimaryKey(
                                    messageDTO.getFromUser()
                            ).getName()
                    );
                    return messageDTO;
                }).collect(Collectors.toList());
        return new WebResponse().success(new PageResult<MessageDTO>(
                ((Page) temp).getTotal(), res));
    }

    @PostMapping("/message/changeStatus")
    public WebResponse readMessgae(@RequestBody MessageDTO messageDTO) {
        Message message = new Message();
        message.setMessageId(messageDTO.getMessageId());
        message.setStatus(messageDTO.getStatus());
        messageMapper.updateByPrimaryKeySelective(message);
        return new WebResponse();
    }
}
