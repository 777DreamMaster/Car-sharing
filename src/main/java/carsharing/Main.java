package carsharing;

public class Main {
    public static void main(String[] args) {
        H2JDBCUtils.setName(args);

        H2JDBCUtils.createDb();
        Menu.process();
    }
}
