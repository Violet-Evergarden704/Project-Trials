import java.util.ArrayList;
import java.util.List;
public class Snake {
    private List<Position> body;
    private int direction;
    private int nextDirection;

    public Snake() {
        // initiate the snake with 4 boxes
        body = new ArrayList<>();
        body.add(new Position(15, 15)); // head
        body.add(new Position(14, 15));
        body.add(new Position(13, 15));
        body.add(new Position(12, 15));

        // direction begins on right
        direction = GameConstants.RIGHT;
        nextDirection = direction;
    }

    public List<Position> getBody() {
        return body;
    }

    public void setDirection(int newDirection) {
        // ban changing directions to 180 degrees
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
        // As we add new head in move method, no more operation is needed.
        // grow method keeps the tail when eat a food so that snake gets longer in fact
    }

    public void removeTail(){
        body.remove(body.size() - 1);
    }

    public Position getHead() {
        return body.get(0);
    }
}
