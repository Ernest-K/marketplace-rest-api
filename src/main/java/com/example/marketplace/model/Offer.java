package com.example.marketplace.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    @Column(length = 250)
    private String description;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Offer(String name, String description, Double price, User user, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.user = user;
        this.category = category;
    }
}
