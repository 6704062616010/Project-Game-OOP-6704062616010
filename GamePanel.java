import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class GamePanel extends JPanel implements KeyListener {
    public static final int GAME_WIDTH = 1600;
    public static final int GAME_HEIGHT = 900;
    Player player;
    Ghost ghost;
    Safe safe;
    ExitDoor exitDoor;
    ArrayList<Wall> walls;
    ArrayList<Desk> desks;
    ArrayList<Generator> generators;
    ArrayList<Spike> spikes;
    ArrayList<Turret> turrets;
    Wall exitBlock;
    int currentLevel;
    JFrame frame;
    final int PLAYING = 0,GAME_OVER = 1,WIN = 2;
    int gameState = PLAYING;
    boolean hasKey = false;
    boolean ePressed = false;
    Thread gameThread;
    public GamePanel(int level,JFrame frame) {
        this.currentLevel = level;
        this.frame = frame;
        setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
        setBackground(new Color(10,10,15));
        setFocusable(true);
        addKeyListener(this);
        walls = new ArrayList<>();
        desks = new ArrayList<>();
        generators = new ArrayList<>();
        spikes = new ArrayList<>();
        turrets = new ArrayList<>();

        createMap(currentLevel);
        createExitBlock();
        startGame();
        requestFocusInWindow();
    }
    private void createMap(int level) {
        walls.add(new Wall(30, 30, 1540, 25));      
        walls.add(new Wall(30, 30, 25, 840));       
        walls.add(new Wall(30, 845, 1540, 25));      
        walls.add(new Wall(1545, 30, 25, 360));      
        walls.add(new Wall(1545, 510, 25, 360));      
        if (level == 1) {
            player = new Player(775, 435);
            ghost = new Ghost(100, 750);
            safe = new Safe(1380, 720);         
            exitDoor = new ExitDoor(1545, 390);
            walls.add(new Wall(55, 350, 260, 25));
            walls.add(new Wall(435, 350, 290, 25));
            walls.add(new Wall(725, 55, 25, 295));
            walls.add(new Wall(850, 55, 25, 295));
            walls.add(new Wall(850, 350, 290, 25));
            walls.add(new Wall(1260, 350, 260, 25));
            walls.add(new Wall(55, 510, 260, 25));
            walls.add(new Wall(435, 510, 290, 25));
            walls.add(new Wall(725, 535, 25, 310));
            walls.add(new Wall(850, 535, 25, 310));
            walls.add(new Wall(850, 510, 290, 25));
            walls.add(new Wall(1260, 510, 260, 25));
            desks.add(new Desk(150, 150));
            desks.add(new Desk(950, 150));
            desks.add(new Desk(150, 620));
            desks.add(new Desk(950, 620));
            generators.add(new Generator(450, 150));
            generators.add(new Generator(1350, 150));
            generators.add(new Generator(450, 680));

} else if (level == 2) {
            player = new Player(100, 100);
            ghost = new Ghost(1400, 730);
            safe = new Safe(100, 730);
            exitDoor = new ExitDoor(1545, 390);
            walls.add(new Wall(320, 55, 25, 550));
            walls.add(new Wall(620, 240, 25, 605));
            walls.add(new Wall(920, 55, 25, 550));
            walls.add(new Wall(1220, 240, 25, 605));
            desks.add(new Desk(120, 400));
            desks.add(new Desk(1020, 400));
            generators.add(new Generator(200, 730));
            generators.add(new Generator(760, 100));
            generators.add(new Generator(1050, 730));
            spikes.add(new Spike(200, 250));
            spikes.add(new Spike(400, 120));
            spikes.add(new Spike(400, 300));
            spikes.add(new Spike(400, 480));
            spikes.add(new Spike(500, 680));
            spikes.add(new Spike(760, 300));
            spikes.add(new Spike(760, 500));
            spikes.add(new Spike(500, 120));
            spikes.add(new Spike(1070, 180));
            spikes.add(new Spike(1070, 320));
            spikes.add(new Spike(1070, 550));
            spikes.add(new Spike(800, 750));
            spikes.add(new Spike(1370, 120));
            spikes.add(new Spike(1370, 280));
            spikes.add(new Spike(1370, 550));
            spikes.add(new Spike(1370, 700));

        } else if (level == 3) {
            player = new Player(775, 435);
            ghost = new Ghost(100, 100);
            safe = new Safe(760, 100);
            exitDoor = new ExitDoor(1545, 390);
            walls.add(new Wall(350, 200, 250, 150));
            walls.add(new Wall(1000, 200, 250, 150));
            walls.add(new Wall(350, 550, 250, 150));
            walls.add(new Wall(1000, 550, 250, 150));
            desks.add(new Desk(750, 600));
            generators.add(new Generator(100, 730));
            generators.add(new Generator(1420, 100));
            generators.add(new Generator(1420, 730));
            spikes.add(new Spike(780, 250));
            spikes.add(new Spike(780, 320));
            spikes.add(new Spike(780, 500));
            spikes.add(new Spike(650, 435));
            spikes.add(new Spike(910, 435));
            spikes.add(new Spike(200, 220));
            spikes.add(new Spike(200, 550));
            spikes.add(new Spike(300, 730));
            spikes.add(new Spike(1350, 220));
            spikes.add(new Spike(1350, 550));
            spikes.add(new Spike(1250, 730));
            spikes.add(new Spike(1250, 100));
            spikes.add(new Spike(450, 400));
            spikes.add(new Spike(1100, 400));
            turrets.add(new Turret(200, 60, 0, 1, 80));
            turrets.add(new Turret(1350, 795, 0, -1, 80));
            turrets.add(new Turret(60, 430, 1, 0, 100));
            turrets.add(new Turret(1485, 430, -1, 0, 100));
        }
    }
    private void createExitBlock() {
        exitBlock = new Wall(1545, 390, 25, 120);
        walls.add(exitBlock);
    }
    private void unlockExit() {
        if (exitBlock != null) {
            walls.remove(exitBlock);
            exitBlock = null;
        }
    }
    private void startGame() {
        gameThread = new Thread(() -> {
            while (true) {
                updateGame();
                repaint();
                try {
                    Thread.sleep(16);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.start();
    }
    private void updateGame() {
        if (gameState != PLAYING) return;
        player.update(walls, desks, generators);
        ghost.update(player, walls, desks);
        for (Generator generator : generators) generator.update();
        for (Turret turret : turrets) turret.update(walls);
        for (Spike spike : spikes) {
            if (player.getBounds().intersects(spike.getBounds())) {
                gameState = GAME_OVER;
                stopGenerators();
            }
        }
        for (Turret turret : turrets) {
            for (Bullet b : turret.bullets) {
                if (player.getBounds().intersects(b.getBounds())) {
                    gameState = GAME_OVER;
                    stopGenerators();
                }
            }
        }
        if (ePressed) {
            int index = getNearbyGenerator();
            if (index >= 0 && !generators.get(index).isRepaired()) {
                generators.get(index).startRepair();
            }
        } else {
            stopGenerators();
        }
        if (allGeneratorsRepaired() && !safe.isOpened() && isNear(safe.getBounds()) && ePressed) {
            safe.open();
            hasKey = true;
        }
        if (hasKey && !exitDoor.isOpened() && isNear(exitDoor.getBounds()) && ePressed) {
            exitDoor.open();
            unlockExit();
            gameState = WIN;
        }
        if (ghost.hitPlayer(player)) {
            gameState = GAME_OVER;
            stopGenerators();
        }
    }
    private void stopGenerators() {
        for (Generator g : generators) g.stopRepair();
    }
    private boolean allGeneratorsRepaired() {
        for (Generator g : generators) {
            if (!g.isRepaired()) return false;
        }
        return true;
    }
    private int getNearbyGenerator() {
        for (int i = 0; i < generators.size(); i++) {
            if (isNear(generators.get(i).getBounds())) return i;
        }
        return -1;
    }
    private boolean isNear(Rectangle object) {
        Rectangle playerRect = player.getBounds();
        int range = 75;
        Rectangle area = new Rectangle(object.x - range, object.y - range, object.width + range * 2, object.height + range * 2);
        return area.intersects(playerRect);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(12, 12, 18));
        g2.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        g2.setColor(new Color(40, 42, 54));
        g2.fillRect(55, 55, 1490, 790);
        g2.setColor(new Color(50, 52, 66));
        for (int x = 55; x < 1545; x += 50) g2.drawLine(x, 55, x, 845);
        for (int y = 55; y < 845; y += 50) g2.drawLine(55, y, 1545, y);
        for (Wall wall : walls) wall.draw(g2);
        for (Desk desk : desks) desk.draw(g2);
        for (Generator generator : generators) generator.draw(g2);
        for (Spike spike : spikes) spike.draw(g2);
        for (Turret turret : turrets) turret.draw(g2);
        safe.draw(g2);
        exitDoor.draw(g2);
        if (player != null && gameState == PLAYING) {
            int auraRadius = 250;
            int auraX = (int) player.x + player.width / 2 - auraRadius;
            int auraY = (int) player.y + player.height / 2 - auraRadius;
            RadialGradientPaint p = new RadialGradientPaint(
                new Point((int) player.x + player.width / 2, (int) player.y + player.height / 2),
                auraRadius,
                new float[]{0.0f, 0.7f, 1.0f},
                new Color[]{new Color(255, 255, 200, 50), new Color(20, 20, 30, 120), new Color(10, 10, 15, 200)}
            );
            g2.setPaint(p);
            g2.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        }
        ghost.draw(g2);
        player.draw(g2);
        drawUI(g2);
        if (gameState == GAME_OVER) drawGameOver(g2);
        if (gameState == WIN) drawWin(g2);
        g2.dispose();
    }
    private void drawUI(Graphics2D g) {
        g.setColor(new Color(20, 20, 30, 220));
        g.fillRoundRect(50, 15, 450, 85, 15, 15);
        g.setColor(new Color(80, 80, 110));
        g.drawRoundRect(50, 15, 450, 85, 15, 15);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(new Color(220, 220, 240));
        g.drawString("ESCAPE NIGHTMARE - LEVEL " + currentLevel, 70, 42);
        int power = 0;
        for (Generator g1 : generators) if (g1.isRepaired()) power++;
        g.setColor(power == 3 ? new Color(100, 255, 100) : new Color(255, 180, 50));
        g.drawString("POWER: " + power + " / 3", 70, 75);
        if (hasKey) {
            g.setColor(new Color(255, 220, 50));
            g.drawString("★ KEY: FOUND", 260, 75);
        }
        int index = getNearbyGenerator();
        if (index >= 0 && !generators.get(index).isRepaired()) {
            g.setColor(Color.YELLOW);
            g.drawString("HOLD [E] TO REPAIR", (int) player.x - 60, (int) player.y - 25);
        }
        if (allGeneratorsRepaired() && !safe.isOpened() && isNear(safe.getBounds())) {
            g.setColor(Color.YELLOW);
            g.drawString("HOLD [E] TO OPEN SAFE", (int) player.x - 70, (int) player.y - 25);
        }
        if (hasKey && !exitDoor.isOpened() && isNear(exitDoor.getBounds())) {
            g.setColor(Color.YELLOW);
            g.drawString("HOLD [E] TO UNLOCK EXIT", (int) player.x - 80, (int) player.y - 25);
        }
    }
    private void drawGameOver(Graphics2D g) {
        g.setColor(new Color(15, 0, 0, 220));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        g.setColor(new Color(255, 50, 50));
        g.setFont(new Font("Arial", Font.BOLD, 75));
        g.drawString("GAME OVER", 570, 380);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 26));
        g.drawString("The nightmare consumed you...", 610, 440);
        g.drawString("Press [ R ] to Restart  |  Press [ M ] for Menu", 520, 510);
    }
    private void drawWin(Graphics2D g) {
        g.setColor(new Color(0, 20, 10, 220));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        g.setColor(new Color(80, 255, 120));
        g.setFont(new Font("Arial", Font.BOLD, 75));
        g.drawString("YOU ESCAPED!", 540, 380);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 26));
        g.drawString("Level " + currentLevel + " Cleared Successfully!", 610, 440);
        g.drawString("Press [ R ] to Play Again  |  Press [ M ] for Menu", 500, 510);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_M && gameState != PLAYING) {
            frame.setContentPane(new GameMenu(frame));
            frame.revalidate();
            frame.repaint();
            return;
        }

        if (key == KeyEvent.VK_R && gameState != PLAYING) {
            restartGame();
            return;
        }

        if (gameState != PLAYING) return;

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) player.up = true;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) player.down = true;
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) player.left = true;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) player.right = true;
        if (key == KeyEvent.VK_E) ePressed = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) player.up = false;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) player.down = false;
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) player.left = false;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) player.right = false;
        if (key == KeyEvent.VK_E) {
            ePressed = false;
            stopGenerators();
        }
    }
    @Override public void keyTyped(KeyEvent e) {}
    private void restartGame() {
        walls.clear();
        desks.clear();
        generators.clear();
        spikes.clear();
        turrets.clear();
        createMap(currentLevel);
        hasKey = false;
        ePressed = false;
        gameState = PLAYING;
        if (exitBlock == null) createExitBlock();
        requestFocusInWindow();
        repaint();
    }
}