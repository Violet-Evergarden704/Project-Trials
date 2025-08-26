import javax.swing.*;

public class SnakeGame extends JFrame {
    public SnakeGame() {
        // headlines
        super("Snake Game");

        // add game panel
        add(new GamePanel());

        // window settings
        pack(); // change size according to panel size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit when window closed
        setLocationRelativeTo(null); // display in middle
        setResizable(false); // unable to resize
        setVisible(true);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
