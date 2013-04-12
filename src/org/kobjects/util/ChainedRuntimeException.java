package org.kobjects.util;

public class ChainedRuntimeException extends RuntimeException
{
  Exception chain;

  public static ChainedRuntimeException create(Exception paramException, String paramString)
  {
    try
    {
      return ((ChainedRuntimeException)Class.forName("org.kobjects.util.ChainedRuntimeExceptionSE").newInstance())._create(paramException, paramString);
    }
    catch (Exception localException)
    {
    }
    return new ChainedRuntimeException(paramException, paramString);
  }

  ChainedRuntimeException()
  {
  }

  ChainedRuntimeException(Exception paramException, String paramString)
  {
    super((paramString == null ? "rethrown" : paramString) + ": " + paramException.toString());
    this.chain = paramException;
  }

  ChainedRuntimeException _create(Exception paramException, String paramString)
  {
    throw new RuntimeException("ERR!");
  }

  public Exception getChained()
  {
    return this.chain;
  }

  public void printStackTrace()
  {
    super.printStackTrace();
    if (this.chain != null)
      this.chain.printStackTrace();
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.kobjects.util.ChainedRuntimeException
 * JD-Core Version:    0.6.2
 */