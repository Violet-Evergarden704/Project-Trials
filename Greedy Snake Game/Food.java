import java.util.List;
import java.util.Random;

public class Food {
    private Position position;
    private Random random;

    public Food() {
        random = new Random();
        position = new Position(0, 0);
    }
    public Position getPosition() {
        return position;
    }

    public void generateNewFood(List<Position> snakeBody) {
        int x, y;
        boolean onSnake;

        do {
            // 随机生成坐标（在游戏区域内）
            x = random.nextInt(GameConstants.WIDTH);
            y = random.nextInt(GameConstants.HEIGHT);
            position = new Position(x, y);

            // 检查是否在蛇身上
            onSnake = false;
            for (Position p : snakeBody) {
                if (position.equals(p)) {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake); // 如果在蛇身上，就重新生成
    }
}
