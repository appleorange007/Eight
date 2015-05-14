package eight;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Card Class
 * 
 * @author
 * @version 5/11/02
 * 
 * 
 */
public class Card
{
    private final static int WIDTH = 41;
    private final static int HEIGHT = 193;

    // reference cards pic
    private final Point[] picBitmapLocs = {
            new Point(568, 8), new Point(527, 8), new Point(486, 8), new Point(445, 8),
            new Point(404, 8), new Point(363, 8), new Point(322, 8), new Point(281, 8),
            new Point(240, 8),

            new Point(569, 204), new Point(528, 204), new Point(487, 204), new Point(446, 204),
            new Point(405, 204), new Point(364, 204), new Point(323, 204), new Point(282, 204),
            new Point(241, 204),

            new Point(568, 402), new Point(527, 402), new Point(486, 402), new Point(445, 402),
            new Point(404, 402), new Point(363, 402), new Point(322, 402), new Point(281, 402),
            new Point(240, 402),

            new Point(181, 328), new Point(139, 328), new Point(97, 328), // xing,qian,hong

            new Point(24, 77), new Point(66, 77), new Point(107, 76), new Point(149, 76),
            new Point(191, 77) // cai, xi,shou,lu,fu
    };

    // Reference at bottom of class
    String cardName;
    int cardNumber;
    BufferedImage cardPic;
    Image cardSideWays;

    // for drawing card
    MainInterface main;
    Graphics g;

    public Card(int cardNumber, Image cardspic, MainInterface mi, Graphics g)
    {
        this.cardNumber = cardNumber;
        this.main = mi;
        this.g = g;

        cardPic = new BufferedImage(WIDTH, HEIGHT, Transparency.BITMASK);
        Graphics tempg = cardPic.getGraphics();

        cardSideWays = new BufferedImage(HEIGHT, WIDTH, Transparency.BITMASK);

        getCardDetails(cardspic, tempg);
    }

    public void drawCard(Point p)
    {
        g.drawImage(cardPic, (int) p.getX(), (int) p.getY(), main);
    }

    public void drawCard2(int x, int y)
    {
        g.drawImage(cardPic, x, y, main);
    }

    public void drawSideWays(Point p)
    {
        g.drawImage(cardSideWays, (int) p.getX(), (int) p.getY(), main);
    }

    public void drawSideWays2(int x, int y)
    {
        g.drawImage(cardSideWays, x, y, main);
    }

    public int getNumber()
    {
        return cardNumber;
    }

    /*--------------------------------
     *   Card Code Index
     *
     *
     *-------------------------------*/

    private void getCardDetails(Image cardspic, Graphics tempg)
    {
        Point loc = picBitmapLocs[cardNumber];

        // seperating card image from other card images
        tempg.drawImage(cardspic, (int) -loc.getX(), (int) -loc.getY(), main);

        // rotating card image to create sideways card image
        Graphics tempg2 = cardSideWays.getGraphics();
        Graphics2D g2d = (Graphics2D) tempg2;
        AffineTransform origXform = g2d.getTransform();
        AffineTransform newXform = (AffineTransform) (origXform.clone());
        newXform.rotate(Math.toRadians(-90), 50, 50);
        g2d.setTransform(newXform);
        // draw image
        g2d.drawImage(cardPic, 29, 0, main);
        g2d.setTransform(origXform);
    }

    public static String getCardStringValue(int cardNumber) {
        String value = "";

        if (cardNumber < 9) {
            value = "" + cardNumber + "t";

        } else if (cardNumber < 18) {
            value = "" + cardNumber + "s";
        } else if (cardNumber < 27) {
            value = "" + cardNumber + "w";
        } else if (27 == cardNumber) {
            value = "xing";
        } else if (28 == cardNumber) {
            value = "qian";
        } else if (29 == cardNumber) {
            value = "hong";
        } else if (30 == cardNumber) {
            value = "cai";
        } else if (31 == cardNumber) {
            value = "xi";
        } else if (32 == cardNumber) {
            value = "shou";
        } else if (33 == cardNumber) {
            value = "lu";
        } else if (34 == cardNumber) {
            value = "fu";
        }
        return value;
    }
}
