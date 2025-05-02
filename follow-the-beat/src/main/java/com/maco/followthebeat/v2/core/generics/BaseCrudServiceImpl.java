package com.maco.followthebeat.v2.core.generics;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BaseCrudServiceImpl<T> implements BaseCrudService<T> {
    protected final JpaRepository<T, UUID> repository;

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<T> update(UUID id, T entity) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        return Optional.of(repository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Entity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public Optional<T> getById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }
}
