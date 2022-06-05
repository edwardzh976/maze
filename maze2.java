import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class maze2 implements ActionListener {
    class Cell {
        int x, y;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        new maze2();
    }

    JFrame window;
    JPanel panel;
    static int panW = 600, panH = 600;
    final static int SIZE = 20;
    int board[][] = new int[SIZE][SIZE];
    final static int path = 1, wall = 2, win = 3;
    // player position
    int px, py;
    int boxW = (int) ((panW / SIZE));
    int boxH = (int) ((panH / SIZE)); // size of one box
    // win tile
    int winX = (int) (Math.random() * (10) + 5);
    int winY = (int) (Math.random() * (10) + 5);

    int i = 0, j = 0;
    Stack<Cell> stack = new Stack<Cell>();
    Timer timer = new Timer(500, this);
    //Cell startCell = new Cell(winX, winY);
    maze2() {
        window = new JFrame("Touch the yellow square");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new DrawingPanel();
        window.add(panel); // the panel will control the size
        window.pack(); // therefore we need pack
        window.setVisible(true);

        board[winX][winY] = win;
        
        //fill everything with walls
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = wall;
            }
        }
        timer.start();
        Cell startCell = new Cell(winX, winY);
        generateWalls(startCell);

    }

    void generateWalls(Cell startCell) {
        panel.repaint();
        // push the current cell to the stack
        Cell cell = new Cell(startCell.x, startCell.y);
        stack.push(cell);
        // set the current cell as a path
        board[startCell.x][startCell.y] = path;

        // randomly choose the direction we're going to move
        List<String> directions = Arrays.asList("N", "E", "S", "W");
        Collections.shuffle(directions);
        
        for (String s : directions) {
            int ny = 0, nx = 0;
            if (s.equals("N")) {
                nx = startCell.x;
                ny = startCell.y - 2;
            }
            if (s.equals("E")) {
                nx = startCell.x + 2;
                ny = startCell.y;
            }
            if (s.equals("S")) {
                nx = startCell.x;
                ny = startCell.y + 2;
            }
            if (s.equals("W")) {
                nx = startCell.x - 2;
                ny = startCell.y;
            }
            // check validity of the tile we're going to move to
            // check if it's within in the boundaries, as well as contains a wall
            if (ny >= 0 && ny < SIZE && nx >= 0 && nx < SIZE && board[nx][ny] == 2) {
                if (startCell.x > nx) {
                    board[startCell.x - 1][ny] = path;
                }
                if (startCell.x < nx) {
                    board[startCell.x + 1][ny] = path;
                }
                if (startCell.y > ny) {
                    board[nx][startCell.y - 1] = path;
                }
                if (startCell.y < ny) {
                    board[nx][startCell.y + 1] = path;
                }
                startCell.x = nx;
                startCell.y = ny;
                // if valid move there and repeat
                generateWalls(startCell);
            }
        }
        // if we return to starting position it means the maze has been created
        if (stack.peek().x == winX && stack.peek().y == winY) {
            return;
        }
        // if no valid moves, start backtracking
        stack.pop();
        generateWalls(stack.pop());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        i++;
        if (i == SIZE){
            j++;
            i = 0;
        }
        
        
    }
    class DrawingPanel extends JPanel {
        DrawingPanel() {
            this.setBackground(Color.WHITE);
            this.setPreferredSize(new Dimension(panW, panH));
            this.setFocusable(true);
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // draw winning tile
            /*g.setColor(Color.yellow);
            g.fillRect(winX * boxH, winY * boxH, boxW, boxH);*/

            // draw walls
            g.setColor(Color.gray);
            
            /*if(board[i][j] == wall){
                g.fillRect(i * boxW, j * boxH, boxW, boxH);
            }*/
            for(int x = 0; x < j; x++){
                for(int y = 0; y < i; y++){
                    g.fillRect(i * boxW, j * boxH, boxW, boxH);
                }
            }
            panel.repaint();
            

            /*for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == wall) {
                        g.fillRect(i * boxW, j * boxH, boxW, boxH);
                    }
                }
            }*/
        }
    }

    

}
