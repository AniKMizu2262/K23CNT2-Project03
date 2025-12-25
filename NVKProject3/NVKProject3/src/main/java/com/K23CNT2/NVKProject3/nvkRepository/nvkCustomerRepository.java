package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface nvkCustomerRepository extends JpaRepository<nvkCustomer, Long> {

    /**
     * Tìm khách hàng theo Email (Dùng cho Login/Register check trùng)
     */
    nvkCustomer findByNvkEmail(String nvkEmail);

    /**
     * Tìm kiếm khách hàng theo Tên, Email hoặc Số điện thoại
     *
     * @param keyword Từ khóa tìm kiếm
     */
    @Query("SELECT c FROM nvkCustomer c WHERE " +
            "(:keyword IS NULL OR c.nvkFullName LIKE %:keyword% " +
            "OR c.nvkEmail LIKE %:keyword% " +
            "OR c.nvkPhone LIKE %:keyword%)")
    List<nvkCustomer> findByKeyword(@Param("keyword") String keyword);
}