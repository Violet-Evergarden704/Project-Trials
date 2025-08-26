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
    private boolean isGameOver;

    public GamePanel() {
        // 初始化游戏元素
        snake = new Snake();
        food = new Food();
        food.generateNewFood(snake.getBody()); // 生成初始食物

        // 初始化计时器（控制蛇的移动速度）
        timer = new Timer(GameConstants.SPEED, this);
        timer.start();

        // 初始化面板属性
        setBackground(Color.BLACK); // 黑色背景
        setPreferredSize(new Dimension(
                GameConstants.WINDOW_WIDTH,
                GameConstants.WINDOW_HEIGHT
        ));
        setFocusable(true); // 允许接收键盘事件
        addKeyListener(this); // 添加键盘监听器

        isGameOver = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 绘制蛇
        g.setColor(Color.GREEN);
        for (Position p : snake.getBody()) {
            // 绘制每个蛇身格子（留1像素间隙，看起来像一节一节）
            g.fillRect(
                    p.getX() * GameConstants.SIZE,
                    p.getY() * GameConstants.SIZE,
                    GameConstants.SIZE - 1,
                    GameConstants.SIZE - 1
            );
        }

        // 绘制食物
        g.setColor(Color.RED);
        Position foodPos = food.getPosition();
        g.fillRect(
                foodPos.getX() * GameConstants.SIZE,
                foodPos.getY() * GameConstants.SIZE,
                GameConstants.SIZE - 1,
                GameConstants.SIZE - 1
        );

        // 游戏结束时显示提示
        if (isGameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("游戏结束！按R键重新开始", 50, 300);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            snake.move(); // 移动蛇
            checkCollisions(); // 检查碰撞
            checkFoodEaten(); // 检查是否吃到食物
        }
        repaint(); // 重绘画面
    }
    private void checkFoodEaten() {
        if (snake.getHead().equals(food.getPosition())) {
            // 吃到食物，蛇增长（不移除尾部）
            snake.grow();
            // 生成新食物
            food.generateNewFood(snake.getBody());
        } else {
            // 没吃到食物，移除尾部（保持长度不变）
            snake.removeTail();
        }
    }

    private void checkCollisions() {
        Position head = snake.getHead();

        // 撞墙检测（超出游戏区域）
        if (head.getX() < 0 || head.getX() >= GameConstants.WIDTH ||
                head.getY() < 0 || head.getY() >= GameConstants.HEIGHT) {
            isGameOver = true;
            timer.stop();
        }

        // 撞到自己检测
        for (int i = 1; i < snake.getBody().size(); i++) {
            if (head.equals(snake.getBody().get(i))) {
                isGameOver = true;
                timer.stop();
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // 控制方向
        if (key == KeyEvent.VK_UP) {
            snake.setDirection(GameConstants.UP);
        } else if (key == KeyEvent.VK_RIGHT) {
            snake.setDirection(GameConstants.RIGHT);
        } else if (key == KeyEvent.VK_DOWN) {
            snake.setDirection(GameConstants.DOWN);
        } else if (key == KeyEvent.VK_LEFT) {
            snake.setDirection(GameConstants.LEFT);
        }

        // 按R键重新开始游戏
        if (key == KeyEvent.VK_R && isGameOver) {
            restartGame();
        }
    }
    private void restartGame() {
        snake = new Snake();
        food.generateNewFood(snake.getBody());
        isGameOver = false;
        timer.start();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
