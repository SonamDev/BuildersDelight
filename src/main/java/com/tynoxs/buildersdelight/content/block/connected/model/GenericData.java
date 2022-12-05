package com.tynoxs.buildersdelight.content.block.connected.model;

import java.util.function.Predicate;

public class GenericData<T> {

    private final String name;
    private T data;

    public GenericData(String name) {
        this.name = name;
    }

    public GenericData<T> withData(T data) {
        this.data = data;
        return this;
    }
    
    public T getData() {
        return this.data;
    }

    public static <T> Predicate<GenericData<T>> predicate(String name) {
        return tGenericData -> tGenericData.name.equals(name);
    }

}
