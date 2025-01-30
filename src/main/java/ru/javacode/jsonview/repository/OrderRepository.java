package ru.javacode.jsonview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javacode.jsonview.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
