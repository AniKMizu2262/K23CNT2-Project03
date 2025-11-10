package service; // <-- Fix theo ý bro

import dto.NvkUsersDTO;
import entity.nvkUser;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class NvkUsersService {

    List<nvkUser> userList = new ArrayList<nvkUser>();

    public NvkUsersService() {
        userList.add(new nvkUser(1L, "user1", "pass1", "John Doe",
                LocalDate.parse("1990-01-01"), "john@example.com", "1234567890", 34, true));
        userList.add(new nvkUser(2L, "user2", "pass2", "Jane Smith",
                LocalDate.parse("1992-05-15"), "jane@example.com", "0987654321", 32,
                false));

    }

    public List<nvkUser> findAll() {
        return userList;
    }

    public Boolean create(NvkUsersDTO nvkUsersDTO) {
        nvkUser user = new nvkUser();
        try {
            user.setNvkId(userList.stream().count() + 1);

            user.setNvkUsername(nvkUsersDTO.getNvkUsername());
            user.setNvkPassword(nvkUsersDTO.getNvkPassword());
            user.setNvkEmail(nvkUsersDTO.getNvkEmail());
            user.setNvkFullName(nvkUsersDTO.getNvkFullName());
            user.setNvkPhone(nvkUsersDTO.getNvkPhone());
            user.setNvkAge(nvkUsersDTO.getNvkAge());
            user.setNvkBirthDay(nvkUsersDTO.getNvkBirthDay());
            user.setNvkStatus(nvkUsersDTO.getNvkStatus());

            userList.add(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}