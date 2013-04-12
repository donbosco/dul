package img.process;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageUtil {
	public static BufferedImage loadImage(String ref) {  
        BufferedImage bimg = null;  
        try {  
  
            bimg = ImageIO.read(new File(ref));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bimg;  
    }
}
