package org.mediabox.mediabox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Access(AccessType.FIELD)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(length = 64, nullable = false)
    private String username;
    @Column(unique = true, length = 64, nullable = false)
    private String email;
    @Column(name = "user_password", nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name="user_role")
    private Role role;
    @Column(nullable = false)
    private Long freeSpace;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;
}
