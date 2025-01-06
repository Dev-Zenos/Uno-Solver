import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import jssim.SsimCalculator;

public class CardReader {
    BufferedImage[] images;

    public CardReader() {
        try {
            images = ImageUtil.grabImages("backup/", "Green_", 15);
            System.out.println("Images grabbed\n");
        } catch (Exception e) {
            System.out.println("Failed to grab images");
        }
        // cropAll(new File("High Res Img/Low Res Images"));

        // grayScaleAll(new File("High Res Img/Low Res Images"));
        // System.out.println("Images gray scaled");

    }

    public Card getMainCard(BufferedImage img) {
        // BufferedImage cropped = UNO.cropImage(img, new Rectangle(5, 10, 25, 40));
        Rectangle rect = new Rectangle(788, 381, 25, 40);
        BufferedImage image = UNO.cropImage(img, rect);
        ImageUtil.write(image, "screenshot.png");
        float num = getCard(image);
        // System.out.printf("%s %s \n", getColor(image), toCard(num));
        return new Card(getColor(image), (int) num, 0);
    }

    public ArrayList<Card> getDeckCard(BufferedImage img) {
        int chunkSize = 100; // Process 100 pixels at a time
        ConcurrentLinkedQueue<Card> deck = new ConcurrentLinkedQueue<>();

        IntStream.range(0, (img.getWidth() - 25) / chunkSize + 1)
                .parallel()
                .forEach(chunk -> processChunk(img, chunk * chunkSize,
                        Math.min((chunk + 1) * chunkSize, img.getWidth() - 25), deck));

        return new ArrayList<>(deck);
    }

    private void processChunk(BufferedImage img, int start, int end, ConcurrentLinkedQueue<Card> deck) {
        for (int i = start; i < end; i++) {
            Rectangle rect = new Rectangle(i, 550, 25, 40);
            BufferedImage image = UNO.cropImage(img, rect);
            float num = getCard(image);
            if (num != -1) {
                deck.offer(new Card(getColor(image), (int) num, 0));
                i += 24;
            }
        }
    }

    private void cropAll(File folder) {
        for (File f : folder.listFiles()) {
            if (f.getName().equals(".DS_Store"))
                continue;
            try {
                BufferedImage img = ImageIO.read(f);
                BufferedImage cropped = UNO.cropImage(img, new Rectangle(5, 10, 25, 40));
                ImageIO.write(cropped, "png", f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void grayScaleAll(File folder) {
        for (File f : folder.listFiles()) {
            if (f.getName().equals(".DS_Store"))
                continue;
            try {
                BufferedImage img = ImageIO.read(f);
                BufferedImage gray = grayscale(img);
                ImageIO.write(gray, "png", f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getColor(BufferedImage img) {

        int rgb = img.getRGB(10, 30);

        // Extract the red, green, and blue components
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        // System.out.println("Red: " + red + " Green: " + green + " Blue: " + blue +
        // "\n");

        if (red >= 150 && green >= 150)
            return "Yellow";
        else if (red >= 200)
            return "Red";
        else if (green >= 150)
            return "Green";
        else if (blue >= 150)
            return "Blue";
        else
            return "Black";
    }

    private float getCard(BufferedImage fileA) {
        try {
            BufferedImage loaded = fileA;
            String str = getColor(loaded);
            loaded = grayscale(loaded);
            BufferedImage img = new BufferedImage(loaded.getWidth(), loaded.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            img.getGraphics().drawImage(loaded, 0, 0, loaded.getWidth(), loaded.getHeight(), null);
            SsimCalculator ssim = new SsimCalculator(img);
            File folder = new File("High Res Img/Low Res Images");
            double maxSim = 0;
            String maxFile = "";
            for (File f : folder.listFiles()) {
                if (f.getName().equals(".DS_Store"))
                    continue;
                // System.out.println(f.getName());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedImage img2 = ImageIO.read(f);
                BufferedImage real = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
                real.getGraphics().drawImage(img2, 0, 0, img.getWidth(), img.getHeight(), null);
                ImageIO.write(real, "png", baos);
                double similarity = ssim.compareTo(baos.toByteArray());
                // System.out.println(f.getName() + " similarity: " + similarity);
                if (similarity > maxSim) {
                    maxSim = similarity;
                    maxFile = f.getName();
                }
            }
            if (maxSim <= .65) {
                maxFile = "Invalid.png";
                return -1;
            }
            // System.out.println("Most similar card: " + str + " " + maxFile + "\n");
            maxFile = maxFile.substring(0, maxFile.length() - 4);
            return Float.parseFloat(maxFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private float compareImage(BufferedImage biA, BufferedImage biB) {

        float percentage = 0;
        try {
            // take buffer data from both image files //
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            int count = 0;
            // compare data-buffer objects //
            if (sizeA == sizeB) {

                for (int i = 0; i < sizeA; i++) {

                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count = count + 1;
                    }

                }
                percentage = (count * 100) / sizeA;
            } else {
                System.out.println("Both the images are not of same size");
            }

        } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
        }
        return percentage;
    }

    private static BufferedImage grayscale(BufferedImage image) {
        // Create a new BufferedImage with the same dimensions as the original image
        BufferedImage grayscaleImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        grayscaleImage.getGraphics().drawImage(image, 0, 0, null);
        return grayscaleImage;
    }

}