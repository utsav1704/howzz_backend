package com.chatapk.chatapplication.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document
public class ChatMessage {

        @Id
        private String id;
        private String chatId;
        private String senderId;
        private String recipientId;
        private String senderName;
        private String recipientName;
        private String content;
        private Date timestamp;
        private MessageStatus messageStatus;
    
}
