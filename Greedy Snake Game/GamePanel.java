import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
    private Snake snake;
    private Food food;
    private Timer timer;
    private Timer clockTimer;
    private boolean isGameOver;
    private boolean isPaused;
    private int secondsPlayed;
    private int points;
    private Level currentLevel;


    public GamePanel() {
        // 初始化游戏元素
        snake = new Snake();
        food = new Food();
        points = 0;
        currentLevel = checkLevel(points);
        food.generateNewFood(snake.getBody()); // generate initial food

        // initiate timer to control speed
        timer = new Timer(currentLevel.getSpeed(), this);
        timer.start();
        // Clock timer updates every second (1000ms)
        clockTimer = new Timer(1000, new ClockActionListener());
        clockTimer.start();

        // initial panels
        setBackground(Color.PINK); // pink background
        setPreferredSize(new Dimension(
                GameConstants.WINDOW_WIDTH,
                GameConstants.WINDOW_HEIGHT
        ));
        setFocusable(true);
        addKeyListener(this);

        isGameOver = false;
        isPaused = false;
        secondsPlayed = 0;
    }
    private class ClockActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Only increment time when game is active (not paused and not over)
            if (!isPaused && !isGameOver) {
                secondsPlayed++;
            }
        }
    }
    // Convert seconds to MM:SS format
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // paint a snake
        g.setColor(Color.GREEN);
        for (Position p : snake.getBody()) {
            // paint the body of snake with 1 pixel gap to make its body clearer
            g.fillRect(
                    p.getX() * GameConstants.SIZE,
                    p.getY() * GameConstants.SIZE,
                    GameConstants.SIZE - 1,
                    GameConstants.SIZE - 1
            );
        }

        // paint one food
        g.setColor(Color.RED); // food in red
        Position foodPos = food.getPosition();
        g.fillRect(
                foodPos.getX() * GameConstants.SIZE,
                foodPos.getY() * GameConstants.SIZE,
                GameConstants.SIZE - 1,
                GameConstants.SIZE - 1
        );

        // Draw play time in top-left corner
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Time: " + formatTime(secondsPlayed), 10, 20);

        // Display points under the timer
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Points: " + points, 10, 40);
        g.drawString("Level: " + currentLevel.getLevelNumber(),
                GameConstants.WINDOW_WIDTH - 100, 20);

        // display info when game over
        if (isGameOver) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            // get position of words to make it in middle
            String message = "Game Over. Press 'R' to Restart.";
            FontMetrics metrics = g.getFontMetrics();
            int x = (GameConstants.WINDOW_WIDTH - metrics.stringWidth(message)) / 2;
            int y = (GameConstants.WINDOW_HEIGHT - metrics.getHeight()) / 2 + metrics.getAscent();

            g.drawString(message, x, y);
        }

        else if (isPaused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);

            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 24));

            String message = "Game Paused.";
            FontMetrics metrics = g.getFontMetrics();
            int x = (GameConstants.WINDOW_WIDTH - metrics.stringWidth(message)) / 2;
            int y = (GameConstants.WINDOW_HEIGHT - metrics.getHeight()) / 2 + metrics.getAscent();

            g.drawString(message, x, y);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            String subMessage = "Press 'P' to resume.";
            int subX = (GameConstants.WINDOW_WIDTH - metrics.stringWidth(subMessage)) / 2;
            int subY = y + 30;
            g.drawString(subMessage, subX, subY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver && !isPaused) {
            snake.move(); // move the snake
            checkCollisions();
            checkFoodEaten();
        }
        repaint();
    }
    private void checkFoodEaten() {
        if (snake.getHead().equals(food.getPosition())) {
            // when eat a food, body grows
            snake.grow();
            points++;
            currentLevel = checkLevel(points);
            // generate a new one
            food.generateNewFood(snake.getBody());
        } else {
            // when not eat, remove 1 tail to make total length the same and step 1 box forward
            snake.removeTail();
        }
    }

    private void checkCollisions() {
        Position head = snake.getHead();

        // whether hits the edge
        if (head.getX() < 0 || head.getX() >= GameConstants.WIDTH ||
                head.getY() < 0 || head.getY() >= GameConstants.HEIGHT) {
            isGameOver = true;
            timer.stop();
        }

        // whether hits itself
        for (int i = 1; i < snake.getBody().size(); i++) {
            if (head.equals(snake.getBody().get(i))) {
                isGameOver = true;
                timer.stop();
                break;
            }
        }
    }
    public Level checkLevel(int points){
        if (points >= 0 && points <= 5){
            return setLevelNumber(0);
        }
        else if (points > 5 && points <= 10){
            return setLevelNumber(1);
        }
        else if (points > 10 && points <= 15){
            return setLevelNumber(2);
        }
        else{
            return setLevelNumber(3);
        }
    }

    public Level setLevelNumber(int levelNumber) {
        try {
            Level newLevel = Level.getLevelByNumber(levelNumber);
            currentLevel = newLevel;
            // 更新游戏计时器的速度（核心：等级决定蛇的移动速度）
            System.out.println(GameConstants.LEVEL_CHANGED_MSG + currentLevel.getLevelNumber());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid level number:" + levelNumber);
        }
        return currentLevel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // directions
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            snake.setDirection(GameConstants.UP);
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            snake.setDirection(GameConstants.RIGHT);
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            snake.setDirection(GameConstants.DOWN);
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            snake.setDirection(GameConstants.LEFT);
        }

        // press r to restart
        if (key == KeyEvent.VK_R && isGameOver) {
            restartGame();
        }

        // press p to pause
        if (key == KeyEvent.VK_P) {
            pauseGame();
        }

        // press esc to exit
        if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    private void restartGame() {
        snake = new Snake();
        food.generateNewFood(snake.getBody());
        isGameOver = false;
        timer.start();
    }

    private void pauseGame() {
        isPaused = !isPaused;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
