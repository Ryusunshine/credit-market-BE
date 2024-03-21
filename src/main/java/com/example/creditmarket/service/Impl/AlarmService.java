package com.example.creditmarket.service.Impl;

import com.example.creditmarket.common.AppException;
import com.example.creditmarket.common.ErrorCode;
import com.example.creditmarket.domain.entity.EntityAlarm;
import com.example.creditmarket.domain.entity.EntityUser;
import com.example.creditmarket.domain.enums.AlarmArgs;
import com.example.creditmarket.domain.enums.AlarmNoti;
import com.example.creditmarket.domain.enums.AlarmType;
import com.example.creditmarket.domain.repository.AlarmRepository;
import com.example.creditmarket.domain.repository.EmitterRepository;
import com.example.creditmarket.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlarmService {
    private final static String ALARM_NAME = "alarm";

    private final AlarmRepository alarmRepository;
    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    @Transactional
    public void send(AlarmType type, AlarmArgs args, Long receiverId) {
        EntityUser user = userRepository.findByUserId(receiverId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND, "회원이 존재하지 않습니다."));
        HashMap<Long, Long> newArgs = new HashMap<>();
        newArgs.put(args.getFromUserId(), args.getTargetId());
        EntityAlarm alarm = EntityAlarm.of(type, newArgs, user);

        try {
            alarmRepository.save(alarm);
        } catch (Exception e){
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "일시적 오류입니다");
        }

        emitterRepository.get(receiverId).ifPresentOrElse(it -> { //emitterRepo에서 인스턴스를 가지고 온다.
                    try {
                        it.send(SseEmitter.event() // 새로운 알람 전송
                                .id(alarm.getAlarmId().toString())
                                .name(ALARM_NAME)
                                .data(new AlarmNoti()));

                    } catch (IOException exception) {
                        emitterRepository.delete(receiverId);
                        throw new AppException(ErrorCode.NOTIFICATION_CONNECT_ERROR, "알림 전송에 실패했습니다.");
                    }
                },
                () -> log.info("No emitter founded")
        );
    }

    @Transactional
    public SseEmitter connectNotification(Long userId) {
        log.info("{userId = }", userId);
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);
        emitter.onCompletion(() -> emitterRepository.delete(userId));
        emitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            log.info("send");
            emitter.send(SseEmitter.event()
                    .id("id")
                    .name(ALARM_NAME)
                    .data("connect completed"));
        } catch (IOException exception) {
            throw new AppException(ErrorCode.NOTIFICATION_CONNECT_ERROR, "통신에 실패했습니다.");
        }
        return emitter;
    }


}
