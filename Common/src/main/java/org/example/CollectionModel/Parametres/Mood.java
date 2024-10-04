package org.example.CollectionModel.Parametres;

import java.io.Serializable;

/**
 * Класс возможных настроений героя
 */
public enum Mood implements Serializable {
    LONGING,
    GLOOM,
    APATHY,
    CALM,
    FRENZY;

    public static String list() {
        StringBuilder resultStr = new StringBuilder();
        for (var names : values()) {
            resultStr.append(names.name()).append("\n");
        }
        return resultStr.toString();
    }
}
