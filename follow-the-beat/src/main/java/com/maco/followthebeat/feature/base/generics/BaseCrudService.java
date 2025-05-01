package com.maco.followthebeat.feature.base.generics;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseCrudService<T> {
    T save(T entity);
    Optional<T> update(UUID id, T entity);
    void delete(UUID id);
    Optional<T> getById(UUID id);
    List<T> getAll();
}