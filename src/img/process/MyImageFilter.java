package img.process;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageFilter;

public class MyImageFilter extends ImageFilter {

	public void setHints(int hints) {
		hints |=ImageConsumer.TOPDOWNLEFTRIGHT;
		super.setHints(hints);
	}
	
	@Override
	public void setPixels(int x, int y, int w, int h, ColorModel model,
			byte[] pixels, int off, int scansize) {
	
		super.setPixels(x, y, w, h, model, pixels, off, scansize);
	}
	
	@Override
	public void setPixels(int x, int y, int w, int h, ColorModel model,
			int[] pixels, int off, int scansize) {
		
		super.setPixels(x, y, w, h, model, pixels, off, scansize);
		
	}
	
}
