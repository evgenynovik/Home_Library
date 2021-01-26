package com.netcracker.education.services.converters;

public interface Converter<T, V> {

    T convertToEntity(V v);

    V convertToDTO(T t);
}
