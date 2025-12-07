package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface nvkAdminRepository extends JpaRepository<nvkAdmin, Long> {

    // --- Custom Queries ---
    Optional<nvkAdmin> findByNvkUsername(String nvkUsername);
}