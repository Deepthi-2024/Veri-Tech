import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {
    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 30;
    private static final int DELAY = 150;

    private JPanel gamePanel;
    private LinkedList<Point> snake;
    private Point food;
    private int direction; // 0: up, 1: right, 2: down, 3: left
    private int score;
    private boolean gameOver = false;

    private JLabel scoreLabel;
    private JLabel gameOverLabel;
    private JButton playAgainButton;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);

        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBorder(new EmptyBorder(10,10,10,10));
        add(gamePanel);

        initGame();

        Timer timer = new Timer(DELAY, this);
        timer.start();

        setFocusable(true);
        addKeyListener(this);

        score = 0; // Initialize score
    }

    private void initGame() {
        snake = new LinkedList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        generateFood();

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setForeground(Color.GREEN);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gamePanel.add(scoreLabel, BorderLayout.NORTH);

        gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setFont(new Font("Orpheus Italic", Font.ITALIC, 50));
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameOverLabel.setVerticalAlignment(SwingConstants.CENTER);
        gameOverLabel.setVisible(false);
        gamePanel.add(gameOverLabel, BorderLayout.CENTER);

        playAgainButton = new JButton("Play Again");
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setBackground(Color.BLACK);
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 20));
        playAgainButton.addActionListener(e -> resetGame());
        playAgainButton.setVisible(false);
        gamePanel.add(playAgainButton, BorderLayout.SOUTH);

        direction = 1;

        
    }

    private void generateFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    private void move() {
        if (gameOver) {
            return; // If game over, do not proceed with the movement logic
        }

        Point head = snake.getFirst();
        Point newHead;

        switch (direction) {
            case 0: // up
                newHead = new Point(head.x, Math.max(0, head.y - 1));
                break;
            case 1: // right
                newHead = new Point(Math.min(GRID_SIZE - 1, head.x + 1), head.y);
                break;
            case 2: // down
                newHead = new Point(head.x, Math.min(GRID_SIZE - 1, head.y + 1));
                break;
            case 3: // left
                newHead = new Point(Math.max(0, head.x - 1), head.y);
                break;
            default:
                return;
        }

        if (newHead.equals(food)) {
            snake.addFirst(newHead);
            generateFood();
            score++; // Increase score when the snake eats food
            updateScoreLabel();
        } else {
            snake.addFirst(newHead);
            snake.removeLast();
        }

        // Check for collisions with self or borders
        if ((snake.subList(1, snake.size()).contains(newHead) || isCollisionWithBorders(newHead))) {
            gameOver();
        }
    }

    private boolean isCollisionWithBorders(Point point) {
        return point.x < 0 || point.x >= GRID_SIZE || point.y < 0 || point.y >= GRID_SIZE;
    }

    private void gameOver() {
        gameOverLabel.setVisible(true);
        playAgainButton.setVisible(true);
        gameOver = true; // Set game over flag
    }

    private void resetGame() {
        gameOverLabel.setVisible(false);
        playAgainButton.setVisible(false);

        snake.clear();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        generateFood();
        direction = 1;
        score = 0;
        updateScoreLabel();
        gameOver = false; // Reset game over flag
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        gamePanel.setBackground(Color.BLACK);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * CELL_SIZE, point.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_UP) && (direction != 2)) {
            direction = 0;
        } else if ((key == KeyEvent.VK_RIGHT) && (direction != 3)) {
            direction = 1;
        } else if ((key == KeyEvent.VK_DOWN) && (direction != 0)) {
            direction = 2;
        } else if ((key == KeyEvent.VK_LEFT) && (direction != 1)) {
            direction = 3;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame snakeGame = new SnakeGame();
            snakeGame.setVisible(true);
        });
    }
}
