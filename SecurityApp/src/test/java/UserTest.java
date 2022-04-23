import model.User;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
//    private final String URI = "jdbc:postgresql:";      // need update
//
//    @Nested
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    class UserORMLiteDaoTest {
//
//        private ConnectionSource connectionSource;
//        private Dao<User, Integer> dao;
//
//        // create a new connection to database, create "incidents" table, and create a
//        // new dao to be used by test cases
//        @BeforeAll
//        public void setUpAll() throws SQLException {
//            connectionSource = new JdbcConnectionSource(URI);
//            TableUtils.createTableIfNotExists(connectionSource, User.class);
//            dao = DaoManager.createDao(connectionSource, User.class);
//        }
//
//        // delete all rows in the incidents table before each test case
//        @BeforeEach
//        public void setUpEach() throws SQLException {
//            TableUtils.clearTable(connectionSource, User.class);
//        }
//
//        @Test
//        public void Test1() throws SQLException{
//
//        }
//    }
//
//    @Nested
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    class InstanceAPITest {
//        final String BASE_URL = "";      // endpoint
//        private OkHttpClient client;
//
//        @BeforeAll
//        public void setUpAll() {
//            client = new OkHttpClient();
//        }
//
//        @Test
//        public void testHTTPGetIncidentsEndPoint() throws IOException {
//
//            client = new OkHttpClient();
//
//            Request request = new Request.Builder()
//                    .url(BASE_URL + "//users")
//                    .build();
//            Response response = client.newCall(request).execute();
//            assertEquals(response.code(),200);
//        }
//    }
}
