

public class Ship {
    int Size;
    String Name;
    int[][] Coordinates;
    boolean Sinked =false;
    public Ship(int size, String name) {
        Size = size;
        Name = name;
        Coordinates = new int[2][2];
    }


}
