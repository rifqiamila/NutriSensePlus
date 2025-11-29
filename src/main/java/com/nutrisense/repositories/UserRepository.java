package com.nutrisense.repositories;

import java.util.List;
import java.util.stream.Collectors;

import com.nutrisense.models.user.DapurMBG;
import com.nutrisense.models.user.Siswa;
import com.nutrisense.models.user.User;

public class UserRepository extends JsonRepository<User> {
    
    private static final String USER_FILE_PATH = "src/main/resources/data/users.json";

    public UserRepository() {
        super(USER_FILE_PATH, User.class);
    }

    // User-specific methods
    public User findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.isActive()) {
            return user;
        }
        return null;
    }

    public List<Siswa> findAllSiswa() {
        return findAll().stream()
                .filter(user -> user instanceof Siswa)
                .map(user -> (Siswa) user)
                .collect(Collectors.toList());
    }

    public List<DapurMBG> findAllDapur() {
        return findAll().stream()
                .filter(user -> user instanceof DapurMBG)
                .map(user -> (DapurMBG) user)
                .collect(Collectors.toList());
    }

    public List<Siswa> findSiswaBySekolah(String sekolahId) {
        return findAllSiswa().stream()
                .filter(siswa -> sekolahId.equals(siswa.getSekolahId()))
                .collect(Collectors.toList());
    }
}