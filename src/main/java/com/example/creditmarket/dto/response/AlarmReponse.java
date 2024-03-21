package com.example.creditmarket.dto.response;

import com.example.creditmarket.domain.entity.EntityAlarm;
import com.example.creditmarket.domain.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmReponse {
    private Long alarmId;

    private AlarmType alarmType;

    private HashMap<Long, Long> args;

    private String alarmText;

    public static AlarmReponse fromEntity(EntityAlarm entity) {
        return AlarmReponse.builder()
                .alarmId(entity.getAlarmId())
                .alarmType(entity.getAlarmType())
                .args(entity.getArgs())
                .alarmText(entity.getAlarmType().getAlarmText())
                .build();
    }
}
