package com.codegym.foody.service;


public interface IGeneralService<T> {
    void save(T t);
    void update(T t);
    void delete(Long id);
    long getTotal();
    T getById(Long id);
    int getPage(Long id);
}
