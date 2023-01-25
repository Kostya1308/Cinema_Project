package by.academy.final_project.repository;

import java.util.List;

public interface Crud<T> {

    boolean create(T t);

    List<T> readAll();

    T read(T t);

    T read(String str);

    boolean update(T t, T newT);

    boolean delete(T t);

}
