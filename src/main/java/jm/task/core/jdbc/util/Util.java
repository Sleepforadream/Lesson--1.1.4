package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydbtest?defaultAutoCommit=false";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "963375";

    public static final String CREATE_TABLE_USERS_SQL = "CREATE TABLE USERS (`id` int NOT NULL AUTO_INCREMENT," +
            "`name` varchar(45) NOT NULL,`last_name` varchar(45) NOT NULL," +
            " `age` int DEFAULT NULL, PRIMARY KEY (`id`))";

    public static final String CHECK_GET_USERS_SQL = "SELECT * FROM USERS";

    public static Connection getConnection() {
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        try {
            Configuration configuration = new Configuration().addAnnotatedClass(User.class);
            return configuration.buildSessionFactory();                                         //не закрываем?
        } catch (HibernateException e) {
            System.out.println("Ошибка создания поставщика сессии");
            throw e;
        }
    }
}