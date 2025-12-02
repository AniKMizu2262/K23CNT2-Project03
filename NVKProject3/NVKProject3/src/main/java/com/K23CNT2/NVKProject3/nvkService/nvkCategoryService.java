package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCategory;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class nvkCategoryService {
    @Autowired private nvkCategoryRepository repo;

    public List<nvkCategory> getAllCategories() { return repo.findAll(); }
    public nvkCategory getCategoryById(Long id) { return repo.findById(id).orElse(null); }
    public void saveCategory(nvkCategory category) { repo.save(category); }
    public void deleteCategory(Long id) { repo.deleteById(id); }
}