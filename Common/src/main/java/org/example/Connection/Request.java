package org.example.Connection;

import org.example.CollectionModel.HumanBeing;

import java.io.Serializable;
import java.util.Objects;

public class Request implements Serializable {
    private final String commandName;
    private String args = "";
    private HumanBeing humanBeing = null;
    private User user;

    public Request(String commandName, String args, User user) {
        this.commandName = commandName.trim();
        this.args = args;
        this.user = user;
    }

    public Request(String commandName, String args, User user, HumanBeing humanBeing) {
        this.commandName = commandName;
        this.args = args;
        this.user = user;
        this.humanBeing = humanBeing;
    }

    public Request(String commandName, User user, HumanBeing humanBeing) {
        this.commandName = commandName;
        this.user = user;
        this.humanBeing = humanBeing;
    }

    public boolean isEmpty() {
        return (commandName.isEmpty() && args.isEmpty() && humanBeing == null);
    }

    public String getCommandName() {
        return commandName;
    }

    public String getArgs() {
        return args;
    }

    public HumanBeing getHumanBeing() {
        return humanBeing;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request request)) return false;
        return Objects.equals(request.commandName, this.commandName) &&
                Objects.equals(request.args, this.args) &&
                Objects.equals(request.humanBeing, this.humanBeing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandName, args, humanBeing, user);
    }

    @Override
    public String toString() {
        return "~~~ Request ~~~\nCommandName: " + commandName +
                "\nUser: " + user + "\n" +
                (args.isEmpty() ? "" : "Args: " + args + "\n") +
                (humanBeing == null ? "" : "HumanBeing:\n" + humanBeing.toString());
    }
}
