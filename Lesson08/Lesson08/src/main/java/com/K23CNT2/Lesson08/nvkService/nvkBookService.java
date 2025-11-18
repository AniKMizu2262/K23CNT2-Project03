package com.K23CNT2.Lesson08.nvkService;

import com.K23CNT2.Lesson08.nvkEntity.nvkBook;
import com.K23CNT2.Lesson08.nvkRepository.nvkBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class nvkBookService {
    @Autowired private nvkBookRepository repo;

    public List<nvkBook> nvkGetAll() { return repo.findAll(); }
    public void nvkSave(nvkBook book) { repo.save(book); }
    public nvkBook nvkGetById(Long id) { return repo.findById(id).orElse(null); }
    public void nvkDelete(Long id) { repo.deleteById(id); }
}
