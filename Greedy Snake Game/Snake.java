import java.util.ArrayList;
import java.util.List;
public class Snake {
    private List<Position> body;
    private int direction;
    // 下一次移动方向（解决快速按键时的方向冲突）
    private int nextDirection;

    public Snake() {
        // 初始化蛇的初始位置（横向排列3个格子）
        body = new ArrayList<>();
        body.add(new Position(15, 15)); // 蛇头
        body.add(new Position(14, 15));
        body.add(new Position(13, 15));

        // 初始方向向右
        direction = GameConstants.RIGHT;
        nextDirection = direction;
    }

    public List<Position> getBody() {
        return body;
    }

    public void setDirection(int newDirection) {
        // 防止180度掉头（例如当前向上时不能直接向下）
        if (Math.abs(newDirection - direction) != 2) {
            nextDirection = newDirection;
        }
    }

    public void move(){
        direction = nextDirection;
        Position head = body.get(0);
        int newX = head.getX();
        int newY = head.getY();
        switch (direction) {
            case GameConstants.UP:
                newY--;
                break;
            case GameConstants.RIGHT:
                newX++;
                break;
            case GameConstants.DOWN:
                newY++;
                break;
            case GameConstants.LEFT:
                newX--;
                break;
        }
        body.add(0, new Position(newX, newY));
    }

    public void grow() {
        // 移动时已经添加了新头部，这里不需要额外操作
        // （正常移动后会移除尾部，grow()的作用是让尾部保留）
    }

    public void removeTail(){
        body.remove(body.size() - 1);
    }

    public Position getHead() {
        return body.get(0);
    }
}
