package com.nutrisense.services;

import java.util.List;

import com.nutrisense.models.user.DapurMBG;
import com.nutrisense.models.user.Sekolah;
import com.nutrisense.models.user.Siswa;
import com.nutrisense.models.user.User;
import com.nutrisense.repositories.SekolahRepository;
import com.nutrisense.repositories.UserRepository;

public class UserService {
    private final UserRepository userRepo;
    private final SekolahRepository sekolahRepo;

    public UserService() {
        this.userRepo = new UserRepository();
        this.sekolahRepo = new SekolahRepository();
    }

    // Authentication
    public User login(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.isActive()) {
            return user;
        }
        return null;
    }

    // Registration
    public boolean registerSiswa(String nisn, String namaLengkap, String sekolahId, String username, String password) {
        // Validate NISN uniqueness
        if (userRepo.findAllSiswa().stream().anyMatch(s -> s.getNisn().equals(nisn))) {
            return false;
        }

        // Validate username uniqueness
        if (userRepo.findByUsername(username) != null) {
            return false;
        }

        // Validate sekolah exists
        if (sekolahRepo.findById(sekolahId) == null) {
            return false;
        }

        // Create new siswa
        String id = "SIS" + System.currentTimeMillis();
        Siswa siswa = new Siswa(id, username, password, nisn, namaLengkap, sekolahId);
        userRepo.save(siswa);
        return true;
    }

    public boolean registerDapur(String namaDapur, List<String> penanggungJawab, 
                                List<String> sekolahIds, String username, String password) {
        // Validate username uniqueness
        if (userRepo.findByUsername(username) != null) {
            return false;
        }

        // Validate semua sekolah exists
        for (String sekolahId : sekolahIds) {
            if (sekolahRepo.findById(sekolahId) == null) {
                return false;
            }
        }

        // Create new dapur
        String id = "DAP" + System.currentTimeMillis();
        DapurMBG dapur = new DapurMBG(id, username, password, namaDapur, penanggungJawab, sekolahIds);
        userRepo.save(dapur);
        return true;
    }

    // User management
    public List<Siswa> getSiswaBySekolah(String sekolahId) {
        return userRepo.findSiswaBySekolah(sekolahId);
    }

    public List<DapurMBG> getAllDapur() {
        return userRepo.findAllDapur();
    }

    public User getUserById(String userId) {
        return userRepo.findById(userId);
    }

    public boolean deactivateUser(String userId) {
        User user = userRepo.findById(userId);
        if (user != null) {
            user.setActive(false);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    // Helper methods
    public List<Sekolah> getAllSekolah() {
        return sekolahRepo.findAll();
    }

    public Sekolah getSekolahById(String sekolahId) {
        return sekolahRepo.findById(sekolahId);
    }
}