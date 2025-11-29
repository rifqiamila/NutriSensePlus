package com.nutrisense.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nutrisense.models.user.DapurMBG;
import com.nutrisense.models.user.Sekolah;
import com.nutrisense.models.user.Siswa;
import com.nutrisense.models.user.User;
import com.nutrisense.models.user.User.UserRole;
import com.nutrisense.repositories.SekolahRepository;
import com.nutrisense.repositories.UserRepository;
import com.nutrisense.utils.PasswordUtil;

public class UserService {
    private final UserRepository userRepo;
    private final SekolahRepository sekolahRepo;

    public UserService() {
        this.userRepo = new UserRepository();
        this.sekolahRepo = new SekolahRepository();
    }

    // ==================== AUTO-DETECT LOGIN SYSTEM ====================

    /**
     * Main login method with auto-detection and auto-registration
     */
    public User login(String username, String password) {
        // First, try to find existing user
        User existingUser = userRepo.findByUsername(username);
        
        if (existingUser != null) {
            // Verify password for existing user
            if (PasswordUtil.verifyPassword(password, existingUser.getPassword()) && existingUser.isActive()) {
                return existingUser;
            }
            return null; // Wrong password or inactive
        }
        
        // If user doesn't exist, auto-detect role and attempt auto-registration
        UserRole detectedRole = detectRoleFromUsername(username);
        return attemptAutoRegister(username, password, detectedRole);
    }

    /**
     * Auto-detect user role based on username pattern
     */
    public UserRole detectRoleFromUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return UserRole.UMUM;
        }
        
        String cleanUsername = username.trim().toLowerCase();
        
        if (cleanUsername.equals("admin")) {
            return UserRole.ADMIN;
        } else if (cleanUsername.startsWith("nisn_")) {
            return UserRole.SISWA;
        } else if (cleanUsername.startsWith("dapur_")) {
            return UserRole.DAPUR_MBG;
        } else if (cleanUsername.startsWith("guru_")) {
            return UserRole.UMUM; // Future: GURU role
        } else {
            return UserRole.UMUM;
        }
    }

    /**
     * Attempt auto-registration based on detected role
     */
    private User attemptAutoRegister(String username, String password, UserRole role) {
        switch (role) {
            case SISWA:
                return autoRegisterSiswa(username, password);
            case DAPUR_MBG:
                return autoRegisterDapur(username, password);
            case UMUM:
                return autoRegisterUmum(username, password);
            case ADMIN:
                // Admin cannot auto-register
                return null;
            default:
                return null;
        }
    }

    // ==================== AUTO-REGISTRATION METHODS ====================

    private User autoRegisterSiswa(String username, String password) {
        try {
            // Extract NISN from username: nisn_20241001001 → 20241001001
            String nisn = username.replace("nisn_", "").replace("NISN_", "");
            
            // Validate NISN format (10-12 digits)
            if (!nisn.matches("\\d{10,12}")) {
                System.out.println("Invalid NISN format: " + nisn);
                return null;
            }
            
            // Check if NISN already exists
            if (userRepo.findAllSiswa().stream().anyMatch(s -> s.getNisn().equals(nisn))) {
                System.out.println("NISN already exists: " + nisn);
                return null;
            }
            
            // Auto-assign to sekolah based on NISN pattern
            String sekolahId = findSekolahForSiswa(nisn);
            if (sekolahId == null) {
                System.out.println("No available sekolah for NISN: " + nisn);
                return null;
            }
            
            // Create new siswa
            String id = "SIS" + System.currentTimeMillis();
            String hashedPassword = PasswordUtil.hashPassword(password);
            String namaLengkap = "Siswa " + nisn; // Default name, can be updated later
            
            Siswa siswa = new Siswa(id, username, hashedPassword, nisn, namaLengkap, sekolahId);
            userRepo.save(siswa);
            
            System.out.println("Auto-registered siswa: " + username);
            return siswa;
            
        } catch (Exception e) {
            System.err.println("Error auto-registering siswa: " + e.getMessage());
            return null;
        }
    }

    private User autoRegisterDapur(String username, String password) {
        try {
            // Extract school code from username: dapur_sd05 → sd05
            String schoolCode = username.replace("dapur_", "").replace("DAPUR_", "").toLowerCase();
            
            // Find matching sekolah based on code
            String sekolahId = findSekolahByCode(schoolCode);
            if (sekolahId == null) {
                System.out.println("No sekolah found for code: " + schoolCode);
                return null;
            }
            
            // Create new dapur
            String id = "DAP" + System.currentTimeMillis();
            String hashedPassword = PasswordUtil.hashPassword(password);
            String namaDapur = "Dapur " + getSekolahName(sekolahId);
            
            DapurMBG dapur = new DapurMBG(id, username, hashedPassword, namaDapur, 
                                          Arrays.asList("Petugas " + schoolCode), 
                                          Arrays.asList(sekolahId));
            userRepo.save(dapur);
            
            System.out.println("Auto-registered dapur: " + username);
            return dapur;
            
        } catch (Exception e) {
            System.err.println("Error auto-registering dapur: " + e.getMessage());
            return null;
        }
    }

    private User autoRegisterUmum(String username, String password) {
        try {
            // Simple registration for general users
            String id = "UMUM" + System.currentTimeMillis();
            String hashedPassword = PasswordUtil.hashPassword(password);
            
            // Create simple User with UMUM role
            User user = new User(id, username, hashedPassword, UserRole.UMUM) {};
            user.setCreatedAt(LocalDateTime.now());
            userRepo.save(user);
            
            System.out.println("Auto-registered umum: " + username);
            return user;
            
        } catch (Exception e) {
            System.err.println("Error auto-registering umum: " + e.getMessage());
            return null;
        }
    }

    // ==================== MANUAL REGISTRATION (OPTIONAL) ====================

    public boolean registerSiswaManual(String nisn, String namaLengkap, String sekolahId, String username, String password) {
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
        String hashedPassword = PasswordUtil.hashPassword(password);
        Siswa siswa = new Siswa(id, username, hashedPassword, nisn, namaLengkap, sekolahId);
        userRepo.save(siswa);
        return true;
    }

    public boolean registerDapurManual(String namaDapur, List<String> penanggungJawab, 
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
        String hashedPassword = PasswordUtil.hashPassword(password);
        DapurMBG dapur = new DapurMBG(id, username, hashedPassword, namaDapur, penanggungJawab, sekolahIds);
        userRepo.save(dapur);
        return true;
    }

    // ==================== USER MANAGEMENT ====================

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

    public boolean updateUserPassword(String username, String newPassword) {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            user.setPassword(PasswordUtil.hashPassword(newPassword));
            userRepo.save(user);
            return true;
        }
        return false;
    }

    // ==================== HELPER METHODS ====================

    private String findSekolahForSiswa(String nisn) {
        List<Sekolah> sekolahList = sekolahRepo.findAktif();
        if (sekolahList.isEmpty()) return null;
        
        // Simple logic: use NISN hash to assign sekolah
        int index = Math.abs(nisn.hashCode()) % sekolahList.size();
        return sekolahList.get(index).getId();
    }

    private String findSekolahByCode(String code) {
        // Map school codes to actual sekolah IDs
        Map<String, String> schoolMap = new HashMap<>();
        schoolMap.put("sd05", "SCH001");
        schoolMap.put("smp14", "SCH002"); 
        schoolMap.put("sma05", "SCH003");
        schoolMap.put("sd12", "SCH004");
        schoolMap.put("smp22", "SCH005");
        schoolMap.put("sma07", "SCH006");
        schoolMap.put("sd09", "SCH007");
        schoolMap.put("smp08", "SCH008");
        schoolMap.put("sma21", "SCH009");
        schoolMap.put("sd03", "SCH010");
        schoolMap.put("smp11", "SCH011");
        schoolMap.put("sma18", "SCH012");
        schoolMap.put("sd17", "SCH013");
        schoolMap.put("smp19", "SCH014");
        schoolMap.put("sma02", "SCH015");
        
        return schoolMap.get(code.toLowerCase());
    }

    private String getSekolahName(String sekolahId) {
        Sekolah sekolah = sekolahRepo.findById(sekolahId);
        return sekolah != null ? sekolah.getNama() : "Sekolah";
    }

    public List<Sekolah> getAllSekolah() {
        return sekolahRepo.findAll();
    }

    public Sekolah getSekolahById(String sekolahId) {
        return sekolahRepo.findById(sekolahId);
    }

    // ==================== VALIDATION METHODS ====================

    public boolean isUsernameAvailable(String username) {
        return userRepo.findByUsername(username) == null;
    }

    public boolean isNisnAvailable(String nisn) {
        return userRepo.findAllSiswa().stream().noneMatch(s -> s.getNisn().equals(nisn));
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}