package cn.edu.seu.alumni_server.controller;

import cn.edu.seu.alumni_server.common.Utils;
import cn.edu.seu.alumni_server.common.dto.WebResponse;
import cn.edu.seu.alumni_server.controller.dto.enums.EventType;
import cn.edu.seu.alumni_server.dao.entity.HistoryEvent;
import cn.edu.seu.alumni_server.dao.mapper.HistoryEventMapper;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/event")
public class UserEventTrackController {

    @Autowired
    HistoryEventMapper historyEventMapper;
    @Autowired
    HttpServletRequest request;

    @PostMapping("/view")
    public WebResponse viewAPost(@RequestParam Long postId,
                                 @RequestParam String eventType) {
        Long accountId = (Long) request.getAttribute("accountId");

        EnumUtils.isValidEnum(EventType.class, eventType);

        HistoryEvent historyEvent = new HistoryEvent();
        historyEvent.setAccountId(accountId);
        historyEvent.setEventId(Utils.generateId());
        historyEvent.setEventId(postId);
//        historyEvent.setEventType(eventType);

        historyEventMapper.insertSelective(historyEvent);
        return new WebResponse();
    }

}
