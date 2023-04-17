package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.CREATE_TABLE_USERS_SQL;
import static jm.task.core.jdbc.util.Util.CHECK_GET_USERS_SQL;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(CHECK_GET_USERS_SQL);
             PreparedStatement createStatement = connection.prepareStatement(CREATE_TABLE_USERS_SQL)) {
            try {
                checkStatement.executeQuery();
                System.out.println("Такая таблица уже есть в бд");
            } catch (SQLException e) {
                createStatement.execute();
                System.out.println("Таблица 'users' создана");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка создания таблицы 'users'!");
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String sqlDrop = "DROP TABLE IF EXISTS USERS";
        try (Connection connection = Util.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(CHECK_GET_USERS_SQL);
             PreparedStatement dropStatement = connection.prepareStatement(sqlDrop)) {
            try {
                checkStatement.executeQuery();
                dropStatement.execute();
                System.out.println("Таблица удалена");
            } catch (SQLException e) {
                System.err.println("Таблица 'USERS' не найдена");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка удаления таблицы 'users'!");
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sqlEdit = "INSERT INTO USERS (NAME, LAST_NAME, AGE) VALUES (?, ? ,?)";
        try (Connection connection = Util.getConnection()) {
            try (PreparedStatement editStatement = connection.prepareStatement(sqlEdit)) {
                editStatement.setString(1, name);
                editStatement.setString(2, lastName);
                editStatement.setByte(3, age);
                editStatement.executeUpdate();
                connection.commit();
                System.out.println("Пользователь с именем - " + name + " добавлен в базу данных");
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Пользователь с именем - " + name + " не был добавлен в базу данных");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка соединения");
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String sqlDelete = "DELETE FROM USERS WHERE ID=" + id;
        String sqlName = "SELECT NAME FROM USERS WHERE ID=" + id;
        try (Connection connection = Util.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(sqlDelete);
             PreparedStatement selectStatement = connection.prepareStatement(sqlName);
             ResultSet resultSet = selectStatement.executeQuery()) {
            if (resultSet.next()) {
                String nameUser = resultSet.getString("NAME");
                deleteStatement.executeUpdate();
                connection.commit();
                System.out.println("Пользователь " + nameUser + " был удалён из базы данных");
            } else {
                connection.rollback();
                System.err.println("Пользователя с таким ID нет в базе данных");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении пользователя!");
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement selectAllStatement = connection.createStatement();
             ResultSet resultSet = selectAllStatement.executeQuery(CHECK_GET_USERS_SQL)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LAST_NAME"));
                user.setAge(resultSet.getByte("AGE"));
                userList.add(user);
            }
            if (userList.isEmpty()) {
                System.err.println("Список пользователей пуст");
                return userList;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения списка пользователей");
            e.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
        String sqlClean = "DELETE FROM USERS";
        try (Connection connection = Util.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(CHECK_GET_USERS_SQL);
             PreparedStatement cleanStatement = connection.prepareStatement(sqlClean);
             ResultSet resultSet = checkStatement.executeQuery()) {
            resultSet.next();
            if (Integer.parseInt(resultSet.getString(1)) > 0) {
                cleanStatement.execute();
                connection.commit();
                System.out.println("Таблица очищена");
            } else {
                connection.rollback();
                System.err.println("Таблица уже пустая");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}