package com.codestyle.board.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "signupdata")
public class SignUpdata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name") // 사용자명 필드 추가
    private String name;

    @Column(name = "password")
    private String password;

    // Getter와 Setter 메서드
}
