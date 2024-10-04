package org.example.Commands;

import org.example.Commands.Interfaces.Executable;
import org.example.Managers.Database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.ExecutableElement;
import java.util.Objects;

/**
 * Класс-шаблон для команд
 */
public abstract class Command implements Executable {
    private final String name;
    private final String args;
    private final String description;

    final Logger commandLogger = LoggerFactory.getLogger(this.getClass());


    public Command(String name, String description) {
        this.name = name;
        this.args = null;
        this.description = description;
    }

    public Command(String name, String args, String description) {
        this.name = name;
        this.args = args;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Command p = (Command) o;
        return this.name.equals(p.name) && this.description.equals(p.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }
}