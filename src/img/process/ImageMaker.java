package img.process;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ImageMaker {  

    public static BufferedImage createImage() {  
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);  
        img.createGraphics();  
        Graphics2D g = (Graphics2D)img.getGraphics();  
        g.setColor(Color.YELLOW);  
        g.fillRect(0, 0, 100, 100);  
          
        for(int i = 1; i < 49; i++) {  
            g.setColor(new Color(5*i, 5*i, 4+1*2+i*3));  
            g.fillRect(2*i, 2*i, 3*i, 3*1);  
        }  
        return img;  
    }  
      
    public static void main(String[] args) {  
        JFrame frame = new JFrame("Image Maker");  
        frame.addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent event) {  
                System.exit(0);  
            }  
        });  
        frame.setBounds(0, 0, 200, 200);  
        JImagePanel panel = new JImagePanel(createImage(), 50, 45);  
        frame.add(panel);  
        frame.setVisible(true);  
    }  

}  