package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {

        UserServiceImpl userDaoJDBC = new UserServiceImpl();
//        userDaoJDBC.createUsersTable();
//        userDaoJDBC.saveUser("Max", "Tsarev",(byte) 29);
//        userDaoJDBC.saveUser("Vladislava", "Kolpakova",(byte) 22);
//        userDaoJDBC.saveUser("Vladimir", "Dolgushin",(byte) 29);
//        userDaoJDBC.saveUser("Umi", "Tsareva",(byte) 5);
        System.out.println(userDaoJDBC.getAllUsers());
//        userDaoJDBC.removeUserById(200);
//        userDaoJDBC.cleanUsersTable();
//        userDaoJDBC.dropUsersTable();
    }
}