import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ExitDoor {
    int x;
    int y;
    int width = 70;
    int height = 120;
    boolean opened = false;
    public ExitDoor(int x,int y) {
        this.x = x;
        this.y = y;
    }
    public void open() {
        opened = true;
    }
    public void draw(Graphics g) {
        if (opened) {
            g.setColor(new Color(50,180,70));
        } else {
            g.setColor(new Color(120,60,40));
        }
        g.fillRect(x,y,width,height);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,width,height);
        g.setColor(Color.WHITE);
        if (opened) {
            g.drawString("EXIT",x+18,y+60);
        } else {
            g.drawString("LOCKED",x+8,y+60);
        }
    }
    public Rectangle getBounds() {
        return new Rectangle(x,y,width,height);
    }
    public boolean isOpened() {
        return opened;
    }
}