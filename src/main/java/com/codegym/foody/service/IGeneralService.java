package com.codegym.foody.service;

import java.util.Optional;

public interface IGeneralService<E> {
    Iterable<E> findAll();

    Optional<E> findById(Long id);

    void save(E e);

    void delete(Long id);
}
