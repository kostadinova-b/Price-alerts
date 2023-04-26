import com.example.pricealertssubscrption.dto.PriceType;
import com.example.pricealertssubscrption.repository.CustomSubRepository;
import com.example.pricealertssubscrption.repository.StockRepository;
import com.example.pricealertssubscrption.repository.ThresholdSubRepository;
import com.example.pricealertssubscrption.repository.entity.CustomSubscription;
import com.example.pricealertssubscrption.repository.entity.Stock;
import com.example.pricealertssubscrption.repository.entity.ThresholdSubscription;
import com.example.pricealertssubscrption.repository.sql.MySQLCustomSubRepository;
import com.example.pricealertssubscrption.repository.sql.MySQLStockRepository;
import com.example.pricealertssubscrption.repository.sql.MySQLThresholdSubRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTest {
    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/price-alerts-subs","root","123");

        DataSource dataSource = new SingleConnectionDataSource(conn, false);
        JdbcTemplate jdbcTemplate =
                new JdbcTemplate(dataSource);
        DataSourceTransactionManager transactionManager =
                new DataSourceTransactionManager(dataSource);
        TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);

        StockRepository st = new MySQLStockRepository(txTemplate, jdbcTemplate);
        Stock s = st.createStock(10.10, 12.12);
        Stock s2 = st.createStock(11.11, 12.12);
        //Stock s = st.getStock(1);
      //  System.out.println(s.id + " " + s.buy + " " + s.sell);
//        st.updateStock(new Stock(s.id, 2.0, 3.5));
//        Stock s1 = st.getStock(s.id);
       // System.out.println(s1.id + " " + s1.buy + " " + s1.sell);

        ThresholdSubRepository tr = new MySQLThresholdSubRepository(txTemplate, jdbcTemplate);
        ThresholdSubscription t = tr.createSubscription(1, 9, PriceType.BUY, 10.12, 12);
    //    System.out.println(t.id);

        CustomSubRepository cr = new MySQLCustomSubRepository(txTemplate, jdbcTemplate);
        CustomSubscription c = cr.createSubscription(1, 9, PriceType.BUY, 10.12);
        System.out.println(c.id + " " + c.userId + " " + c.stockId + " " + c.price + " " + c.type.toString());
       // cr.deleteSubscription(c.id);


    }
}
