package com.example.creditmarket.domain.entity;

import com.example.creditmarket.domain.enums.AlarmType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name= "tb_alarm")
public class EntityAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_fk")
    private EntityUser user;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(type = "com.vladmihalcea.hibernate.type.json.JsonType")
    @Column(columnDefinition = "json")
    private HashMap<Long, Long> args;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    public static EntityAlarm of(AlarmType alarmType, HashMap<Long, Long> args, EntityUser user) {
        EntityAlarm entity = new EntityAlarm();
        entity.setAlarmType(alarmType);
        entity.setArgs(args);
        entity.setUser(user);
        return entity;
    }

    public void updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}