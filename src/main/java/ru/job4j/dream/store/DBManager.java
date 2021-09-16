package ru.job4j.dream.store;

import java.util.Collection;

public interface DBManager<T> {
    Collection<T> findAll();
    void save(T t);
    T findById(int id);
}
