import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player {
    public double x;
    public double y;
    public int width = 50;
    public int height = 50;
    private int collisionWidth = 32;
    private int collisionHeight = 32;
    private double speed = 3.2;
    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;

    private BufferedImage playerImage;

    public Player(double x,double y) {
        this.x = x;
        this.y = y;
        loadImage();
    }
    private void loadImage() {
        try {
            playerImage = ImageIO.read(getClass().getResource("/images/player.png"));
        } catch (Exception e) {
            playerImage = null;
        }
    }
    public void update(
            java.util.ArrayList<Wall> walls,
            java.util.ArrayList<Desk> desks,
            java.util.ArrayList<Generator> generators
    ) {
        double dx = 0;
        double dy = 0;

        if (up) dy -= 1;
        if (down) dy += 1;
        if (left) dx -= 1;
        if (right) dx += 1;
        if (dx != 0 && dy != 0) {
            dx *= 0.7071;
            dy *= 0.7071;
        }
        if (dx != 0) {
            double nextX = x+(dx*speed);
            Rectangle nextBounds = new Rectangle(
                    (int) (nextX + getCollisionOffsetX()),
                    (int) (y + getCollisionOffsetY()),
                    collisionWidth,
                    collisionHeight
            );
            if (!isBlocked(nextBounds,walls,desks,generators)) {
                x = nextX;
            }
        }
        if (dy != 0) {
            double nextY = y + (dy*speed);
            Rectangle nextBounds = new Rectangle(
                    (int)(x+getCollisionOffsetX()),
                    (int)(nextY+getCollisionOffsetY()),
                    collisionWidth,
                    collisionHeight
            );
            if (!isBlocked(nextBounds,walls,desks,generators)) {
                y = nextY;
            }
        }
        if (x < 55) x = 55;
        if (y < 55) y = 55;
        if (x + width > GamePanel.GAME_WIDTH-55) x = GamePanel.GAME_WIDTH-55-width;
        if (y + height > GamePanel.GAME_HEIGHT-55) y = GamePanel.GAME_HEIGHT-55-height;
    }
    private boolean isBlocked(
            Rectangle playerRect,
            java.util.ArrayList<Wall> walls,
            java.util.ArrayList<Desk> desks,
            java.util.ArrayList<Generator> generators
    ) {
        for (Wall wall : walls) {
            if (playerRect.intersects(wall.getBounds())) return true;}
        for (Desk desk : desks) {
            if (playerRect.intersects(desk.getBounds())) return true;}
        for (Generator generator:generators) {
            if (playerRect.intersects(generator.getBounds())) return true;}
        return false;
    }
    private int getCollisionOffsetX() {
        return (width - collisionWidth)/2;
    }
    private int getCollisionOffsetY() {
        return (height - collisionHeight)/2;
    }
    public Rectangle getBounds() {
        return new Rectangle(
                (int)x+getCollisionOffsetX(),
                (int)y+getCollisionOffsetY(),
                collisionWidth,
                collisionHeight
        );
    }

    public void draw(Graphics2D g) {
        if (playerImage != null) {
            g.drawImage(playerImage,(int)x,(int)y,width,height,null);
        } else {
            g.setColor(new Color(50,150,255));
            g.fillOval((int) x,(int)y,width,height);
            g.setColor(Color.WHITE);
            g.drawOval((int)x,(int)y,width,height);
        }
    }
}