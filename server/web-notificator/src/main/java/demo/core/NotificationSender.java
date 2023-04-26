package demo.core;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import demo.dto.Direction;
import demo.dto.PriceType;
import demo.dto.SubType;
import demo.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class NotificationSender {

    private final String NOTIF_TITLE = "Price alert!";
    private final int RETRY_COUNT = 5;

//    @Autowired
//    private ExecutorService executorService;
    @Autowired
    private FirebaseMessaging firebaseMessaging;
    @Autowired
    private UserTokenRepository utRepo;

    private String generateBody(PriceType priceType, Direction direction, double price, int stockId){
        String dir = direction.equals(Direction.UP) ? "risen" : "fallen";
        String template = "The "+ priceType.toString()+" price of #instrument_"+ stockId + " has "+dir+" up to " + price;
        return template;
    }

//    @Async
    public void send(int userId, SubType sType, PriceType pType, Direction direction, double price, int stockId) {

        int retries = RETRY_COUNT;
        boolean isSent = false;

        while (retries > 0 && !isSent){
            try{
                String userToken = utRepo.getToken(userId);
                Notification notification = Notification.builder().setTitle(sType.toString() +" "+NOTIF_TITLE).setBody(generateBody(pType, direction, price, stockId)).build();
                Message msg = Message.builder()
                        .setNotification(notification)
                        .setToken(userToken)
                        .build();
                firebaseMessaging.send(msg);
                isSent = true;

            } catch (FirebaseMessagingException exception){
                System.out.println("Notification not send: " + userId);
                retries--;
                if(retries < 0){
                    throw new RuntimeException();
                } else {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

    }
}
