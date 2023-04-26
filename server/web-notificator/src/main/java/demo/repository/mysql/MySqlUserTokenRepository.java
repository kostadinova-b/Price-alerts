package demo.repository.mysql;

import demo.repository.UserTokenRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class MySqlUserTokenRepository implements UserTokenRepository {

    private JdbcTemplate jdbc;
    private TransactionTemplate tcTemplate;

    public MySqlUserTokenRepository(JdbcTemplate jdbcTemplate, TransactionTemplate tcTemplate) {
        this.jdbc = jdbcTemplate;
        this.tcTemplate = tcTemplate;
    }

    @Override
    public void addUserToken(int id, String token) {
        tcTemplate.execute(status -> {
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(Queries.CREATE_TOKEN, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, id);
                ps.setString(2, token);
                return ps;
            });
            return null;
        });
    }

    @Override
    public void updateUserToken(int id, String token) {
        tcTemplate.execute(status -> {
            jdbc.update(Queries.UPDATE_TOKEN, token, id);
            return  null;
        });
    }

    @Override
    public String getToken(int id) {
        return jdbc.queryForObject(Queries.GET_TOKEN, (rs, rn) -> {return  rs.getString("fcm_token");}, id);
    }

    static class Queries{
        public static final String UPDATE_TOKEN = "UPDATE users SET fcm_token = ? WHERE id = ?";
        public static final String CREATE_TOKEN = "INSERT INTO users(id, fcm_token) VALUES (?,?)";
        public static final String GET_TOKEN = "SELECT fcm_token FROM users WHERE id = ?";
    }
}
