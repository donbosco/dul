package img.process;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ImageViewer extends JFrame {

	public ImageViewer(String fileName) {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName ;
		
		if(args.length==0)
			fileName = "C:\\Documents and Settings\\user\\My Documents\\Downloads\\seatbeltdesign.jpg";
		else
			fileName = args[0];
		
		new ImageViewer(fileName);

	}

	public Image loadFile(String name) {
		Image image = Toolkit.getDefaultToolkit().getImage(name);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(image, 1);
		
		try {
			mediaTracker.waitForAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
 		return image;
	}
	
}
