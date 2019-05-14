package social.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import social.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    Chat findByChatId(Integer chatId);
    Chat findOneByTrackListId(String trackListId);
}