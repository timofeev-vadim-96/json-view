package ru.javacode.jsonview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.javacode.jsonview.util.OrderStatus;
import ru.javacode.jsonview.view.Views;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView(Views.UserDetails.class)
    private Long id;

    @Setter
    @Column(name = "time", nullable = false)
    @Nullable
    @JsonView(Views.UserDetails.class)
    private LocalDateTime time;

    @Setter
    @JsonProperty("general_cost")
    @Column(name = "general_cost", nullable = false)
    @Nullable
    @JsonView(Views.UserDetails.class)
    private Double generalCost;

    @Setter
    @NotNull(message = "Order status must not be null")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(Views.UserDetails.class)
    private OrderStatus status;

    @Setter
    @JsonProperty("user_id")
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    @JsonView(Views.UserDetails.class)
    private List<Product> products;
}
