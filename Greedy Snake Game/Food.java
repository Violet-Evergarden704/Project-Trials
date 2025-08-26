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
            // randomly generate in the game area
            x = random.nextInt(GameConstants.WIDTH);
            y = random.nextInt(GameConstants.HEIGHT);
            position = new Position(x, y);

            // check if it is on snake body
            onSnake = false;
            for (Position p : snakeBody) {
                if (position.equals(p)) {
                    onSnake = true;
                    break;
                }
            }
        } while (onSnake); // regenerate if on body
    }
}
