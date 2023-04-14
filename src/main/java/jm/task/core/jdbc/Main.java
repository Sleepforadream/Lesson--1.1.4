package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

import java.lang.reflect.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        UserServiceImpl userDaoJDBC = new UserServiceImpl();
        userDaoJDBC.createUsersTable();
        userDaoJDBC.saveUser("Maxasfhhhhhhhfsasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasasa", "Tsarev",(byte) 29);
        userDaoJDBC.saveUser("Vladislava", "Kolpakova",(byte) 22);
        userDaoJDBC.saveUser("Vladimir", "Dolgushin",(byte) 29);
        userDaoJDBC.saveUser("Umi", "Tsareva",(byte) 5);
        System.out.println(userDaoJDBC.getAllUsers());
        userDaoJDBC.removeUserById(2);
        userDaoJDBC.cleanUsersTable();
        userDaoJDBC.dropUsersTable();
    }
}