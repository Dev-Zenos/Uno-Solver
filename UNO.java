import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Graphics;

public class UNO implements KeyListener {

    private Robot jeff;
    private UnoElo unoElo = new UnoElo();
    private CardReader cardReader = new CardReader();
    private ArrayList<Card> deck = new ArrayList<Card>();
    private Card mainCard;

    public static void main(String[] args) throws IOException {
        UNO uno = new UNO();

        // File file2 = new File("screenshot2.png");
        // BufferedImage image = ImageIO.read(file2 );
        // File file = new File("screenshot2.png");
        // ImageIO.write(CardReader.grayscale(image), "png", file);
        /*
         * Rectangle rect = new Rectangle(783, 371, 40, 60);
         * BufferedImage img = cropImage(image, rect);
         * try {
         * ImageIO.write(img, "png", new File("screenshot.png"));
         * } catch (IOException e) {
         * e.printStackTrace();
         * }
         */

        uno.start();

    }

    public UNO() {
        // System.setProperty("sun.java2d.uiScale", "1");
        try {
            jeff = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        JFrame frame = new JFrame("UNO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);

        // Add global key listener
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof KeyEvent) {
                KeyEvent keyEvent = (KeyEvent) event;
                if (keyEvent.getKeyChar() == 'i') {
                    System.out.println("uno");
                    takeScreenshot();
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
        // takeScreenshot2();
        while (true) {
            try {
                Thread.sleep(1500);
                // takeScreenshot();
                // takeScreenshot2();

                BufferedImage image = ImageUtil.read("screenshot2.png");
                long start = System.currentTimeMillis();
                scanImage(image);
                // Rectangle rect = new Rectangle(788, 381,25, 40);
                // Rectangle rect = new Rectangle(825, 550, 25, 40);
                // BufferedImage img = cropImage(image, rect);
                // ImageUtil.write(img, "screenshot.png");
                // cardReader.getMainCard(image);
                System.out.println("Scan Complete");
                System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");

                ArrayList<Card> validCards = unoElo.getValidCards(mainCard, deck);
                System.out.println("\n\nValid Cards are:");
                for (Card card : validCards) {
                    System.out.println(card);
                }

                break;
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }

    public void scanImage(BufferedImage image) {
        deck.clear();

        System.out.print("Main Card is: ");
        mainCard = cardReader.getMainCard(image);
        System.out.println(mainCard);

        System.out.println("\n\nDeck Cards are:");
        ArrayList<Card> decks = cardReader.getDeckCard(image);
        for (Card card : decks) {
            deck.add(card);
            System.out.println(card);
        }
    }

    public void takeScreenshot() {
        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = jeff.createScreenCapture(screenRect);
            File file = new File("screenshot.png");
            ImageIO.write(capture, "png", file);
            System.out.println("Screenshot saved as screenshot.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        BufferedImage img = src.getSubimage(rect.x, rect.y, rect.width, rect.height); // fill in the corners of the
                                                                                      // desired crop location here
        BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, null);
        return copyOfImage;
    }

    public void takeScreenshot2() {
        try {
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = jeff.createScreenCapture(screenRect);
            File file = new File("screenshot2.png");
            ImageIO.write(capture, "png", file);
            // System.out.println("Screenshot saved as screenshot2.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in this example
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Not used in this example
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used in this example
    }
}
