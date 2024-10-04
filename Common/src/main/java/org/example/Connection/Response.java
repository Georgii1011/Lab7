package org.example.Connection;

import org.example.CollectionModel.HumanBeing;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class Response implements Serializable {
    private final ResponseStatus responseStatus;
    private String response = "";
    private Collection<HumanBeing> collection;

    public Response(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Response(ResponseStatus responseStatus, String response) {
        this.responseStatus = responseStatus;
        this.response = response.trim();
    }

    public Response(ResponseStatus responseStatus, String response, Collection<HumanBeing> collection) {
        this.responseStatus = responseStatus;
        this.response = response;
        this.collection = collection.stream()
                .sorted(Comparator.comparing(HumanBeing::getId))
                .toList();
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public String getResponse() {
        return response;
    }

    public Collection<HumanBeing> getCollection() {
        return collection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response response)) return false;
        return Objects.equals(this.responseStatus, response.responseStatus) &&
                Objects.equals(this.response, response.response) &&
                Objects.equals(this.collection, response.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(responseStatus, response, collection);
    }

    @Override
    public String toString() {
        return "~~~ Response ~~~\nResponseStatus:: " + responseStatus +
                (response.isEmpty() ? "" : "Response: " + response + "\n") +
                (collection == null ? "" : "Collection:\n" + collection.toString());
    }
}
