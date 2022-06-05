import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class maze implements ActionListener {
    class Cell {
        int x, y;
        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public static void main(String[] args) {
        new maze();
    }

    JFrame window;
    JPanel panel;
    Rectangle player;
    MyKL mainKL = new MyKL();
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

    Stack<Cell> stack = new Stack<Cell>();

    maze() {
        player = new Rectangle(0, 0, boxW, boxH);
        window = new JFrame("Touch the yellow square");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new DrawingPanel();
        window.add(panel); // the panel will control the size
        window.pack(); // therefore we need pack
        window.setVisible(true);
        // generate winnning tile
        board[winX][winY] = win;
        Cell startCell = new Cell(winX, winY);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = wall;
            }
        }
        Timer timer = new Timer(50, this);
        timer.start();
        generateWalls(startCell);
        board[0][0] = path;
        board[1][0] = path;

        
    }

    void generateWalls(Cell startCell) {

        Cell cell = new Cell(startCell.x, startCell.y);
        stack.push(cell);
        //set the current tile as path
        board[startCell.x][startCell.y] = path;
        List<String> directions = Arrays.asList("N", "E", "S", "W");
        Collections.shuffle(directions);
        // choose the direction we're going to move
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
            //check validity of the tile we're going to move to
            //in the array, as well as contains a wall
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
        //if no valid moves, start backtracking
        stack.pop();
        generateWalls(stack.pop());
    }

    private class DrawingPanel extends JPanel {
        DrawingPanel() {
            this.setBackground(Color.WHITE);
            this.setPreferredSize(new Dimension(panW, panH));
            this.addKeyListener(mainKL);
            this.setFocusable(true); 
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // draw winning tile
            g.setColor(Color.yellow);
            g.fillRect(winX * boxH, winY * boxH, boxW, boxH);

            // draw walls
            g.setColor(Color.gray);
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == wall) {
                        g.fillRect(i * boxW, j * boxH, boxW, boxH);
                    }
                }
            }
            //draw player
            g.setColor(Color.RED);
            g.fillRect(player.x, player.y, player.width, player.height);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // get keys and move player
        if (mainKL.isKeyDown('A') && player.x > 0) {
            if (board[px - 1][py] == wall) {
            } else {
                px -= 1;
                player.x -= boxW;
            }
        }
        if (mainKL.isKeyDown('D') && player.x + player.width < panW) {
            if (board[px + 1][py] == wall) {
            } else {
                px += 1;
                player.x += boxW;
            }
        }
        if (mainKL.isKeyDown('W') && player.y > 0) {
            if (board[px][py - 1] == wall) {
            } else {
                player.y -= boxH;
                py -= 1;
            }
        }
        if (mainKL.isKeyDown('S') && player.y + player.height < panH) {
            if (board[px][py + 1] == wall) {
            } else {
                player.y += boxH;
                py += 1;
            }
        }
        if (board[px][py] == 3) {
            window.setTitle("W");
            panel.setFocusable(false);
        }
        panel.repaint();
    }

    class MyKL implements KeyListener {
        private boolean[] keys = new boolean[256];

        boolean isKeyDown(int n) {
            return keys[n];
        }

        @Override
        public void keyPressed(KeyEvent e) {
            keys[e.getKeyCode()] = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keys[e.getKeyCode()] = false;
        }

        @Override
        public void keyTyped(KeyEvent e) {
        } // slow!!!
    }

}
