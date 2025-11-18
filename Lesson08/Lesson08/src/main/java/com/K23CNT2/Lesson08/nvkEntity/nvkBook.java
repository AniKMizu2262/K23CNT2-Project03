package com.K23CNT2.Lesson08.nvkEntity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    private String nvkCode;
    private String nvkTitle;
    private String nvkDescription;
    private String nvkImgUrl;
    private Double nvkPrice;
    private Integer nvkQuantity;
    private Boolean nvkActive;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "nvk_book_author",
            joinColumns = @JoinColumn(name = "nvk_book_id"),
            inverseJoinColumns = @JoinColumn(name = "nvk_author_id")
    )
    @ToString.Exclude
    private List<nvkAuthor> nvkAuthors;
}