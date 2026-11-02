import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Safe {
    int x;
    int y;
    int width = 80;
    int height = 80;
    boolean opened = false;
    public Safe(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void open() {
        opened = true;
    }
    public void draw(Graphics g) {
        if (opened) {
            g.setColor(new Color(70,70,70));
        } else {
            g.setColor(new Color(100,100,110));
        }
        g.fillRect(x,y,width,height);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,width,height);
        g.setColor(Color.WHITE);
        if (opened) {
            g.drawString("OPEN",x+20,y+35
            );
        } else {
            g.drawString("SAFE",x+20,y+35);
        }
        if (opened) {
            g.setColor(new Color(255,220,50));
            g.fillOval(x + 30,y + 42,15,15);
            g.fillRect(x+43,y+47,25,5);
            g.fillRect(x+60,y+50,5,8);
            g.fillRect(x+53,y+50,5,8);
        }
    }
    public Rectangle getBounds() {
        return new Rectangle(x,y,width,height);
    }
    public boolean isOpened() {
        return opened;
    }
}