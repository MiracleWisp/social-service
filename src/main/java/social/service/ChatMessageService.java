package social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import social.entity.ChatMessage;
import social.repository.ChatMessageRepository;

import java.util.Date;

@Service("chatMessageService")
public class ChatMessageService {
    @Autowired
    ChatMessageRepository chatMessageRepository;

    public void saveChatMessage(ChatMessage chatMessage){
        chatMessage.setSendTime(new Date());
        chatMessageRepository.save(chatMessage);
    }
}
