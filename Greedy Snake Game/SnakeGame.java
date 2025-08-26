import javax.swing.*;

public class SnakeGame extends JFrame {
    public SnakeGame() {
        // 设置窗口标题
        super("Snake Game");

        // 添加游戏面板
        add(new GamePanel());

        // 窗口设置
        pack(); // 根据面板大小自动调整窗口大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭窗口时退出程序
        setLocationRelativeTo(null); // 窗口居中显示
        setResizable(false); // 禁止调整窗口大小
        setVisible(true); // 显示窗口
    }

    public static void main(String[] args) {
        // 在Swing事件线程中启动游戏（确保线程安全）
        SwingUtilities.invokeLater(() -> new SnakeGame());
    }
}
