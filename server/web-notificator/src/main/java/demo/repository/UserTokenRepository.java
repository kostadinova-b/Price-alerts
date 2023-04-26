package demo.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository {
    void addUserToken(int id, String token);
    void updateUserToken(int id, String token);
    String getToken(int id);

}
