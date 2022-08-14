package carsharing.DAO;

import carsharing.Models.Company;
import carsharing.H2JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyDaoImpl implements AbstractDao<Company> {

    private final List<Company> companies;
    static private final Connection connection = H2JDBCUtils.getConnection();

    private static final String SELECT_QUERY = "SELECT * FROM COMPANY";
    private static final String BY_NAME = " WHERE NAME =?";
    private static final String BY_ID = " WHERE ID =?";
    private static final String INSERT_COMPANIES_SQL = "INSERT INTO COMPANY" +
                                                    " (NAME) VALUES" +
                                                    " (?);";

    public CompanyDaoImpl() {
        this.companies = new ArrayList<>();
    }

    public Optional<Company> getById(long id) {
        Optional<Company> company = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_QUERY + BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int receivedId = rs.getInt("id");
                String receivedName = rs.getString("name");
                company = Optional.of(new Company(receivedId, receivedName));
            }

        } catch (Exception e) {
            System.out.println("Err while getting");
        }
        return company;
    }
    @Override
    public Optional<Company> getByName(String name) {
        Optional<Company> company = Optional.empty();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_QUERY + BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String receivedName = rs.getString("name");
                company = Optional.of(new Company(id, receivedName));
            }

        } catch (Exception e) {
            System.out.println("Err while getting");
        }
        return company;
    }

    @Override
    public List<Company> getAll() {
        try {
            ResultSet rs = connection.createStatement().executeQuery(SELECT_QUERY);
            companies.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                String receivedName = rs.getString("name");
                companies.add(new Company(id, receivedName));
            }
        } catch (Exception e) {
            System.out.println("Err while getting All");
        }
        return companies;
    }

    @Override
    public Company create(Company entity) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_COMPANIES_SQL)) {
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
    public boolean update(Company entity) {
        return false;
    }
}

