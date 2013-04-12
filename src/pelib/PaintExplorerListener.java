package pelib;

public abstract interface PaintExplorerListener
{
  public abstract void onMaskEvent(PaintExplorerMaskEvent paramPaintExplorerMaskEvent);

  public abstract void onScissorsEvent(PaintExplorerScissorsEvent paramPaintExplorerScissorsEvent);

  public abstract void onHistoryEvent(PaintExplorerHistoryEvent paramPaintExplorerHistoryEvent);

  public abstract void onProgress(PaintExplorerProgressEvent paramPaintExplorerProgressEvent);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.PaintExplorerListener
 * JD-Core Version:    0.6.2
 */