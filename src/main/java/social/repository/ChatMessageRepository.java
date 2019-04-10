package social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import social.entity.Chat;
import social.entity.ChatMessage;

import java.util.Date;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    List<ChatMessage> findTop10BySendTimeBeforeAndChatOrderBySendTimeDesc(Date since, Chat chat);
}
