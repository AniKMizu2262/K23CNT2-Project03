package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class nvkCustomerService {

    @Autowired
    private nvkCustomerRepository repo;

    // ==========================================================
    // PHẦN 1: ADMIN QUẢN LÝ KHÁCH HÀNG
    // ==========================================================

    public List<nvkCustomer> getAllCustomers() {
        return repo.findAll();
    }

    public nvkCustomer getCustomerById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void saveCustomer(nvkCustomer customer) {
        repo.save(customer);
    }

    public void deleteCustomer(Long id) {
        repo.deleteById(id);
    }

    public List<nvkCustomer> searchCustomer(String keyword) {
        if (keyword != null && keyword.trim().isEmpty()) keyword = null;
        return repo.findByKeyword(keyword);
    }

    // ==========================================================
    // PHẦN 2: CLIENT (LOGIN, PROFILE)
    // ==========================================================

    public nvkCustomer login(String email, String password) {
        nvkCustomer customer = repo.findByNvkEmail(email);
        if (customer != null && customer.getNvkPassword().equals(password)) {
            return customer;
        }
        return null;
    }

    public nvkCustomer updateCustomer(nvkCustomer customer) {
        nvkCustomer existingUser = repo.findById(customer.getNvkId()).orElse(null);
        if (existingUser != null) {
            existingUser.setNvkFullName(customer.getNvkFullName());
            existingUser.setNvkPhone(customer.getNvkPhone());
            existingUser.setNvkAddress(customer.getNvkAddress());

            if (customer.getNvkAvatar() != null) {
                existingUser.setNvkAvatar(customer.getNvkAvatar());
            }
            return repo.save(existingUser);
        }
        return null;
    }

    public boolean changePassword(Long customerId, String oldPass, String newPass) {
        nvkCustomer existingUser = repo.findById(customerId).orElse(null);
        if (existingUser != null && existingUser.getNvkPassword().equals(oldPass)) {
            existingUser.setNvkPassword(newPass);
            repo.save(existingUser);
            return true;
        }
        return false;
    }
}