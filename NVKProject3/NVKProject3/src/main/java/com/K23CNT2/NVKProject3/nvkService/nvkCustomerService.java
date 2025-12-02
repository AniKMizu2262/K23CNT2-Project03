package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class nvkCustomerService {
    @Autowired private nvkCustomerRepository repo;

    public nvkCustomer login(String email, String password) {
        Optional<nvkCustomer> op = repo.findByNvkEmail(email);
        if (op.isPresent() && op.get().getNvkPassword().equals(password)) {
            return op.get();
        }
        return null;
    }
}