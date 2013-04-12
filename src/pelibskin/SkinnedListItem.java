package pelibskin;

import java.awt.Graphics;

public abstract interface SkinnedListItem
{
  public abstract void draw(Graphics paramGraphics, SkinnedList paramSkinnedList, int paramInt1, int paramInt2, boolean paramBoolean);

  public abstract Object getData();

  public abstract String getLabel();
}

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.SkinnedListItem
 * JD-Core Version:    0.6.2
 */