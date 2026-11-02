import javax.swing.*;

public class main {
    public static void main(
            String[] args
    ) {
        JFrame frame = new JFrame("Escape Nightmare");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GamePanel.GAME_WIDTH,GamePanel.GAME_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        GameMenu menu = new GameMenu(frame);
        frame.setContentPane(menu);
        frame.setVisible(true);
    }
}


