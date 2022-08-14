package carsharing.DAO;

import carsharing.H2JDBCUtils;
import carsharing.Models.Car;
import carsharing.Models.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarDaoImpl implements AbstractDao<Car> {
    static private final Connection connection = H2JDBCUtils.getConnection();

    private static final String SELECT_QUERY = "SELECT * FROM CAR";
    private static final String BY_COMPANY = " WHERE COMPANY_ID =?";
    private static final String BY_NAME= " WHERE NAME =?";
    private static final String BY_ID= " WHERE ID =?";
    private static final String INSERT_CARS_SQL = "INSERT INTO CAR" +
            " (NAME, COMPANY_ID) VALUES" +
            " (?, ?);";
    private static final String SELECT_AVAILABLE_CARS =
            "SELECT CAR.ID, CAR.NAME, CAR.COMPANY_ID" +
                    " FROM CAR LEFT JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID" +
                    " WHERE COMPANY_ID = ? AND CUSTOMER.ID IS NULL";

    public Optional<Car> getById(long id) {
        Optional<Car> car = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_QUERY + BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int receivedId = rs.getInt("id");
                String receivedName = rs.getString("name");
                int company_id = rs.getInt("company_id");
                car = Optional.of(new Car(receivedId, receivedName, company_id));
            }

        } catch (Exception e) {
            System.out.println("Err while getting");
        }
        return car;
    }

    @Override
    public Optional<Car> getByName(String name) {
        Optional<Car> car = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_QUERY + BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String receivedName = rs.getString("name");
                int company_id = rs.getInt("company_id");
                car = Optional.of(new Car(id, receivedName, company_id));
            }

        } catch (Exception e) {
            System.out.println("Err while getting");
        }
        return car;
    }

    public List<Car> getAllByCompany(Company company) {
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_QUERY + BY_COMPANY)) {
            ps.setInt(1, company.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int company_id = rs.getInt("company_id");
                cars.add(new Car(id, name, company_id));
            }
        } catch (Exception e) {
            System.out.println("Err while getting All");
        }
        return cars;
    }

    @Override
    public Car create(Car entity) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_CARS_SQL)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getCompanyId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Err while creating");
        }
        return getByName(entity.getName()).orElse(null);
    }

    @Override
    public List<Car> getAll() {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public boolean update(Car entity) {
        return false;
    }

    public List<Car> getAllAvailable(Company company) {
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_AVAILABLE_CARS)) {
            ps.setInt(1, company.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int company_id = rs.getInt("company_id");
                cars.add(new Car(id, name, company_id));
            }
        } catch (Exception e) {
            System.out.println("Err while getting All");
        }
        return cars;
    }
}
