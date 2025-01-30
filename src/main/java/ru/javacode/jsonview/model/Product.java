package ru.javacode.jsonview.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.javacode.jsonview.view.Views;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(of = {"id", "name"})
@EqualsAndHashCode(of = {"id"})
@Table(name = "products")
@JsonView(Views.UserDetails.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    @Column(name = "id")
    private Long id;

    @Setter
    @NotBlank(message = "Product name must not be null")
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @NotNull(message = "Product cost must not be null")
    @Column(name = "cost", nullable = false)
    private Double cost;
}
