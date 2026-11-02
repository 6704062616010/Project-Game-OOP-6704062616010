import java.awt.*;
public class Generator {
    int x;
    int y;
    int width = 70;
    int height = 70;
    boolean repaired = false;
    boolean repairing = false;
    int repairTime = 180;
    int repairProgress = 0;
    public Generator(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void update() {
        if (repaired) {
            repairing = false;
            repairProgress = repairTime;
            return;
        }
        if (repairing) {
            repairProgress++;
            if (repairProgress >= repairTime) {
                repairProgress = repairTime;
                repaired = true;
                repairing = false;
            }
        }
    }
    public void startRepair() {
        if (repaired) {
            return;
        }
        repairing = true;
    }
    public void stopRepair() {
        if (repaired) {
            return;
        }
        repairing = false;
    }
    public boolean isRepaired() {
        return repaired;
    }
    public Rectangle getBounds() {
        return new Rectangle(x,y,width,height);
    }
    public void draw(Graphics2D g) {
        if (repaired) {
            g.setColor(new Color(50,170,70));
        } else {
            g.setColor(new Color(100,100,105));
        }
        g.fillRoundRect(x,y,width,height,12,12);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x,y,width,height,12,12);
        if (repaired) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.RED);
        }
        g.fillOval(x+10,y+10,15,15);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.BOLD,12));
        g.drawString("GEN",x+20,y+45);
        if (repairProgress>0 && !repaired) {
            int barWidth = width - 10;
            int progressWidth = (int)(barWidth * ((double) repairProgress / repairTime));
            g.setColor(Color.BLACK);
            g.fillRect(x+5,y-12,barWidth,7);
            g.setColor(Color.YELLOW);
            g.fillRect(x+5,y-12,progressWidth,7);
            g.setColor(Color.WHITE);
            g.drawRect(x+5,y-12,barWidth,7);
        }
    }
}