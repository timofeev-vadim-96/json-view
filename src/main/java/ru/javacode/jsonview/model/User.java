package ru.javacode.jsonview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.javacode.jsonview.view.Views;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(of = {"id", "firstName", "secondName", "email"})
@EqualsAndHashCode(of = {"id"})
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView({Views.UserDetails.class, Views.UserSummary.class})
    private Long id;

    @Setter
    @Column(name = "first_name", nullable = false)
    @JsonProperty("first_name")
    @JsonView({Views.UserDetails.class, Views.UserSummary.class})
    private String firstName;

    @Setter
    @Column(name = "second_name", nullable = false)
    @JsonProperty("second_name")
    @JsonView({Views.UserDetails.class, Views.UserSummary.class})
    private String secondName;

    @Setter
    @Column(name = "email", nullable = false)
    @JsonView({Views.UserDetails.class, Views.UserSummary.class})
    private String email;

    @Setter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Views.UserDetails.class)
    @Fetch(FetchMode.SUBSELECT)
    private List<Order> orders;
}
