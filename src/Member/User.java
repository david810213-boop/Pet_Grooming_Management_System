package Member;

import java.util.ArrayList;
import java.util.List;

import PetManagement.Pet;

public class User {
    private String memberName;
    private String birthday;
    private String email;
    private String phone;
    private String password;
    private String memberId;
    private List<Pet>pets = new ArrayList<>(); // 使用者的寵物清單

    
    public User (String memberName,String password,String email){
        this.memberName = memberName;
        this.password = password;
        this.email = email;
        
    }

    public void setName(String memberName) {
        this.memberName = memberName;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
    

    public String getMemberId() {
        return memberId;
    }

    // 添加寵物
    public void addPet(Pet pet) {
    for (Pet p : pets) {
        if (p.getName().equals(pet.getName()) &&
            p.getPetType().equals(pet.getPetType()) &&
            p.getBreed().equals(pet.getBreed())) {
            return; 
        }
    }
    pets.add(pet);
}

    // 取得所有寵物
    public List<Pet> getPets() {
        return pets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("使用者: ").append(memberName)
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

