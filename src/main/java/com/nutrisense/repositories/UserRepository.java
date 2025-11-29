package com.nutrisense.repositories;

import java.util.List;
import java.util.stream.Collectors;

import com.nutrisense.models.user.Admin;
import com.nutrisense.models.user.DapurMBG;
import com.nutrisense.models.user.Siswa;
import com.nutrisense.models.user.User;
import com.nutrisense.models.user.User.UserRole;

public class UserRepository extends JsonRepository<User> {
    
    public UserRepository() {
        super("src/main/resources/data/users.json", User.class);
    }

    @Override
    protected User parseItem(String jsonItem) {
        try {
            // Manual parsing dari String JSON
            String role = extractField(jsonItem, "role");
            
            if (role == null) {
                return createUserFromJson(jsonItem, User.class);
            }
            
            UserRole userRole = UserRole.valueOf(role);
            
            switch (userRole) {
                case SISWA:
                    return createUserFromJson(jsonItem, Siswa.class);
                case DAPUR_MBG:
                    return createUserFromJson(jsonItem, DapurMBG.class);
                case ADMIN:
                    return createUserFromJson(jsonItem, Admin.class);
                case UMUM:
                default:
                    return createUserFromJson(jsonItem, User.class);
            }
        } catch (Exception e) {
            System.err.println("Error parsing user: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected String convertItemToJson(User item) {
        // Simple manual JSON conversion
        StringBuilder json = new StringBuilder("{");
        
        json.append("\"id\":\"").append(item.getId()).append("\",");
        json.append("\"username\":\"").append(item.getUsername()).append("\",");
        json.append("\"password\":\"").append(item.getPassword()).append("\",");
        json.append("\"role\":\"").append(item.getRole()).append("\",");
        json.append("\"isActive\":").append(item.isActive());
        
        // Add role-specific fields
        if (item instanceof Siswa siswa) {
            json.append(",\"nisn\":\"").append(siswa.getNisn()).append("\"");
            json.append(",\"sekolahId\":\"").append(siswa.getSekolahId()).append("\"");
            json.append(",\"nama\":\"").append(siswa.getNamaLengkap()).append("\"");
        } else if (item instanceof DapurMBG dapur) {
            json.append(",\"namaDapur\":\"").append(dapur.getNamaDapur()).append("\"");
            json.append(",\"penanggungJawab\":\"").append(String.join(",", dapur.getPenanggungJawab())).append("\"");
            json.append(",\"sekolahIds\":\"").append(String.join(",", dapur.getSekolahIds())).append("\"");
        } else if (item instanceof Admin admin) {
            json.append(",\"email\":\"").append(admin.getEmail()).append("\"");
        }
        
        json.append("}");
        return json.toString();
    }

    private User createUserFromJson(String jsonItem, Class<? extends User> userClass) {
        try {
            User user = userClass.getDeclaredConstructor().newInstance();
            
            user.setId(extractField(jsonItem, "id"));
            user.setUsername(extractField(jsonItem, "username"));
            user.setPassword(extractField(jsonItem, "password"));
            user.setRole(UserRole.valueOf(extractField(jsonItem, "role")));
            user.setActive(Boolean.parseBoolean(extractField(jsonItem, "isActive")));
            
            // Set role-specific fields
            if (user instanceof Siswa siswa) {
                siswa.setNisn(extractField(jsonItem, "nisn"));
                siswa.setSekolahId(extractField(jsonItem, "sekolahId"));
                siswa.setNamaLengkap(extractField(jsonItem, "nama"));
            } else if (user instanceof DapurMBG dapur) {
                dapur.setNamaDapur(extractField(jsonItem, "namaDapur"));
                // Handle array fields for penanggungJawab and sekolahIds
                String penanggungJawab = extractField(jsonItem, "penanggungJawab");
                if (penanggungJawab != null) {
                    dapur.setPenanggungJawab(List.of(penanggungJawab.split(",")));
                }
                String sekolahIds = extractField(jsonItem, "sekolahIds");
                if (sekolahIds != null) {
                    dapur.setSekolahIds(List.of(sekolahIds.split(",")));
                }
            } else if (user instanceof Admin admin) {
                admin.setEmail(extractField(jsonItem, "email"));
            }
            
            return user;
        } catch (Exception e) {
            System.err.println("Error creating user from JSON: " + e.getMessage());
            return null;
        }
    }

    private String extractField(String json, String fieldName) {
        try {
            String search = "\"" + fieldName + "\":";
            int startIndex = json.indexOf(search);
            if (startIndex == -1) return null;
            
            startIndex += search.length();
            int endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) endIndex = json.indexOf("}", startIndex);
            
            if (endIndex == -1) return null;
            
            String value = json.substring(startIndex, endIndex).trim();
            
            // Remove quotes if present
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    // Tambah method-method ini ke class UserRepository
    public User findByUsername(String username) {
        List<User> allUsers = findAll();
        return allUsers.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst()
                .orElse(null);
    }

    public List<Siswa> findAllSiswa() {
        List<User> allUsers = findAll();
        return allUsers.stream()
                .filter(user -> user instanceof Siswa)
                .map(user -> (Siswa) user)
                .collect(Collectors.toList());
    }

    public List<DapurMBG> findAllDapur() {
        List<User> allUsers = findAll();
        return allUsers.stream()
                .filter(user -> user instanceof DapurMBG)
                .map(user -> (DapurMBG) user)
                .collect(Collectors.toList());
    }

    public List<Siswa> findSiswaBySekolah(String sekolahId) {
        List<Siswa> allSiswa = findAllSiswa();
        return allSiswa.stream()
                .filter(siswa -> sekolahId.equals(siswa.getSekolahId()))
                .collect(Collectors.toList());
    }

    public List<User> findByRole(UserRole role) {
        List<User> allUsers = findAll();
        return allUsers.stream()
                .filter(user -> role == user.getRole())
                .collect(Collectors.toList());
    }
}