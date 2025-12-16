package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class nvkCustomerService {

    @Autowired
    private nvkCustomerRepository nvkCustomerRepository;

    // ======================
    // 1. LOGIN
    // ======================
    public nvkCustomer login(String email, String password) {
        // Tìm user theo email
        nvkCustomer customer = nvkCustomerRepository.findByNvkEmail(email);

        // Check mật khẩu
        if (customer != null && customer.getNvkPassword().equals(password)) {
            return customer;
        }
        return null; // Sai thông tin
    }

    // ======================
    // 2. LẤY USER THEO ID (Thêm cái này để Controller gọi cho tiện)
    // ======================
    public nvkCustomer getCustomerById(Long id) {
        Optional<nvkCustomer> customer = nvkCustomerRepository.findById(id);
        return customer.orElse(null);
    }

    // ======================
    // 3. UPDATE CUSTOMER INFO
    // ======================
    public nvkCustomer updateCustomer(nvkCustomer customer) {
        // Lấy user trong DB để tránh ghi đè những trường không được chỉnh sửa (như password, email)
        nvkCustomer existingUser = nvkCustomerRepository.findById(customer.getNvkId()).orElse(null);

        if (existingUser != null) {
            // Cập nhật các trường cho phép thay đổi
            existingUser.setNvkFullName(customer.getNvkFullName());
            existingUser.setNvkPhone(customer.getNvkPhone());
            existingUser.setNvkAddress(customer.getNvkAddress());

            // Cập nhật Avatar (nếu người dùng có nhập link mới)
            existingUser.setNvkAvatar(customer.getNvkAvatar());

            return nvkCustomerRepository.save(existingUser);
        }
        return null;
    }

    // ======================
    // 4. ĐỔI MẬT KHẨU
    // ======================
    public boolean changePassword(Long customerId, String oldPass, String newPass) {
        nvkCustomer existingUser = nvkCustomerRepository.findById(customerId).orElse(null);

        if (existingUser != null) {
            // Kiểm tra mật khẩu cũ
            if (existingUser.getNvkPassword().equals(oldPass)) {
                existingUser.setNvkPassword(newPass); // Gán mật khẩu mới
                nvkCustomerRepository.save(existingUser); // Lưu lại
                return true; // Thành công
            }
        }
        return false; // Sai mật khẩu cũ hoặc lỗi
    }
}