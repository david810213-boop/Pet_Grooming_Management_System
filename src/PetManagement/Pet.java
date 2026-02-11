package PetManagement;

public class Pet {
    private String name;         // 新增
    private String petType;        // CAT, DOG (原 type)
    private String breed;        // 品種 (新增)
    private double weight;       // 體重 (利用原 length)
    private int age;             // 年齡 (新增)
        //會員ID (新增)
    
    
    public Pet(String name, String petType, String breed, double weight, int age) {
        this.name = name;
        this.petType = petType;
        this.breed = breed;
        this.weight = weight;
        this.age = age;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPetType() {
        return petType;
    }


    public void setPetType(String petType) {
        this.petType = petType;
    }


    public String getBreed() {
        return breed;
    }


    public void setBreed(String breed) {
        this.breed = breed;
    }


    public double getWeight() {
        return weight;
    }


    public void setWeight(double weight) {
        this.weight = weight;
    }


    public int getAge() {
        return age;
    }


    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("寵物名稱: %s | 種類: %s | 品種: %s | 體重: %.1f | 年齡: %d",
                name, petType, breed, weight, age);
    }


}
