package by.academy.final_project.util;

import by.academy.final_project.exceptions.DriverNotLoaded;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private ConnectionManager() {
    }

    public static final String URL = "db.url";
    public static final String LOGIN = "db.login";
    public static final String PASSWORD = "db.password";

    static {
        try {
            loadDriver();
        } catch (DriverNotLoaded e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection open() throws DriverNotLoaded {
        try {
            return DriverManager.getConnection(PropertyUtil.get(URL), PropertyUtil.get(LOGIN), PropertyUtil.get(PASSWORD));
        } catch (SQLException e) {
            throw new DriverNotLoaded("Драйвер не загружен");
        }
    }

    private static void loadDriver() throws DriverNotLoaded {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DriverNotLoaded("Драйвер не загружен");
        }
    }

}
