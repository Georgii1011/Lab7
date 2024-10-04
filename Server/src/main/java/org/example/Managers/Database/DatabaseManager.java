package org.example.Managers.Database;

import org.example.CollectionModel.HumanBeing;
import org.example.CollectionModel.Parametres.Car;
import org.example.CollectionModel.Parametres.Coordinates;
import org.example.CollectionModel.Parametres.Mood;
import org.example.CollectionModel.Parametres.WeaponType;
import org.example.Connection.User;
import org.example.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.basic.BasicBorders;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmopqrstuvwxyz0123456789-=!@#$%^&*()_+{}[]:";
    private static final String PEPPER = "(2g%#o3<@";

    private Connection connection;
    private MessageDigest messageDigest;
    static final Logger databaseManagerLogger = LoggerFactory.getLogger(DatabaseManager.class);

    public DatabaseManager() {
        try {
            messageDigest = MessageDigest.getInstance(Main.HASHING);
            connect();
            createDatabaseTables();
        } catch (SQLException e) {
            databaseManagerLogger.info("Ошибка запроса или таблицы уже созданы");
            databaseManagerLogger.debug(e.toString());
        } catch (NoSuchAlgorithmException e) {
            databaseManagerLogger.error("Такого алгоритма нет");
        }
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(Main.DATABASE_HELIOS_URL,"s408665", "tD31nHWkG1c6sjME");

            databaseManagerLogger.info("Подключение к базе данных успешно выполнено");
        } catch (SQLException e) {
            databaseManagerLogger.error("Невозможно подключиться к базе данных");
            databaseManagerLogger.debug(e.toString());
            System.exit(1);
        }
    }

    public void createDatabaseTables() throws SQLException {
        connection.prepareStatement(DBCommands.tablesCreation).execute();
        databaseManagerLogger.info("Таблицы созданы");
    }

    public void addUser(User user) throws SQLException {
        String login = user.getLogin();
        String salt = getRandomString();
        String password = PEPPER + user.getPassword() + salt;

        PreparedStatement preparedStatement = connection.prepareStatement(DBCommands.addUser);
        if (checkExistUser(login)) throw new SQLException();
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, salt);
        preparedStatement.execute();
        databaseManagerLogger.info("Добавлен пользователь " + user.getLogin());
    }

    public boolean confirmUser(User user) {
        try {
            String login = user.getLogin();
            PreparedStatement dbUser = connection.prepareStatement(DBCommands.getUser);
            dbUser.setString(1,login);
            ResultSet resultSet = dbUser.executeQuery();
            if (resultSet.next()) {
                String salt = resultSet.getString("salt");
                String password = PEPPER + user.getPassword() + salt;
                return password.equals(resultSet.getString("password"));
            } else return false;
        } catch (SQLException e) {
            databaseManagerLogger.error("Неверная sql команда");
            return false;
        }
    }


    public boolean checkExistUser(String login) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DBCommands.getUser);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public Long addHumanBeing(HumanBeing humanBeing, User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBCommands.addHumanBeing);
            preparedStatement.setString(1, humanBeing.getName());
            preparedStatement.setInt(2, humanBeing.getCoordinates().getX());
            preparedStatement.setDouble(3, humanBeing.getCoordinates().getY());
            preparedStatement.setTimestamp(4, new Timestamp(humanBeing.getCreationDate().getTime()));
            preparedStatement.setBoolean(5, humanBeing.getRealHero());
            preparedStatement.setBoolean(6, humanBeing.getHasToothpick());
            preparedStatement.setInt(7, humanBeing.getImpactSpeed());
            preparedStatement.setObject(8, humanBeing.getWeaponType(), Types.OTHER);
            if (!Objects.isNull(humanBeing.getMood())) {
                preparedStatement.setObject(9, humanBeing.getMood(), Types.OTHER);
            } else {
                preparedStatement.setNull(9, Types.NULL);
            }
            preparedStatement.setString(10, humanBeing.getCar().getName());
            preparedStatement.setString(11, user.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                databaseManagerLogger.info("Не удалось добавить объект в таблицу");
                return (long) -1;
            }
            databaseManagerLogger.info("Объект успешно добавлен в таблицу");
            return resultSet.getLong(1);
        } catch (SQLException e) {
            databaseManagerLogger.info("Не удалось добавить объект в таблицу");
            databaseManagerLogger.debug(e.toString());
            return (long) -1;
        }
    }

    public boolean updateHumanBeing(Long id, HumanBeing humanBeing, User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBCommands.updateUserHumanBeing);
            preparedStatement.setString(1, humanBeing.getName());
            preparedStatement.setInt(2, humanBeing.getCoordinates().getX());
            preparedStatement.setDouble(3, humanBeing.getCoordinates().getY());
            preparedStatement.setTimestamp(4, new Timestamp(humanBeing.getCreationDate().getTime()));
            preparedStatement.setBoolean(5, humanBeing.getRealHero());
            preparedStatement.setBoolean(6, humanBeing.getHasToothpick());
            preparedStatement.setInt(7, humanBeing.getImpactSpeed());
            preparedStatement.setObject(8, humanBeing.getWeaponType(), Types.OTHER);
            if (!Objects.isNull(humanBeing.getMood())) {
                preparedStatement.setObject(9, humanBeing.getMood(), Types.OTHER);
            } else {
                preparedStatement.setNull(9, Types.NULL);
            }
            preparedStatement.setString(10, humanBeing.getCar().getName());
            preparedStatement.setString(11, user.getLogin());
            preparedStatement.setLong(12, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            databaseManagerLogger.info("Не удалось обновить поля объекта из таблицы");
            return false;
        }
    }

    public boolean deleteHumanBeing(Long id, User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBCommands.deleteUserHumanBeing);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setLong(2, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            databaseManagerLogger.info("Не удалось удалить объект из таблицы");
            return false;
        }
    }

    public boolean deleteAllHumanBeing(User user, List<Long> id) {
        try {
            for (Long curId : id) {
                PreparedStatement preparedStatement = connection.prepareStatement(DBCommands.deleteUserHumanBeing);
                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setLong(2, curId);
                preparedStatement.executeQuery();
            }
            databaseManagerLogger.info("Из таблицы удалены все строки пользователя " + user.getLogin());
            return true;
        } catch (SQLException e) {
            databaseManagerLogger.error("Не удалось удалить строки из таблицы");
            return false;
        }
    }

    public Stack<HumanBeing> loadCollection() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DBCommands.getAllHumanBeing);
            ResultSet resultSet = preparedStatement.executeQuery();
            Stack<HumanBeing> collection = new Stack<>();
            while (resultSet.next()) {
                collection.add(new HumanBeing(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        new Coordinates(resultSet.getInt("coords_x"), resultSet.getDouble("coords_y")),
                        resultSet.getTimestamp("creation_date"),
                        resultSet.getBoolean("real_hero"),
                        resultSet.getBoolean("has_toothpick"),
                        resultSet.getInt("impact_speed"),
                        WeaponType.valueOf(resultSet.getString("weapon_type")),
                        (Objects.isNull(resultSet.getString("mood"))) ? null : Mood.valueOf(resultSet.getString("mood")),
                        new Car(resultSet.getString("car_name")),
                        resultSet.getString("owner_login")
                ));
            }
            databaseManagerLogger.info("Из таблицы успешно загружена коллекция");
            return collection;
        } catch (SQLException e) {
            databaseManagerLogger.error("Возникла ошибка при выполнении запроса");
            return new Stack<>();
        }
    }

    public String getRandomString() {
        Random random = new Random();
        StringBuilder line = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            line.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return line.toString();
    }
}
