import java.awt.*;

public class Wall {
    int x;
    int y;
    int width;
    int height;
    public Wall(int x,int y,int width,int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public Rectangle getBounds() {
        return new Rectangle(x,y,width,height);
    }
    public void draw(Graphics2D g) {
        g.setColor(new Color(70,70,70));
        g.fillRect(x,y,width,height);
    }
}