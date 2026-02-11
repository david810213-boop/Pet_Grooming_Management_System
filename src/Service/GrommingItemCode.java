package Service;

public class GrommingItemCode {
    public enum Item {
    GS001("指甲修剪磨圓", 200),
    GS002("剃腳底屁股毛", 150),
    GS003("擠肛門腺", 180),
    GS004("耳道清潔", 100),
    GS005("手工吹整毛髮", 300),
    GS006("腳緣修剪", 200),
    GS007("臉部精緻修容", 350),
    GS008("舒壓按摩", 400),
    GS009("天然低敏結構式洗浴", 500),
    GS010("護膚潤澤毛髮", 280),
    GS011("毛鱗修復液", 320),
    GS012("牙齒清潔", 200);

    private final String description;
    private final int price;

        Item(String description, int price) {
            this.description = description;
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public int getPrice() {
            return price;
        }
        
        @Override
        public String toString() {
            return name() + " - " + description + " ($" + price + ")";
        }
    }
}
