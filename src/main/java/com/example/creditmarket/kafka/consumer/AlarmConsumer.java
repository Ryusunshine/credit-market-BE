package com.example.creditmarket.kafka.consumer;

import com.example.creditmarket.domain.enums.AlarmEvent;
import com.example.creditmarket.service.Impl.AlarmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmConsumer {

    private final AlarmService alarmService;
    private final ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper

    @KafkaListener(topics = "alarm")
    public void consumeNotification(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            AlarmEvent event = objectMapper.readValue(record.value(), AlarmEvent.class); // 메시지를 AlarmEvent 객체로 변환
            log.info("Consume the event {}", event);
            alarmService.send(event.getType(), event.getArgs(), event.getReceiverUserId());
            ack.acknowledge(); // 메시지 처리 후 수동으로 ack
        } catch (IOException e) {
            log.error("Error deserializing message", e);
        }
    }
}

