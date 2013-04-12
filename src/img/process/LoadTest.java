package img.process;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class LoadTest {  

    public void loadAndDisplayImage(JFrame frame) {  
        // Load the img  
        BufferedImage loadImg = ImageUtil.loadImage("C:\\Documents and Settings\\user\\My Documents\\Downloads\\seatbeltdesign.jpg");  
        frame.setBounds(0, 0, loadImg.getWidth(), loadImg.getHeight());  
        // Set the panel visible and add it to the frame  
        frame.setVisible(true);  
        // Get the surfaces Graphics object  
        Graphics2D g = (Graphics2D)frame.getRootPane().getGraphics();  
        // Now draw the image  
        g.drawImage(loadImg, null, 0, 0);  
          
    } 
    
    public static void main(String[] args) {  
        LoadTest ia = new LoadTest();  
        JFrame frame = new JFrame("Tutorials");  
        ia.loadAndDisplayImage(frame);  
    }  
      
}  