public class Item implements Comparable<Item>{
    public String itemName;
    public int itemSpace;

    public Item(String name, int space){
        this.itemName=name;
        this.itemSpace=space;
    }
    public int compareTo(Item o) {
        if(this.itemSpace == o.itemSpace){
            return this.itemName.charAt(0) > o.itemName.charAt(0) ? 1 : -1;
        } else {
            return o.itemSpace - this.itemSpace;
        }
    }
}
