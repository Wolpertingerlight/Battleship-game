public class Main {

    public static void printMap(String[][] BattleField){
        for(int i = 0; i < BattleField.length; i++){
            for(int j = 0; j < BattleField.length; j++){
                System.out.print(BattleField[i][j]+" ");
            }
            System.out.println();
        }
    }

    public static void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void startGame(){
        Player player1 = new Player(1);
        Player player2 = new Player(1);

        player1.createMap();
        player2.createMap();

        while(!player1.checkWinner() || !player2.checkWinner()){

            player1.takeShot(player2);
            player2.takeShot(player1);
        }
    }

    public static void main(String[] args) {
        startGame();
    }
}
