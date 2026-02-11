package Member;

import java.util.ArrayList;
import java.util.List;

import PetManagement.Pet;

public class UserService {
    private List<User> usersList = new ArrayList<>();

    // 使用者註冊
    public String register(User user) {
        if (user.getMemberName() == null || user.getPassword() == null || user.getEmail() == null) {
            return "註冊失敗：資料不完整";
        }
        // 檢查是否重複
        for (User u : usersList) {
            if (u.getEmail().equals(user.getEmail())) {
                return "註冊失敗：帳號已存在";
            }
        }
        usersList.add(user);
        return "註冊成功";
    }
    // 使用者登入
    public User login(String email, String password) {
        for (User u : usersList) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                return u; // 登入成功，回傳 User 物件
            }
        }
        return null; // 登入失敗
    }
    // 新增寵物到使用者
    public String addPetToUser(String userEmail, Pet pet) {
        for (User u : usersList) {
            if (u.getEmail().equals(userEmail)) {
                u.addPet(pet);
                return "寵物新增成功";
            }
        }
        return "找不到該使用者";
    }


    // 取得使用者的寵物清單
    public List<Pet> getPetsByEmail(String userEmail) {
        for (User u : usersList) {
            if (u.getEmail().equals(userEmail)) {
                return u.getPets();
            }
        }
        return new ArrayList<>();
    }

    // 取得所有使用者
    public List<User> getAllUsers() {
        return usersList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (usersList.isEmpty()) {
            sb.append("目前沒有註冊的使用者");
        } else {
            for (User u : usersList) {
                sb.append(u.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
    



