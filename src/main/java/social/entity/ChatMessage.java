package social.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "messages")
public class ChatMessage {

    //На будущее
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
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
