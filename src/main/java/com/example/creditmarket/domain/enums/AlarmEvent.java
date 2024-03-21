package com.example.creditmarket.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEvent {
    private AlarmType type;
    private AlarmArgs args;
    private Long receiverUserId;
}
