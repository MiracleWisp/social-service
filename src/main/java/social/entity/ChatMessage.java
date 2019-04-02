package social.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "messages")
public class ChatMessage {

    //На будущее
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public ChatMessage(User sender, String content, Chat chat, MessageType type){
        this.sender = sender;
        this.content = content;
        this.chat = chat;
        this.type = type;
        this.sendTime = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageId")
    private Integer messageId;

    @OneToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "username")
    private User sender;

    @Column(name = "content")
    private String content;

    @JoinColumn(name = "chatId", nullable = false)
    @ManyToOne
    private Chat chat;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sendTime;

    @Enumerated(EnumType.STRING)
    private MessageType type;
}
