package com.K23CNT2.NVKProject3.nvkRepository;
import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface nvkOrderRepository extends JpaRepository<nvkOrder, Long> {
}