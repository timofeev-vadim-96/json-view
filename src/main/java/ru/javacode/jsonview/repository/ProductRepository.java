package ru.javacode.jsonview.repository;

import org.springframework.data.repository.CrudRepository;
import ru.javacode.jsonview.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
