import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class CarRacingGame extends JPanel implements ActionListener, KeyListener {

    // Window size
    private final int WIDTH = 400;
    private final int HEIGHT = 700;

    // Player car
    private int carX = 170;
    private int carY = 550;
    private final int carWidth = 60;
    private final int carHeight = 100;

    // Road animation
    private int roadOffset = 0;

    // Enemies
    private class EnemyCar {
        int x, y;
        EnemyCar(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private ArrayList<EnemyCar> enemies = new ArrayList<>();
    private Random random = new Random();

    private Timer timer;
    private boolean gameOver = false;
    private int score = 0;

    public CarRacingGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawRoad(g2);
        drawPlayerCar(g2);
        drawEnemies(g2);
        drawScore(g2);

        if (gameOver) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString("GAME OVER", 90, 350);
        }
    }

    private void drawRoad(Graphics2D g2) {
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(50, 0, 300, HEIGHT);

        g2.setColor(Color.WHITE);
        for (int i = 0; i < HEIGHT; i += 40) {
            g2.fillRect(195, i + roadOffset, 10, 20);
        }
    }

    private void drawPlayerCar(Graphics2D g2) {
        g2.setColor(Color.CYAN);
        g2.fillRoundRect(carX, carY, carWidth, carHeight, 20, 20);
    }

    private void drawEnemies(Graphics2D g2) {
        g2.setColor(Color.RED);
        for (EnemyCar enemy : enemies) {
            g2.fillRoundRect(enemy.x, enemy.y, carWidth, carHeight, 20, 20);
        }
    }

    private void drawScore(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Score: " + score, 20, 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            updateGame();
        }
        repaint();
    }

    private void updateGame() {
        roadOffset += 5;
        if (roadOffset > 40) {
            roadOffset = 0;
        }

        // Spawn enemies
        if (random.nextInt(100) < 3) {
            int laneX = 70 + random.nextInt(3) * 100;
            enemies.add(new EnemyCar(laneX, -100));
        }

        // Move enemies
        for (int i = 0; i < enemies.size(); i++) {
            EnemyCar enemy = enemies.get(i);
            enemy.y += 6;

            if (enemy.y > HEIGHT) {
                enemies.remove(i);
                score++;
            }

            // Collision detection
            Rectangle playerRect = new Rectangle(carX, carY, carWidth, carHeight);
            Rectangle enemyRect = new Rectangle(enemy.x, enemy.y, carWidth, carHeight);

            if (playerRect.intersects(enemyRect)) {
                gameOver = true;
                timer.stop();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && carX > 60) {
                carX -= 20;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && carX < 280) {
                carX += 20;
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Racing Game");
        CarRacingGame game = new CarRacingGame();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
