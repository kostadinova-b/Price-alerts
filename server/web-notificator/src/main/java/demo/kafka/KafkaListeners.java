package demo.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.core.NotificationSender;
import demo.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KafkaListeners {
    @Autowired
    private ObjectMapper json;
    @Autowired
    private NotificationSender sender;

    public Set<NotificationDto> notified = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @KafkaListener(topics = KafkaConfig.TOPIC_NOTIFICATION, groupId = KafkaConfig.GROUP_ID)
    public void onNotificationEvent(String data) throws JsonProcessingException {
        System.out.println("received notif");
        NotificationDto dto = json.readValue(data, NotificationDto.class);
        if(!notified.contains(dto)) {
            try{
                sender.send(dto.userId(), dto.subType(), dto.pType(), dto.direction(), dto.price(), dto.stockId());
                notified.add(dto);
            }
            catch (Exception e){

            }

        }
    }
}
