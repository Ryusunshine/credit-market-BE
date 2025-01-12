package com.example.creditmarket.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmArgs {
    // user who occur alarm
    private Long fromUserId;
    private Long targetId;
}
