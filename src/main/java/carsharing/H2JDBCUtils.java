package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class H2JDBCUtils {

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/main/db/";

    private static String dbName = "Test";

    public static void setName(String[] args) {
        if (args.length != 0 && Optional.of(args[0]).get().equals("-databaseFileName")) {
            dbName = args[1];
        }
    }

    public static void createDb(){
        String drop = "DROP TABLE IF EXISTS COMPANY";
        String drop2 = "DROP TABLE IF EXISTS CAR";
        String drop3 = "DROP TABLE IF EXISTS CUSTOMER";

        String sqlCompany = "CREATE TABLE IF NOT EXISTS COMPANY " +
                     "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                     "NAME VARCHAR(255) UNIQUE NOT NULL )";

        String sqlCar = "CREATE TABLE IF NOT EXISTS CAR " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                "COMPANY_ID INTEGER NOT NULL, " +
                "CONSTRAINT fk_company " +
                "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID))";

        String sqlCustomer = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                "RENTED_CAR_ID INTEGER DEFAULT NULL, " +
                "CONSTRAINT fk_car " +
                "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR (ID))";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
//            stmt.executeUpdate(drop3);
//            stmt.executeUpdate(drop2);
//            stmt.executeUpdate(drop);
            stmt.executeUpdate(sqlCompany);
            stmt.executeUpdate(sqlCar);
            stmt.executeUpdate(sqlCustomer);
        } catch (SQLException e) {
            System.out.println("SQL while creating");
        }
    }
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL + dbName);
            connection.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return connection;
    }
}
