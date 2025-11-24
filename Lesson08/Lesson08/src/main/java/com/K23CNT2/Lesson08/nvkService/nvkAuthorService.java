package com.K23CNT2.Lesson08.nvkService;

import com.K23CNT2.Lesson08.nvkEntity.nvkAuthor;
import com.K23CNT2.Lesson08.nvkEntity.nvkBook;
import com.K23CNT2.Lesson08.nvkRepository.nvkAuthorRepository;
import com.K23CNT2.Lesson08.nvkRepository.nvkBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class nvkAuthorService {

    @Autowired
    private nvkAuthorRepository nvkAuthorRepository;

    @Autowired
    private nvkBookRepository nvkBookRepository;

    // 1. Lấy tất cả
    public List<nvkAuthor> nvkGetAll() {
        return nvkAuthorRepository.findAll();
    }

    // 2. Lưu
    public void nvkSave(nvkAuthor author) {
        nvkAuthorRepository.save(author);
    }

    // 3. Lấy theo ID
    public nvkAuthor nvkGetById(Long id) {
        return nvkAuthorRepository.findById(id).orElse(null);
    }

    // 4. Tìm list theo danh sách ID
    public List<nvkAuthor> nvkFindAllByIds(List<Long> ids) {
        return nvkAuthorRepository.findAllById(ids);
    }

    // === 5. XÓA TÁC GIẢ  ===
    public void nvkDelete(Long id) {
        // Bước 1: Tìm tác giả
        Optional<nvkAuthor> authorOp = nvkAuthorRepository.findById(id);

        if (authorOp.isPresent()) {
            nvkAuthor author = authorOp.get();
            List<nvkBook> books = author.getNvkBooks();
            if (books != null && !books.isEmpty()) {
                for (nvkBook book : books) {
                    book.getNvkAuthors().remove(author);
                    nvkBookRepository.save(book);
                }
            }


            nvkAuthorRepository.deleteById(id);
        }
    }
}