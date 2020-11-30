package org.example;

public class Simulation {
    public int width;
    public int height;
    int[][] board;

    public Simulation(int width, int height) {
        this.width = width;
        this.height = height;

        this.board = new int[width][height];
    }

    public void printBoard() {
        System.out.print("-----\n");
        for (int y = 0; y < height; y++) {
            String line = "|";
            for (int x = 0; x < width; x++) {
                if(this.board[x][y] == 0) {
                    line += ".";
                }else {
                    line += "*";
                }
            }
            line += "|";
            System.out.println(line);
        }
        System.out.print("-----\n");
    }

    public void nextGeneration() {
        int[][] tempBoard = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int aliveNeighbours = countAliveNeighbours(x ,y);

                if(this.board[x][y]== 1) {
                    if(aliveNeighbours < 2) {
                        tempBoard[x][y] = 0;
                    }else if(aliveNeighbours == 2 || aliveNeighbours == 3){
                        tempBoard[x][y] = 1;
                    }else if(aliveNeighbours > 3) {
                        tempBoard[x][y] = 0;
                    }
                }else {
                    if(aliveNeighbours == 3) {
                        tempBoard[x][y] = 1;
                    }
                }
            }
        }
        this.board = tempBoard;
    }

    public int isAlive(int x, int y) {
        if(x < 0 || x >= width) {
            return 0;
        }
        if(y < 0 || y >= height) {
            return 0;
        }
        return this.board[x][y];
    }

    public void setAlive(int x, int y) {
        this.board[x][y] = 1;
    }

    public void setDead(int x, int y) {
        this.board[x][y] = 0;
    }

    public int countAliveNeighbours(int x, int y) {
        int count = 0;
        count += isAlive(x-1, y-1);
        count += isAlive(x, y-1);
        count += isAlive(x+1, y-1);
        count += isAlive(x-1, y);
        count += isAlive(x+1, y);
        count += isAlive(x-1, y+1);
        count += isAlive(x, y+1);
        count += isAlive(x+1, y+1);
        return count;
    }

    public static void main(String[] args) {
        Simulation simulation = new Simulation(8,8);

        simulation.setAlive(2,2);
        simulation.setAlive(3,2);
        simulation.setAlive(4,2);

        simulation.printBoard();
        simulation.nextGeneration();
        simulation.printBoard();
    }

}

