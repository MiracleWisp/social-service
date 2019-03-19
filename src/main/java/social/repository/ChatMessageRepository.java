package social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import social.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
}
