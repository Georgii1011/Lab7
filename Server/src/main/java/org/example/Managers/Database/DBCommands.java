package org.example.Managers.Database;

public class DBCommands {
    public static final String tablesCreation = """
            CREATE TYPE WEAPON_TYPE AS ENUM (
                'AXE',
                'BAT',
                'MACHINE_GUN',
                'SHOTGUN'
            );
            CREATE TYPE MOOD AS ENUM (
                'LONGING',
                'GLOOM',
                'APATHY',
                'CALM',
                'FRENZY'
            );
            CREATE TABLE IF NOT EXISTS HUMAN_BEING (
                id BIGSERIAL PRIMARY KEY,
                name TEXT NOT NULL,
                coords_x INTEGER NOT NULL,
                coords_y DOUBLE PRECISION,
                creation_date TIMESTAMP NOT NULL,
                real_hero BOOLEAN NOT NULL,
                has_toothpick BOOLEAN NOT NULL,
                impact_speed INTEGER NOT NULL,
                weapon_type WEAPON_TYPE NOT NULL,
                mood MOOD,
                car_name TEXT NOT NULL,
                owner_login TEXT NOT NULL
            );
            CREATE TABLE IF NOT EXISTS USERS (
                id SERIAl PRIMARY KEY,
                login TEXT NOT NULL,
                password TEXT NOT NULL,
                salt TEXT
            );
            """;

    public static final String addUser = """
            INSERT INTO USERS(login, password, salt) VALUES (?, ?, ?);
            """;

    public static final String getUser = """
            SELECT * FROM USERS WHERE (login = ?);
            """;

    public static final String addHumanBeing = """
            INSERT INTO HUMAN_BEING(
                name,
                coords_x,
                coords_y,
                creation_date,
                real_hero,
                has_toothpick,
                impact_speed,
                weapon_type,
                mood,
                car_name,
                owner_login
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id;
            """;

    public static final String getAllHumanBeing = """
            SELECT * FROM HUMAN_BEING;
            """;

    public static final String deleteUserHumanBeing = """
            DELETE FROM HUMAN_BEING WHERE (owner_login = ?) AND (id = ?) RETURNING id;
            """;

    public static final String updateUserHumanBeing = """
            UPDATE HUMAN_BEING SET (
                name,
                coords_x,
                coords_y,
                creation_date,
                real_hero,
                has_toothpick,
                impact_speed,
                weapon_type,
                mood,
                car_name
            ) = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            WHERE (owner_login = ?) AND (id = ?)
            RETURNING id;
            """;

    public static final String checkUserHumanBeing = """
            SELECT * FROM human_being WHERE (owner_login = ?) AND (id = ?);
            """;
}
