package minesweeper;

import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private static int[][] field;
    private static int[][] fieldExplore;
    private static final String START_PROMPT = "How many mines do you want on the field?";
    private static final String FIRST_FIELD_STRING = " |123456789|";
    private static final String SECOND_FIELD_STRING = "-|---------|";
    private static final String USER_INPUT = "Set/unset mines marks or claim a cell as free:";
    private static final String NUMBER_HERE = "There is a number here!";
    private static final String WIN_MESSAGE = "Congratulations! You found all the mines!";
    private static final String  LOSE_MESSAGE = "You stepped on a mine and failed!";

    public static boolean continueOfGame = true;



    public Game(int x, int y) {

        field = new int[x + 2][y + 2];
        fieldExplore = new int[x + 2][y + 2];
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int mineCount = askGamerMineCount(scanner);
        generateField(mineCount);
        printField();
        while (continueOfGame) {
            System.out.println(USER_INPUT);
            userInputAndCheck(scanner);
        }
    }

    private void userInputAndCheck(Scanner scanner) {
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        String command = scanner.next();

        if (Objects.equals(command, "mine")) {
            switch (field[y][x]) {
                case 1: {
                    field[y][x] = 2;
                    fieldExplore[y][x] = 1;
                    if (checkAllMineFound()) {
                        continueOfGame = false;
                        System.out.println(WIN_MESSAGE);
                    } else {
                        printField();
                        return;
                    }
                }
                case 0: {
                    field[y][x] = -1;
                    fieldExplore[y][x] = 1;
                    printField();
                    return;
                }
                case -1: {
                    field[y][x] = 0;
                    fieldExplore[y][x] = 0;
                    printField();
                    return;
                }
                case 2: {
                    field[y][x] = 1;
                    fieldExplore[y][x] = 0;
                    printField();
                    return;
                }
            }


        } else if (Objects.equals(command, "free")) {
            switch (field[y][x]) {
                case 1: {
                    continueOfGame = false;
                    printField();
                    System.out.println(LOSE_MESSAGE);
                    return;
                }
                case 0: {
                    fieldExplore[x][y] = 1;
                    recalculateFieldExplore(x,y);
                    printField();
                    return;
                }
            }
        }
    }

    private void recalculateFieldExplore(int x, int y) {
        for (int i  = 1; i < field.length - 1; i++) {
            for (int j = 1; j < field[0].length - 1; j++) {
                if (field[j][i] < 1) {
                    fieldExplore[j][i] = 1;
                    field[j][i] = 0;
                }
            }
        }
    }

    private boolean checkAllMineFound() {
        for (int x  = 1; x < field.length - 1; x++) {
            for (int y  = 1; y < field[0].length - 1; y++) {
                if (field[x][y] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int askGamerMineCount(Scanner scanner) {
        System.out.println(START_PROMPT);
        return scanner.nextInt();
    }

    private static void printField() {
        System.out.println(FIRST_FIELD_STRING);
        System.out.println(SECOND_FIELD_STRING);
        for (int x  = 1; x < field.length - 1; x++) {
            System.out.printf("%d|",x);
            for (int y  = 1; y < field[0].length - 1; y++) {
                if (fieldExplore[x][y] == 1) {
                    if (field[x][y] == 0) {
                        int mineCount = calculateMineCountAroundCell(x,y);
                        if (mineCount > 0) {
                            System.out.printf("%d",mineCount);
                        } else {
                            System.out.print("/");
                        }
                    } else if (field[x][y] == 1) {
                        System.out.print(".");
                    } else if (field[x][y] == 2) {
                        System.out.print("*");
                    } else if (field[x][y] < 0) {
                        System.out.print("*");
                    }
                } else {
                    System.out.print(".");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println(SECOND_FIELD_STRING);
    }

    private static int calculateMineCountAroundCell(int x, int y) {
        int mineCount = 0;
        for (int i  = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if (x == i && y == j ) {
                    continue;
                }
                if (field[i][j] > 0) {
                    mineCount++;
                }
            }
        }
        return mineCount;
    }

    private static void generateField(int mineCount) {
        Random rnd = new Random();
        while (mineCount > 0) {
            int XcellWithMine = 1 + rnd.nextInt(field.length - 2);
            int YcellWithMine = 1 + rnd.nextInt(field.length - 2);
            //System.out.printf("x: %d y: %d\n",XcellWithMine,YcellWithMine);
            if (field[XcellWithMine][YcellWithMine] == 0) {
                field[XcellWithMine][YcellWithMine] = 1;
                mineCount--;
            }
        }
    }
}
