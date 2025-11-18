package com.K23CNT2.Lesson08.nvkEntity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "authors")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    private String nvkCode;
    private String nvkName;
    private String nvkDescription;
    private String nvkImgUrl;
    private String nvkEmail;
    private String nvkPhone;
    private String nvkAddress;
    private Boolean nvkActive;


    @ManyToMany(mappedBy = "nvkAuthors")
    @ToString.Exclude
    private List<nvkBook> nvkBooks;
}