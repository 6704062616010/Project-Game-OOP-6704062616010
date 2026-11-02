import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Desk {

    // ==========================================
    // POSITION
    // ==========================================

    public int x;
    public int y;


    // ==========================================
    // SIZE
    // ==========================================

    public int width = 100;
    public int height = 55;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public Desk(int x, int y) {

        this.x = x;
        this.y = y;
    }


    // ==========================================
    // DRAW DESK
    // ==========================================

    public void draw(Graphics2D g) {

        // ------------------------------------------
        // DESK TOP
        // ------------------------------------------

        g.setColor(
                new Color(
                        120,
                        75,
                        40
                )
        );

        g.fillRect(
                x,
                y,
                width,
                height
        );


        // ------------------------------------------
        // DESK BORDER
        // ------------------------------------------

        g.setColor(Color.BLACK);

        g.drawRect(
                x,
                y,
                width,
                height
        );


        // ------------------------------------------
        // DESK LINE
        // ------------------------------------------

        g.setColor(
                new Color(
                        80,
                        45,
                        25
                )
        );

        g.drawLine(
                x,
                y + height / 2,
                x + width,
                y + height / 2
        );


        // ------------------------------------------
        // DESK LEGS
        // ------------------------------------------

        g.setColor(
                new Color(
                        70,
                        70,
                        70
                )
        );


        // Left leg

        g.fillRect(
                x + 10,
                y + height,
                8,
                25
        );


        // Right leg

        g.fillRect(
                x + width - 18,
                y + height,
                8,
                25
        );


        // ------------------------------------------
        // CHAIR
        // ------------------------------------------

        int chairX =
                x + 35;

        int chairY =
                y + height + 12;


        g.setColor(
                new Color(
                        90,
                        90,
                        95
                )
        );


        // Chair seat

        g.fillRect(
                chairX,
                chairY,
                35,
                25
        );


        // Chair back

        g.fillRect(
                chairX,
                chairY - 18,
                35,
                8
        );


        // Chair border

        g.setColor(Color.BLACK);

        g.drawRect(
                chairX,
                chairY,
                35,
                25
        );


        g.drawRect(
                chairX,
                chairY - 18,
                35,
                8
        );
    }


    // ==========================================
    // COLLISION
    // ==========================================

    public Rectangle getBounds() {

        /*
         * ให้ Collision ครอบเฉพาะตัวโต๊ะ
         * ไม่รวมเก้าอี้
         */

        return new Rectangle(
                x,
                y,
                width,
                height
        );
    }
}

