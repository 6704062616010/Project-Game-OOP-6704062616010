import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Turret {
    public int x, y, width = 40, height = 40;
    private int dirX, dirY;
    private int shootTimer = 0;
    private int shootInterval;
    private BufferedImage turretImage;
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public Turret(int x,int y,int dirX,int dirY,int shootInterval) {
        this.x = x;
        this.y = y;
        this.dirX = dirX;
        this.dirY = dirY;
        this.shootInterval = shootInterval;
        loadImage();
    }
    private void loadImage() {
        try {
            turretImage = ImageIO.read(getClass().getResource("/images/Giant Projectile Cannon 3.png"));
        } catch (Exception e1) {
            try {
                turretImage = ImageIO.read(getClass().getResource("/images/Giant Projectile Cannon 3.png"));
            } catch (Exception e2) {
                turretImage = null;
            }
        }
    }
    public void update(ArrayList<Wall> walls) {
        shootTimer++;
        if (shootTimer >= shootInterval) {
            shootTimer = 0;
            bullets.add(new Bullet(x+width/2-5,y+height/2-5,dirX,dirY));
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.update();
            boolean hitWall = false;
            for (Wall w : walls) {
                if (b.getBounds().intersects(w.getBounds())) {
                    hitWall = true;
                    break;
                }
            }
            if (hitWall || b.isOutOfBounds()) {
                bullets.remove(i);
                i--;
            }
        }
    }
    public void draw(Graphics2D g) {
        if (turretImage != null) {
            g.drawImage(turretImage, x, y, width, height, null);
        } else {
            g.setColor(new Color(130, 40, 160));
            g.fillRect(x, y, width, height);
            g.setColor(Color.CYAN);
            g.fillOval(x + 10, y + 10, 20, 20);
        }
        for (Bullet b : bullets) {
            b.draw(g);
        }
    }
    public Rectangle getBounds() {
        return new Rectangle(x,y,width,height);
    }
}
class Bullet {
    public double x, y;
    public int size = 10;
    private double vx, vy;
    public Bullet(double x,double y,int dirX,int dirY) {
        this.x = x;
        this.y = y;
        double speed = 5.5;
        this.vx = dirX * speed;
        this.vy = dirY * speed;
    }
    public void update() {
        x += vx;
        y += vy;
    }
    public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.fillOval((int)x,(int)y,size,size);
    }
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,size,size);
    }
    public boolean isOutOfBounds() {
        return x < 0 || x > 1600 || y < 0 || y > 900;
    }
}