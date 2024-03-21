package com.example.creditmarket.kafka.producer;

import com.example.creditmarket.domain.enums.AlarmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmProducer {

    private final KafkaTemplate<String, AlarmEvent> alarmEventKafkaTemplate;

    public void send(AlarmEvent event) {
        alarmEventKafkaTemplate.send("alarm", String.valueOf(event.getReceiverUserId()), event);
        log.info("send fin");
    }
}
