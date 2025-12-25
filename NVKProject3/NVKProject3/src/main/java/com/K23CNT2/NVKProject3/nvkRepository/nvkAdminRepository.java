package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface nvkAdminRepository extends JpaRepository<nvkAdmin, Long> {

    /**
     * Tìm kiếm Admin theo username (Dùng cho đăng nhập)
     *
     * @param nvkUsername Tên đăng nhập
     * @return Optional chứa thông tin Admin nếu tìm thấy
     */
    Optional<nvkAdmin> findByNvkUsername(String nvkUsername);
}