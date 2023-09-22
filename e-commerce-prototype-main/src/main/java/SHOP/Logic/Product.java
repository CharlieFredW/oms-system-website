package SHOP.Logic;

public class Product implements Comparable {
    private int stock;
    private String ID, title, description, color, tag, category, imageDirectory;
    private double price;

    public Product(String ID,String category,double price) {
        this.ID = ID;
        this.category = category;
        this.price = price;
    }

    public Product(String ID,String category,double price,String title,String description,String color,String tag, String imageDirectory, int stock) {
        this.ID = ID;
        this.category = category;
        this.price = price;
        this.title = title;
        this.description = description;
        this.color = color;
        this.tag = tag;
        this.imageDirectory = imageDirectory;
        this.stock = stock;
    }

    public Product(String ID,String category,String title){
        this.ID = ID;
        this.category = category;
        this.title = title;
    }

    public String getID() {
        return ID;
    }
    public String getTitle() {
        return title;
    }
    public String getColor() {
        return color;
    }
    public String getDescription() {
        return description;
    }
    public String getTag() {
        return tag;
    }
    public String getImageDirectory() {
        return imageDirectory;
    }

    @Override
    public String toString() {
        return "ID=" + ID +  ",type=" + category + ",price=" + price;
    }

    public double getPrice() {
        return  price;
    }

    public String getCategory() {
        return category;
    }
    public int getStock(){
        return stock;
    }

    @Override
    public int compareTo(Object o) {
        //int idComp = this.ID.compareTo(((Product) o).getID());
        //return idComp;
        return 0;
    }
}
