package demo.api.rest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import demo.core.NotificationSender;
import demo.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class TestController {

    @Autowired
    private NotificationSender sender;


    @Autowired
    private FirebaseMessaging firebaseMessaging;


    @PostMapping("/msg")
    public void testSend(@RequestBody NotificationDto dto){
        sender.send(dto.userId(),dto.subType(),dto.pType(), dto.direction(), dto.price(), dto.stockId());
    }
}
