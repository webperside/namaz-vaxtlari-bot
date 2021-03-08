package com.webperside.namazvaxtlaribot.models;

import com.webperside.namazvaxtlaribot.enums.ActionLogStatus;
import com.webperside.namazvaxtlaribot.telegram.enums.TelegramCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "action_log")
@FieldNameConstants
public class ActionLog {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "action_log_id")
    private Integer id;

    @JoinColumn(name = "fk_user_id", referencedColumnName = "user_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(name = "command", nullable = false)
    private TelegramCommand command;

    @Column(name = "status", nullable = false, columnDefinition = "tinyint(4) default")
    private ActionLogStatus status;

    @Column(name = "message")
    private String message;

    @Column(name = "created_at", updatable = false, columnDefinition = "timestamp")
    private LocalDateTime createdAt;
}
