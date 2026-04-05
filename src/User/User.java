package User;

import java.util.ArrayList;
import java.util.List;

import PetManagement.Pet;

public class User {
    private String name;
    private String birthday;
    private String email;
    private String phone;
    private String password;
    private String memberId;
    private UserRole role;
    private List<Pet>pets = new ArrayList<>(); // 使用者的寵物清單

    
    public User (String name,String password,String email,UserRole role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public void setName(String name) {this.name = name;}
    public void setBirthday(String birthday) {this.birthday = birthday;}
    public void setEmail(String email) {this.email = email;}
    public void setPhone(String phone) {this.phone = phone;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getName() {return name;}
    public String getBirthday() {return birthday;}
    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public String getMemberId() {return memberId;}
    public UserRole getRole() {return role;}

    // 添加寵物
    public void addPet(Pet pet) {
        this.pets.add(pet);
    }

    // 取得所有寵物
    public List<Pet> getPets() {
        return pets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("使用者: ").append(name)
          .append(" | Email: ").append(email)
          .append("\n寵物清單:\n");

        if (pets.isEmpty()) {
            sb.append("  (尚未新增寵物)");
        } else {
            for (Pet pet : pets) {
                sb.append("  - ").append(pet.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}

