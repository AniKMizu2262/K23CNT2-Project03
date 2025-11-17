package com.K23CNT2.Lesson06.nvkService;

import com.K23CNT2.Lesson06.nvkEntity.nvkStudent;
import com.K23CNT2.Lesson06.nvkRepository.nvkStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class nvkStudentService {
    @Autowired
    private nvkStudentRepository nvkStudentRepository;

    public List<nvkStudent> nvkGetAll() {
        return nvkStudentRepository.findAll();
    }

    public Optional<nvkStudent> nvkGetById(Long id) {
        return nvkStudentRepository.findById(id);
    }

    public void nvkSave(nvkStudent student) {
        nvkStudentRepository.save(student);
    }

    public void nvkDelete(Long id) {
        nvkStudentRepository.deleteById(id);
    }
}
