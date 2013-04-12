package pelib.print;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract interface PrintContentProvider
{
  public abstract boolean provideContentVisible(String paramString);

  public abstract String provideContentText(String paramString);

  public abstract boolean provideContentGraphics(String paramString, Graphics2D paramGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

  public abstract Color provideContentFillColor(String paramString);

  public abstract Color provideContentStrokeColor(String paramString);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.print.PrintContentProvider
 * JD-Core Version:    0.6.2
 */