import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class Spike {
    public int x, y, width = 40, height = 40;
    private BufferedImage spikeImage;
    public Spike(int x, int y) {
        this.x = x;
        this.y = y;
        loadImage();
    }
    private void loadImage() {
        try {
            spikeImage = ImageIO.read(getClass().getResource("/images/small_wood_spike.png"));
        } catch (Exception e) {
            spikeImage = null;
        }
    }
    public void draw(Graphics2D g) {
        if (spikeImage != null) {
            g.drawImage(spikeImage, x, y, width, height, null);
        } else {
            g.setColor(new Color(200, 60, 60));
            int[] x1 = {x, x + 13, x + 26};
            int[] y1 = {y + height, y, y + height};
            g.fillPolygon(x1, y1, 3);

            int[] x2 = {x + 14, x + 27, x + 40};
            int[] y2 = {y + height, y, y + height};
            g.fillPolygon(x2, y2, 3);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }
    public Rectangle getBounds() {
        return new Rectangle(x + 5, y + 5, width - 10, height - 10);
    }
}