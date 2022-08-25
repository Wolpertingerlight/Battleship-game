

import java.util.Arrays;
import java.util.Scanner;

public class Player{
    int Number;
    String[][] battleField;
    String[][] enemyMap;
    Ship[] Ships;

    public Player(int Number) {
        this.Number = Number;
        this.battleField = emptyMap();
        this.enemyMap = emptyMap();

        this.Ships = new Ship[5];
        this.Ships[0] = new Ship(5, "Aircraft Carrier");
        this.Ships[1] = new Ship(4, "Battleship");
        this.Ships[2] = new Ship(3, "Submarine");
        this.Ships[3] = new Ship(3,"Cruiser");
        this.Ships[4] = new Ship(2, "Destroyer");
    }

    String [][] emptyMap(){
        String[][] seaField = new String[11][11];
        String[] Words={"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        seaField[0][0]=" ";
        for(int i = 1; i < seaField.length; i++ ){
            seaField[0][i]= String.valueOf(i);
            seaField[i][0]= Words[i-1];
        }

        for(int i = 1; i < seaField.length; i++){
            for(int j = 1; j < seaField.length; j++){
                seaField[i][j]="~";
            }
        }

        return seaField;
    }

    int[] getCoordinates(String str){
        Character[] Words={'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        int[] Coordinate = new int[2];
        Coordinate[0]= Arrays.asList(Words).indexOf(str.charAt(0)) < 0 ? -1 : Arrays.asList(Words).indexOf(str.charAt(0))+1;
        Coordinate[1]= Integer.parseInt(str.substring(1));
        return Coordinate;
    }


    boolean readCoordinates(String[] str, Ship ship){

        int[] start = getCoordinates(str[0]);
        int[] end = getCoordinates(str[1]);

        int x1 = start[0]  < end[0] ? start[0] : end[0],
                x2 = end[0]  > start[0] ? end[0] : start[0],
                y1 = start[1]  < end[1] ? start[1] : end[1],
                y2 = end[1] > start[1] ? end[1] : start[1];


        if((x1==x2 && y1!=y2) || (x1!=x2 && y1==y2)){
            if(y2-y1 ==ship.Size-1 || x2-x1 ==ship.Size-1){

                int yBord = y2 == battleField.length-1 ? y2 : y2 +1;
                int xBord = x2 == battleField.length-1 ? x2 : x2 +1;
                for(int i = y1-1; i<=yBord;i++)
                    for(int j = x1-1; j<=xBord;j++)
                        if(this.battleField[j][i].equals("O")){
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            return false;
                        }

                ship.Coordinates[0][0]= x1;
                ship.Coordinates[0][1]= y1;
                ship.Coordinates[1][0]= x2;
                ship.Coordinates[1][1]= y2;
                return  true;
            }
            else System.out.println("Error! Wrong length of the" + ship.Name + "! Try again:");

        }
        else System.out.println("Error! Wrong ship location! Try again:");
        return false;
    }

    void addShip(Ship ship){
        Scanner scanner = new Scanner(System.in);

        while(true){

            if (readCoordinates(scanner.nextLine().split("\\s+"), ship)) {
                if(ship.Coordinates[0][0]==ship.Coordinates[1][0])
                    for (int i = ship.Coordinates[0][1]; i<=ship.Coordinates[1][1]; i++)
                        this.battleField[ship.Coordinates[0][0]][i]="O";
                else
                    for (int i = ship.Coordinates[0][0]; i<=ship.Coordinates[1][0]; i++)
                        this.battleField[i][ship.Coordinates[0][1]]="O";

                return;
            }
        }
    }

    void createMap(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player " + this.Number + ", place your ships on the game field");
        Main.printMap(this.battleField);
        for( int i = 0; i<this.Ships.length; i++){
            System.out.printf("Enter the coordinates of the %s (%d cells):", this.Ships[i].Name, this.Ships[i].Size);
            addShip(this.Ships[i]);
            Main.printMap(this.battleField);
        }
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        Main.clearScreen();
    }

    boolean checkShot(int[] Shot, Player enemy){

        for(int i = 0; i < enemy.Ships.length; i++) {
            if((Shot[0] >= enemy.Ships[i].Coordinates[0][0] && Shot[0] <= enemy.Ships[i].Coordinates[1][0]) &&
                    Shot[1] >= enemy.Ships[i].Coordinates[0][1] && Shot[1] <= enemy.Ships[i].Coordinates[1][1]){

                for(int a = enemy.Ships[i].Coordinates[0][0]; a <=enemy.Ships[i].Coordinates[1][0]; a++){
                    for (int b = enemy.Ships[i].Coordinates[0][1]; b <= enemy.Ships[i].Coordinates[1][1]; b++){

                        if(enemy.battleField[a][b].equals("O"))
                            return false;
                    }
                }

                enemy.Ships[i].Sinked = true;
            }
        }

        return true;
    }

    boolean checkWinner(Player player){
        for(int i = 0; i < player.Ships.length; i++)
            if(player.Ships[i].Sinked == false)
                return false;
        return true;
    }

    boolean checkWinner(){
        for(int i = 0; i < this.Ships.length; i++)
            if(this.Ships[i].Sinked == false)
                return false;
        return true;
    }

    void takeShot(Player enemy){
        Scanner scanner = new Scanner(System.in);

        Main.printMap(this.enemyMap);
        System.out.println("---------------------");
        Main.printMap(this.battleField);
        System.out.println("Player" + this.Number + ", it's your turn:");

        while(true){
            try{
                int[] Coordinate = getCoordinates(scanner.nextLine());

                if( enemy.battleField[Coordinate[0]][Coordinate[1]].equals("O") || enemy.battleField[Coordinate[0]][Coordinate[1]].equals("X")){
                    enemy.battleField[Coordinate[0]][Coordinate[1]]="X";
                    this.enemyMap[Coordinate[0]][Coordinate[1]]="X";

                    if(checkShot(Coordinate, enemy)==true){
                        if(checkWinner(enemy)==true){
                            System.out.println("You sank the last ship. You won. Congratulations!");
                            return;
                        }

                        System.out.println("You sank a ship! Specify a new target:");
                    }
                    else
                        System.out.println("You hit a ship!");
                    System.out.println("Press Enter and pass the move to another player");
                    scanner.nextLine();
                    //Main.printMap(this.battleField);
                    return;
                }

                else if( enemy.battleField[Coordinate[0]][Coordinate[1]].equals("~") || enemy.battleField[Coordinate[0]][Coordinate[1]].equals("M")){
                    enemy.battleField[Coordinate[0]][Coordinate[1]]="M";
                    this.enemyMap[Coordinate[0]][Coordinate[1]]="M";

                    System.out.println("You missed!");
                    System.out.println("Press Enter and pass the move to another player");
                    scanner.nextLine();
                    return;
                }
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                break;
            }
            return;
        }

    }
}

