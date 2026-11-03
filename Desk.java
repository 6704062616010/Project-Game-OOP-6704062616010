import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Desk {
    public int x;
    public int y;
    public int width = 100;
    public int height = 55;
    public Desk(int x, int y) {

        this.x = x;
        this.y = y;
    }
    public void draw(Graphics2D g) {
        g.setColor(new Color(120,75,40));
        g.fillRect(x,y,width,height);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,width,height);
        g.setColor(new Color(80,45,25));
        g.drawLine(x,y+height/2,x+width,y+height/2);
        g.setColor(new Color(70,70,70));
        g.fillRect(x+10,y+height,8,25);
        g.fillRect(x+width-18,y+height,8,25);
        int chairX = x+35;
        int chairY = y+height+12;
        
        g.setColor(new Color(90,90,95));
        g.fillRect(chairX,chairY,35,25);
        g.fillRect(chairX,chairY - 18,35,8);
        g.setColor(Color.BLACK);
        g.drawRect(chairX,chairY,35,25);
        g.drawRect(chairX,chairY - 18,35,8);
    }
    public Rectangle getBounds() {
        return new Rectangle(x,y,width,height);
    }
}

