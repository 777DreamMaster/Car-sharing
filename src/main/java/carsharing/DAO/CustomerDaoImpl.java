package carsharing.DAO;

import carsharing.H2JDBCUtils;
import carsharing.Models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl implements AbstractDao<Customer> {

    static private final Connection connection = H2JDBCUtils.getConnection();

    private static final String SELECT_QUERY = "SELECT * FROM CUSTOMER";
    private static final String BY_CAR = " WHERE RENTED_CAR_ID =?";
    private static final String BY_NAME= " WHERE NAME =?";
    private static final String INSERT_CUSTOMERS_SQL = "INSERT INTO CUSTOMER" +
            " (NAME) VALUES" +
            " (?);";

    private static final String UPDATE_USERS_SQL =
            "UPDATE CUSTOMER SET NAME = ?, RENTED_CAR_ID = ? WHERE ID = ?;";

    @Override
    public Optional<Customer> getByName(String name) {
        Optional<Customer> customer = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_QUERY + BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String receivedName = rs.getString("name");
                int car_id = rs.getInt("rented_car_id");
                customer = Optional.of(new Customer(id, receivedName, car_id));
            }
        } catch (Exception e) {
            System.out.println("Err while getting");
        }
        return customer;
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        try {
            ResultSet rs = connection.createStatement().executeQuery(SELECT_QUERY);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int car_id = rs.getInt("rented_car_id");
                customers.add(new Customer(id, name, car_id));
            }
        } catch (Exception e) {
            System.out.println("Err while getting All");
        }
        return customers;
    }

    @Override
    public Customer create(Customer entity) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_CUSTOMERS_SQL)) {
            ps.setString(1, entity.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Err while creating");
        }
        return getByName(entity.getName()).orElse(null);
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public boolean update(Customer entity) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_USERS_SQL)) {
            ps.setString(1, entity.getName());
            if (entity.getRentedCarId() == 0) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, entity.getRentedCarId());
            }
            ps.setInt(3, entity.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Err while creating");
            return false;
        }
        return true;
    }
}
