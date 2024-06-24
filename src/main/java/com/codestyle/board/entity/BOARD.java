package com.codestyle.board.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "BOARD")

public class BOARD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 또는 SEQUENCE
    private Long ID;

    private String TITLE;
    private String CONTENT;
}
