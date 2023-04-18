package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jm.task.core.jdbc.dao.UserDaoHibernateImpl.ObjectEntity.TABLE;
import static jm.task.core.jdbc.dao.UserDaoHibernateImpl.ObjectEntity.USER;
import static jm.task.core.jdbc.util.Util.CREATE_TABLE_USERS_SQL;
import static jm.task.core.jdbc.util.Util.CHECK_GET_USERS_SQL;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        doTransaction(session -> {
            try {
                session.createNativeQuery(CHECK_GET_USERS_SQL, User.class);
                throw new RuntimeException("Такая таблица уже существует");
            } catch (Exception exception) {
                session.createNativeQuery(CREATE_TABLE_USERS_SQL, User.class).executeUpdate();
                return TABLE.name;
            }
        }, TABLE, " была создана");
    }

    @Override
    public void dropUsersTable() {
        doTransaction(session -> {
            try {
                session.createNativeQuery("DROP TABLE USERS", User.class).executeUpdate();
                session.createNativeQuery(CHECK_GET_USERS_SQL, User.class);
                return TABLE.name;
            } catch (Exception exception) {
                throw new RuntimeException("Такая таблица не существует");
            }
        }, TABLE, " была удалена");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        doTransaction(session -> {
            User user = new User(name, lastName, age);
            session.persist(user);
            return user.getName();
        }, USER, " был добавлен в базу данных");
    }

    @Override
    public void removeUserById(long id) {
        doTransaction(session -> {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new RuntimeException("Такого пользователя не существует");
            }
            session.delete(user);
            return user.getName();
        }, USER, " был удалён из базы данных");
    }

    @Override
    public List<User> getAllUsers() {
        AtomicReference<List<User>> allUsers = new AtomicReference<>();
        doTransaction(session -> {
            allUsers.set(session.createNativeQuery(CHECK_GET_USERS_SQL, User.class).getResultList());
            if (allUsers.get().isEmpty()) throw new RuntimeException("Список пользователей пуст");
            else return TABLE.name;
        }, TABLE, " содержит следующие строки пользователей");
        return allUsers.get();
    }

    @Override
    public void cleanUsersTable() {
        doTransaction(session -> {
            if (session.createNativeQuery(CHECK_GET_USERS_SQL, User.class).getResultList().isEmpty())
                throw new RuntimeException("Таблица уже пустая");
            else session.createNativeQuery("TRUNCATE users", User.class).executeUpdate();
            return TABLE.name;
        }, TABLE, " была очищена");
    }

    private void doTransaction(Function<Session, String> command, ObjectEntity objectEntity, String successMessage) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                String name = command.apply(session);
                transaction.commit();
                System.out.println(objectEntity.title + name + successMessage);
            } catch (Exception exception) {
                transaction.rollback();
                successMessage = (objectEntity.title + successMessage).replace(" с именем - ", "");
                System.out.println(successMessage.replaceAll("(?i)\\sбы(л[аио])?\\s"," не бы$1 "));
                exception.printStackTrace();
            }
        } catch (HibernateException exception) {
            System.err.println("Ошибка создания сессии");
            exception.printStackTrace();
        }
    }

    enum ObjectEntity {
        USER("Пользователь с именем - ", null),
        TABLE("Таблица с именем - ", "Users");
        private final String title;
        private final String name;

        ObjectEntity(String title, String name) {
            this.title = title;
            this.name = name;
        }
    }
}