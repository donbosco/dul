package pelibskin;

public abstract interface ListListener
{
  public abstract void listModified(ListEvent paramListEvent);

  public abstract void itemSelected(ListEvent paramListEvent);

  public abstract void itemHighlighted(ListEvent paramListEvent);

  public abstract void itemRenamed(ListEvent paramListEvent, SkinnedListItem paramSkinnedListItem, String paramString);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     pelibskin.ListListener
 * JD-Core Version:    0.6.2
 */