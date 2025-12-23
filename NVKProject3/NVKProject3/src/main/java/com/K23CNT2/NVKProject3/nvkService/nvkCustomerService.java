package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class nvkCustomerService {

    @Autowired
    private nvkCustomerRepository nvkCustomerRepository;

    // ==========================================================
    // PHẦN 1: DÀNH CHO ADMIN (QUẢN LÝ KHÁCH HÀNG)
    // ==========================================================

    // Lấy tất cả khách hàng
    public List<nvkCustomer> getAllCustomers() {
        return nvkCustomerRepository.findAll();
    }

    // Lấy khách hàng theo ID
    public nvkCustomer getCustomerById(Long id) {
        Optional<nvkCustomer> customer = nvkCustomerRepository.findById(id);
        return customer.orElse(null);
    }

    // Lưu khách hàng (Dùng cho cả Thêm mới và Sửa từ trang Admin)
    public void saveCustomer(nvkCustomer customer) {
        nvkCustomerRepository.save(customer);
    }

    // Xóa khách hàng
    public void deleteCustomer(Long id) {
        nvkCustomerRepository.deleteById(id);
    }

    // Tìm kiếm (Fix lỗi cú pháp ở đây)
    public List<nvkCustomer> searchCustomer(String keyword) {
        if (keyword != null && keyword.trim().isEmpty()) keyword = null;
        // GỌI QUA BIẾN repository, KHÔNG GỌI QUA CLASS
        return nvkCustomerRepository.findByKeyword(keyword);
    }

    // ==========================================================
    // PHẦN 2: DÀNH CHO CLIENT (LOGIN, PROFILE)
    // ==========================================================

    // 1. LOGIN
    public nvkCustomer login(String email, String password) {
        // Tìm user theo email (Phải đảm bảo Repo có hàm findByNvkEmail)
        nvkCustomer customer = nvkCustomerRepository.findByNvkEmail(email);

        // Check mật khẩu
        if (customer != null && customer.getNvkPassword().equals(password)) {
            return customer;
        }
        return null; // Sai thông tin
    }

    // 2. UPDATE CUSTOMER INFO (Người dùng tự sửa profile)
    public nvkCustomer updateCustomer(nvkCustomer customer) {
        // Lấy user trong DB để tránh ghi đè password/email bằng null
        nvkCustomer existingUser = nvkCustomerRepository.findById(customer.getNvkId()).orElse(null);

        if (existingUser != null) {
            // Cập nhật các trường cho phép thay đổi
            existingUser.setNvkFullName(customer.getNvkFullName());
            existingUser.setNvkPhone(customer.getNvkPhone());
            existingUser.setNvkAddress(customer.getNvkAddress());

            // Cập nhật Avatar (nếu có)
            if (customer.getNvkAvatar() != null) {
                existingUser.setNvkAvatar(customer.getNvkAvatar());
            }

            return nvkCustomerRepository.save(existingUser);
        }
        return null;
    }

    // 3. ĐỔI MẬT KHẨU
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