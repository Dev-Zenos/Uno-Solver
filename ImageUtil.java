import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *	Image Utilites for Images
 *
 *	@author	Nikunj Bafna
 *	@since Decemeber 27, 2023
 */
public class ImageUtil {
    /**
     * Opens the folder and grabs the images of nameIndex.
     * @param filePath path of the folder the images are in
     * @param nameIndex index of the images name ie: image for images(image1, image2)
     * @param amt amount of images to grab
     * @return 		arraylist of all the images
     * @throws IOException 
     */
    public static BufferedImage[] grabImages(String filePath, String nameIndex, int amt) throws IOException{
        BufferedImage[] images = new BufferedImage[amt];
        for(int i = 0; i < amt; i++){
            try {
                String imagePath = filePath + nameIndex + i + ".png";
                File file2 = new File(imagePath);
                BufferedImage image = ImageIO.read(file2 ); 
                images[i] = image;
            }
            catch(IllegalArgumentException e){
                System.out.println("The image was not found.");
            }
        }
        return images;
    }

    public static boolean write (BufferedImage image, String path) {
        try {
            ImageIO.write(image, "png", new File(path));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static BufferedImage read (String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();                
        }
        return image;
    }
}
