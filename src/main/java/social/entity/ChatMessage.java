package social.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageId")
    Integer messageId;

    @OneToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "username")
    User sender;

    @Column(name = "content")
    String content;

    @JoinColumn(name = "chatId", nullable = false)
    @ManyToOne
    Chat chat;

    @Temporal(TemporalType.TIMESTAMP)
    Date sendTime;
}
