package org.kobjects.pim;

public class VCard extends PimItem
{
  public VCard()
  {
  }

  public VCard(VCard paramVCard)
  {
    super(paramVCard);
  }

  public String getType()
  {
    return "vcard";
  }

  public int getArraySize(String paramString)
  {
    if (paramString.equals("n"))
      return 5;
    if (paramString.equals("adr"))
      return 6;
    return -1;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.pim.VCard
 * JD-Core Version:    0.6.2
 */