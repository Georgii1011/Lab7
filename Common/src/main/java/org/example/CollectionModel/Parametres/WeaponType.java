package org.example.CollectionModel.Parametres;

import java.io.Serializable;

/**
 * Класс возможных типов оружия героя
 */
public enum WeaponType implements Serializable {
    AXE,
    SHOTGUN,
    MACHINE_GUN,
    BAT;

    public static String list() {
        StringBuilder resultStr = new StringBuilder();
        for (var names : values()) {
            resultStr.append(names.name()).append("\n");
        }
        return resultStr.toString();
    }
}