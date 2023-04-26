package demo.api.rest;

import demo.dto.UserToken;
import demo.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @PostMapping("/tokens")
    public void updateToken(@RequestBody UserToken token){
        userTokenRepository.addUserToken(token.id(), token.token());
    }

    @PostMapping("/refresh_tokens")
    public void addToken(@RequestBody UserToken token){
        userTokenRepository.updateUserToken(token.id(), token.token());
    }


}
