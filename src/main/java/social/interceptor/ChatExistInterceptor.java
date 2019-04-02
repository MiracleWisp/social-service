package social.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import social.entity.Chat;
import social.service.ChatService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class ChatExistInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private ChatService chatService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Map<String, String> pathVariables = (Map<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer chatId = Integer.parseInt(pathVariables.get("chatId"));
        System.out.println(chatId);
        Chat chat = chatService.findChatByChatId(chatId);
        if (chat == null) {
            response.setStatus(404);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{ successful: false, data: \"Chat not found\"}");
            return false;
        }
        return true;
    }
}
