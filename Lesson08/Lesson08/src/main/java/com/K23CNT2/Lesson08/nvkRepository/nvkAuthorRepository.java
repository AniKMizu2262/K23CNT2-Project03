package com.K23CNT2.Lesson08.nvkRepository;

import com.K23CNT2.Lesson08.nvkEntity.nvkAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface nvkAuthorRepository extends JpaRepository<nvkAuthor, Long> {
}
