package org.example.CommandSpace.Forms;

/**
 * Абстрактный класс опросника
 * @param <T>
 */
public abstract class Form<T> {
    public abstract T build();
}
