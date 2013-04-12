package img.process;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ImageApp {  
  
    public void loadAndDisplayImage(JFrame frame) {  
        BufferedImage loadImg = ImageUtil.loadImage("C:\\Documents and Settings\\user\\My Documents\\Downloads\\seatbeltdesign.jpg");  
        frame.setBounds(0, 0, loadImg.getWidth(), loadImg.getHeight());  
        JImagePanel panel = new JImagePanel(loadImg, 0, 0);  
        frame.add(panel);  
        frame.setVisible(true);  
    }  
    public static void main(String[] args) {  
        ImageApp ia = new ImageApp();  
        JFrame frame = new JFrame("Tutorials");  
        ia.loadAndDisplayImage(frame);  
    }  
}