package pelib.scissors;

import java.util.Vector;
import pelib.Node;

public abstract interface ScissorAlgorithm
{
  public abstract void reset();

  public abstract Node addEdges(Node paramNode1, Node paramNode2, Vector paramVector, boolean paramBoolean);
}

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.scissors.ScissorAlgorithm
 * JD-Core Version:    0.6.2
 */