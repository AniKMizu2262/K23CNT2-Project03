package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkAdmin;
import com.K23CNT2.NVKProject3.nvkRepository.nvkAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class nvkAdminService {

    @Autowired
    private nvkAdminRepository repo;

    // --- Xử lý Đăng nhập ---
    public nvkAdmin login(String username, String password) {
        // Tìm user theo tên đăng nhập
        Optional<nvkAdmin> adminOp = repo.findByNvkUsername(username);

        // Nếu tồn tại và mật khẩu khớp
        if (adminOp.isPresent() && adminOp.get().getNvkPassword().equals(password)) {
            return adminOp.get();
        }

        return null; // Đăng nhập thất bại
    }
}