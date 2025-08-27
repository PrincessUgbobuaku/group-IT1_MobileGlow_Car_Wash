package za.ac.cput.service;

import java.util.List;

public interface IService<T, ID> {
    T create(T t);
    T read(ID id);
    T update(T t);
    boolean delete(ID id);
    List<T> findAll(); //needed for customer and vehicle classes to find all customers and vehicles
}