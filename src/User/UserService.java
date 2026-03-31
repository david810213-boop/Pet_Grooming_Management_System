package User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PetManagement.Pet;

// public class UserService {
//     private List<User> usersList = new ArrayList<>();

//     // 使用者註冊
//     public String register(User user) {
//         if (user.getMemberName() == null || user.getPassword() == null || user.getEmail() == null) {
//             return "註冊失敗：資料不完整";
//         }
//         // 檢查是否重複
//         for (User u : usersList) {
//             if (u.getEmail().equals(user.getEmail())) {
//                 return "註冊失敗：帳號已存在";
//             }
//         }
//         usersList.add(user);
//         return "註冊成功";
//     }
//     // 使用者登入
//     public User login(String email, String password) {
//         for (User u : usersList) {
//             if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
//                 return u; // 登入成功，回傳 User 物件
//             }
//         }
//         return null; // 登入失敗
//     }
//     // 新增寵物到使用者
//     public String addPetToUser(String userEmail, Pet pet) {
//         for (User u : usersList) {
//             if (u.getEmail().equals(userEmail)) {
//                 u.addPet(pet);
//                 return "寵物新增成功";
//             }
//         }
//         return "找不到該使用者";
//     }


//     // 取得使用者的寵物清單
//     public List<Pet> getPetsByEmail(String userEmail) {
//         for (User u : usersList) {
//             if (u.getEmail().equals(userEmail)) {
//                 return u.getPets();
//             }
//         }
//         return new ArrayList<>();
//     }

//     // 取得所有使用者
//     public List<User> getAllUsers() {
//         return usersList;
//     }

//     @Override
//     public String toString() {
//         StringBuilder sb = new StringBuilder();
//         if (usersList.isEmpty()) {
//             sb.append("目前沒有註冊的使用者");
//         } else {
//             for (User u : usersList) {
//                 sb.append(u.toString()).append("\n");
//             }
//         }
//         return sb.toString();
//     }
// }
public class UserService {
    // 關鍵修改：將 List 改為 Map。Key 是 Email (String)，Value 是 User 物件
    private Map<String, User> usersMap = new HashMap<>();

    public UserService() {
        // 預設一個最高管理員，系統啟動就能登入
        User admin = new User("系統管理員", "admin123", "admin@pet.com", UserRole.ADMIN);
        usersMap.put(admin.getEmail(), admin);
    }

    // 使用者註冊
    public String register(User user) {
        if (user.getMemberName() == null || user.getPassword() == null || user.getEmail() == null) {
            return "註冊失敗：資料不完整";
        }
        
        // 修改：Map 的檢查重複非常簡單，直接看有沒有這個 Key
        if (usersMap.containsKey(user.getEmail())) {
            return "註冊失敗：帳號已存在";
        }
        
        // 修改：使用 put 存入
        usersMap.put(user.getEmail(), user);
        return "註冊成功";
    }

    // 使用者登入
    public User login(String email, String password) {
        // 修改：不用跑迴圈了！直接 get 出來
        User u = usersMap.get(email); 
        
        if (u != null && u.getPassword().equals(password)) {
            return u; 
        }
        return null;
    }

    // 新增寵物到使用者
    public String addPetToUser(String userEmail, Pet pet) {
        User u = usersMap.get(userEmail);
        if (u != null) {
            u.addPet(pet);
            return "寵物新增成功";
        }
        return "找不到該使用者";
    }

    // 取得所有使用者 (為了後台管理選單)
    public List<User> getAllUsers() {
        // Map 轉回 List，方便原本的程式碼介接
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public String toString() {
        if (usersMap.isEmpty()) return "目前沒有註冊的使用者";
        
        StringBuilder sb = new StringBuilder();
        for (User u : usersMap.values()) {
            sb.append(u.toString()).append("\n");
        }
        return sb.toString();
    }
}
    



