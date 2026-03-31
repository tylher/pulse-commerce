package com.damoladev.pulsecommerce.model;

import com.damoladev.pulsecommerce.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ElementCollection
    @CollectionTable(
            name = "user_addresses",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Address> addresses;

    @JsonIgnore
    private String password;


}
