package org.example.Managers;

import org.example.Connection.Response;

import java.io.ObjectOutputStream;

public record ConnectionManagerPool(Response response, ObjectOutputStream objectOutputStream) {
}
