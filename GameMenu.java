import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class GameMenu extends JPanel {
    private final Color BUTTON = new Color(70,70,80);
    private final Color BUTTON_HOVER = new Color(100,100,110);
    private JFrame frame;
    private BufferedImage bgImage;
    
    public GameMenu(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        loadBackgroundImage();

        JLabel title = new JLabel("ESCAPE NIGHTMARE", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial",Font.BOLD,65));
        title.setBounds(200,100,1200,90);
        add(title);

        JLabel subtitle = new JLabel("SURVIVE THE NIGHT", SwingConstants.CENTER);
        subtitle.setForeground(new Color(180,180,180));
        subtitle.setFont(new Font("Arial",Font.PLAIN,22));
        subtitle.setBounds(400,190,800,40);
        add(subtitle);

        JButton startButton = createButton("PLAY GAME");
        startButton.setBounds(600,300,400,70);
        startButton.addActionListener(e->showLevelSelect());
        add(startButton);

        JButton exitButton = createButton("EXIT");
        exitButton.setBounds(600,420,400,70);
        exitButton.addActionListener(e->System.exit(0));
        add(exitButton);
    }
    private void loadBackgroundImage() {
        try {
            bgImage = ImageIO.read(getClass().getResource("/images/background.jpg"));
        } catch (Exception e) {
            System.out.println("error background.jpg");
            bgImage = null;
        }
    }
    private void showLevelSelect() {
        removeAll();
        setLayout(null);
        JLabel title = new JLabel("SELECT LEVEL",SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial",Font.BOLD,55));
        title.setBounds(300,100,1000,80);
        add(title);
        JButton lvl1 = createButton("LEVEL 1: CLASSIC");
        lvl1.setBounds(550,240,500,65);
        lvl1.addActionListener(e->startGame(1));
        add(lvl1);
        JButton lvl2 = createButton("LEVEL 2: SPIKE TRAPS");
        lvl2.setBounds(550,330,500,65);
        lvl2.addActionListener(e->startGame(2));
        add(lvl2);
        JButton lvl3 = createButton("LEVEL 3: TURRET HELL");
        lvl3.setBounds(550, 420, 500, 65);
        lvl3.addActionListener(e->startGame(3));
        add(lvl3);
        JButton back = createButton("BACK");
        back.setBounds(650,550,300,60);
        back.addActionListener(e -> {
            frame.setContentPane(new GameMenu(frame));
            frame.revalidate();
            frame.repaint();
        });
        add(back);
        revalidate();
        repaint();
    }
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial",Font.BOLD,26));
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {button.setBackground(BUTTON_HOVER);}
            public void mouseExited(MouseEvent e) {button.setBackground(BUTTON);}
        });
        return button;
    }
    private void startGame(int level){
        GamePanel game = new GamePanel(level,frame);
        frame.setContentPane(game);
        frame.revalidate();
        frame.repaint();
        game.requestFocusInWindow();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (bgImage != null) {
            g2.drawImage(bgImage,0,0,getWidth(),getHeight(),null);
            g2.setColor(new Color(0,0,0,100));
            g2.fillRect(0,0,getWidth(),getHeight());
        } else {
            GradientPaint gradient = new GradientPaint(0,0,new Color(10,10,15),0,getHeight(),new Color(35,35,45));
            g2.setPaint(gradient);
            g2.fillRect(0,0,getWidth(),getHeight());
        }
    }
}