package com.K23CNT2.Lesson06.nvkEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    private String nvkName;
    private String nvkEmail;
    private int nvkAge;


    private Boolean nvkStatus;
}
