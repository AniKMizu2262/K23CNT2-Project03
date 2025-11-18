package com.K23CNT2.Lesson08.nvkService;

import com.K23CNT2.Lesson08.nvkEntity.nvkAuthor;
import com.K23CNT2.Lesson08.nvkRepository.nvkAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class nvkAuthorService {
    @Autowired private nvkAuthorRepository repo;

    public List<nvkAuthor> nvkGetAll() { return repo.findAll(); }
    public void nvkSave(nvkAuthor author) { repo.save(author); }
    public nvkAuthor nvkGetById(Long id) { return repo.findById(id).orElse(null); }
    public void nvkDelete(Long id) { repo.deleteById(id); }


    public List<nvkAuthor> nvkFindAllByIds(List<Long> ids) {
        return repo.findAllById(ids);
    }
}
