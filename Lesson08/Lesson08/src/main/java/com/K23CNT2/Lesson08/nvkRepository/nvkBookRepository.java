package com.K23CNT2.Lesson08.nvkRepository;
import com.K23CNT2.Lesson08.nvkEntity.nvkBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface nvkBookRepository extends JpaRepository<nvkBook, Long> {
}