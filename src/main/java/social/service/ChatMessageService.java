package social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import social.entity.Chat;
import social.entity.ChatMessage;
import social.repository.ChatMessageRepository;

import java.util.Date;
import java.util.List;

@Service("chatMessageService")
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveChatMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getPreviousMessages(Date since, Chat chat) {
        return chatMessageRepository.findTop10BySendTimeBeforeAndChatOrderBySendTimeDesc(since, chat);
    }
}
