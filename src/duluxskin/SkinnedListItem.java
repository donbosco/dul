package duluxskin;

import java.awt.Graphics;

public abstract interface SkinnedListItem
{
  public abstract void draw(Graphics paramGraphics, SkinnedList paramSkinnedList, int paramInt1, int paramInt2, boolean paramBoolean);

  public abstract Object getData();

  public abstract String getLabel();

  public abstract Object clone();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     duluxskin.SkinnedListItem
 * JD-Core Version:    0.6.2
 */