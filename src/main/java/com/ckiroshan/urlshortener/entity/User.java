package com.ckiroshan.urlshortener.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    private Long id;
    @Column(unique = true, nullable = false) // Email must be unique and not null
    private String email;
    @Column(nullable = false) // Password cannot be null
    private String password;
    @Enumerated(EnumType.STRING) // Store enum name ("USER" or "ADMIN") in DB
    private UserRole role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // One user can have many short URLs
    private List<ShortUrl> shortUrls = new ArrayList<>();
}