package social.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import social.entity.Chat;
import social.entity.User;
import social.service.ChatService;
import social.service.UserService;

import java.security.Principal;

@Component
public class MessageInterceptor implements ChannelInterceptor {

    @Autowired
    UserService userService;

    @Autowired
    ChatService chatService;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())
                || StompCommand.MESSAGE.equals(headerAccessor.getCommand())) {
            Principal userPrincipal = headerAccessor.getUser();
            if (!validateSubscription(userPrincipal, headerAccessor.getDestination())) {
                throw new IllegalArgumentException("No permission for this topic");
            }
        }
        return message;
    }

    private boolean validateSubscription(Principal principal, String topicDestination) {
        System.out.println("topic here |||");
        System.out.println(topicDestination);
        if (principal == null) {
            return false;
        }

        User user = userService.findUserByUsername(principal.getName());
        Chat chat = chatService.findChatByChatId(Integer.parseInt(topicDestination.replace("/topic/", "")));

        if (chat == null) return false;

        return (chat.getParticipants().stream()
                .map(User::getUsername)
                .anyMatch(username -> username.equals(user.getUsername())));
    }
}