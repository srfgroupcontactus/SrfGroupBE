package com.takirahal.srfgroup.modules.chat.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.takirahal.srfgroup.modules.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sg_message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "content")
    private String content;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "is_read")
    private Boolean isRead;

    @ManyToOne
    private User senderUser;

    @ManyToOne
    private User receiverUser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "senderUser", "receiverUser" }, allowSetters = true)
    private Conversation conversation;
}
