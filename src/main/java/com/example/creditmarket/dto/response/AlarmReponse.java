package com.example.creditmarket.dto.response;

import com.example.creditmarket.domain.entity.EntityAlarm;
import com.example.creditmarket.domain.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@AllArgsConstructor
public class AlarmReponse {
    private Long alarmId = null;

    private AlarmType alarmType;

    private HashMap<Long, Long> args;

    private LocalDateTime registeredAt;

    private LocalDateTime updatedAt;

    private LocalDateTime removedAt;

    public String getAlarmText() {
        return alarmType.getAlarmText();
    }

    public static AlarmReponse fromEntity(EntityAlarm entity) {
        return new AlarmReponse(
                entity.getAlarmId(),
                entity.getAlarmType(),
                entity.getArgs(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}
