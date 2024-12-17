package org.mediabox.mediabox.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "email", length = 64, nullable = false)
    private String email;

    @Column(name = "username", length = 60, nullable = false)
    private String username;

    @Column(name = "user_password", length = 20, nullable = false)
    private String userPassword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_role_id", referencedColumnName = "role_id", nullable = false)
    private Role role;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "free_space", nullable = false)
    private Integer freeSpace;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
