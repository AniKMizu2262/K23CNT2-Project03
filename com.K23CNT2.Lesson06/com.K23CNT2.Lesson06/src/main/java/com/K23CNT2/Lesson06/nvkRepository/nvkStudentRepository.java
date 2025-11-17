package com.K23CNT2.Lesson06.nvkRepository;
import com.K23CNT2.Lesson06.nvkEntity.nvkStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface nvkStudentRepository extends JpaRepository<nvkStudent, Long> {
}
