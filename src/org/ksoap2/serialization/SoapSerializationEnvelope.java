package org.ksoap2.serialization;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class SoapSerializationEnvelope extends SoapEnvelope
{
  protected static final int QNAME_TYPE = 1;
  protected static final int QNAME_NAMESPACE = 0;
  protected static final int QNAME_MARSHAL = 3;
  private static final String ANY_TYPE_LABEL = "anyType";
  private static final String ARRAY_MAPPING_NAME = "Array";
  private static final String NULL_LABEL = "null";
  private static final String NIL_LABEL = "nil";
  private static final String HREF_LABEL = "href";
  private static final String ID_LABEL = "id";
  private static final String ROOT_LABEL = "root";
  private static final String TYPE_LABEL = "type";
  private static final String ITEM_LABEL = "item";
  private static final String ARRAY_TYPE_LABEL = "arrayType";
  static final Marshal DEFAULT_MARSHAL = new DM();
  public Hashtable properties = new Hashtable();
  Hashtable idMap = new Hashtable();
  Vector multiRef;
  public boolean implicitTypes;
  public boolean dotNet;
  protected Hashtable qNameToClass = new Hashtable();
  protected Hashtable classToQName = new Hashtable();

  public SoapSerializationEnvelope(int paramInt)
  {
    super(paramInt);
    addMapping(this.enc, "Array", PropertyInfo.VECTOR_CLASS);
    DEFAULT_MARSHAL.register(this);
  }

  public void parseBody(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    this.bodyIn = null;
    //paramXmlPullParser.nextTag();
    Object localObject1;
    if ((paramXmlPullParser.getEventType() == 2) && (paramXmlPullParser.getNamespace().equals(this.env)) && (paramXmlPullParser.getName().equals("Fault")))
    {
      localObject1 = new SoapFault();
      ((SoapFault)localObject1).parse(paramXmlPullParser);
      this.bodyIn = localObject1;
    }
    else
    {
      while (paramXmlPullParser.getEventType() == 2)
      {
        localObject1 = paramXmlPullParser.getAttributeValue(this.enc, "root");
        Object localObject2 = read(paramXmlPullParser, null, -1, paramXmlPullParser.getNamespace(), paramXmlPullParser.getName(), PropertyInfo.OBJECT_TYPE);
        if (("1".equals(localObject1)) || (this.bodyIn == null))
          this.bodyIn = localObject2;
        //paramXmlPullParser.nextTag();
      }
    }
  }

  protected void readSerializable(XmlPullParser paramXmlPullParser, KvmSerializable paramKvmSerializable)
    throws IOException, XmlPullParserException
  {
    int i = -1;
    int j = paramKvmSerializable.getPropertyCount();
    PropertyInfo localPropertyInfo = new PropertyInfo();
    while (paramXmlPullParser.nextTag() != 3)
    {
      String str = paramXmlPullParser.getName();
      int k = j;
      while (true)
      {
        if (k-- == 0)
          throw new RuntimeException("Unknown Property: " + str);
        i++;
        if (i >= j)
          i = 0;
        paramKvmSerializable.getPropertyInfo(i, this.properties, localPropertyInfo);
        if (((localPropertyInfo.namespace != null) || (!str.equals(localPropertyInfo.name))) && ((localPropertyInfo.name != null) || (i != 0)))
          if ((str.equals(localPropertyInfo.name)) && (paramXmlPullParser.getNamespace().equals(localPropertyInfo.namespace)))
            break;
      }
      paramKvmSerializable.setProperty(i, read(paramXmlPullParser, paramKvmSerializable, i, null, null, localPropertyInfo));
    }
    paramXmlPullParser.require(3, null, null);
  }

  protected Object readUnknown(XmlPullParser paramXmlPullParser, String paramString1, String paramString2)
    throws IOException, XmlPullParserException
  {
    String str1 = paramXmlPullParser.getName();
    String str2 = paramXmlPullParser.getNamespace();
    paramXmlPullParser.next();
    Object localObject = null;
    String str3 = null;
    if (paramXmlPullParser.getEventType() == 4)
    {
      str3 = paramXmlPullParser.getText();
      localObject = new SoapPrimitive(paramString1, paramString2, str3);
      paramXmlPullParser.next();
    }
    else if (paramXmlPullParser.getEventType() == 3)
    {
      localObject = new SoapObject(paramString1, paramString2);
    }
    if (paramXmlPullParser.getEventType() == 2)
    {
      if ((str3 != null) && (str3.trim().length() != 0))
        throw new RuntimeException("Malformed input: Mixed content");
      SoapObject localSoapObject = new SoapObject(paramString1, paramString2);
      while (paramXmlPullParser.getEventType() != 3)
      {
        localSoapObject.addProperty(paramXmlPullParser.getName(), read(paramXmlPullParser, localSoapObject, localSoapObject.getPropertyCount(), null, null, PropertyInfo.OBJECT_TYPE));
        paramXmlPullParser.nextTag();
      }
      localObject = localSoapObject;
    }
    paramXmlPullParser.require(3, str2, str1);
    return localObject;
  }

  private int getIndex(String paramString, int paramInt1, int paramInt2)
  {
    if (paramString == null)
      return paramInt2;
    return paramString.length() - paramInt1 < 3 ? paramInt2 : Integer.parseInt(paramString.substring(paramInt1 + 1, paramString.length() - 1));
  }

  protected void readVector(XmlPullParser paramXmlPullParser, Vector paramVector, PropertyInfo paramPropertyInfo)
    throws IOException, XmlPullParserException
  {
    String str1 = null;
    String str2 = null;
    int i = paramVector.size();
    int j = 1;
    String str3 = paramXmlPullParser.getAttributeValue(this.enc, "arrayType");
    if (str3 != null)
    {
      int k = str3.indexOf(':');
      int m = str3.indexOf("[", k);
      str2 = str3.substring(k + 1, m);
      String str4 = k == -1 ? "" : str3.substring(0, k);
      str1 = paramXmlPullParser.getNamespace(str4);
      i = getIndex(str3, m, -1);
      if (i != -1)
      {
        paramVector.setSize(i);
        j = 0;
      }
    }
    if (paramPropertyInfo == null)
      paramPropertyInfo = PropertyInfo.OBJECT_TYPE;
    paramXmlPullParser.nextTag();
    int k = getIndex(paramXmlPullParser.getAttributeValue(this.enc, "offset"), 0, 0);
    while (paramXmlPullParser.getEventType() != 3)
    {
      k = getIndex(paramXmlPullParser.getAttributeValue(this.enc, "position"), 0, k);
      if ((j != 0) && (k >= i))
      {
        i = k + 1;
        paramVector.setSize(i);
      }
      paramVector.setElementAt(read(paramXmlPullParser, paramVector, k, str1, str2, paramPropertyInfo), k);
      k++;
      paramXmlPullParser.nextTag();
    }
    paramXmlPullParser.require(3, null, null);
  }

  public Object read(XmlPullParser paramXmlPullParser, Object paramObject, int paramInt, String paramString1, String paramString2, PropertyInfo paramPropertyInfo)
    throws IOException, XmlPullParserException
  {
	    String str1 = paramXmlPullParser.getName();
	    String str2 = paramXmlPullParser.getAttributeValue(null, "href");
	    Object localObject1;
	    Object localObject2;
	    if (str2 != null)
	    {
	      if (paramObject == null)
	        throw new RuntimeException("href at root level?!?");
	      str2 = str2.substring(1);
	      localObject1 = this.idMap.get(str2);
	      if ((localObject1 == null) || ((localObject1 instanceof FwdRef)))
	      {
	        localObject2 = new FwdRef();
	        ((FwdRef)localObject2).next = ((FwdRef)localObject1);
	        ((FwdRef)localObject2).obj = paramObject;
	        ((FwdRef)localObject2).index = paramInt;
	        this.idMap.put(str2, localObject2);
	        localObject1 = null;
	      }
	      paramXmlPullParser.nextTag();
	      paramXmlPullParser.require(3, null, str1);
	    }
	    else
	    {
	      localObject2 = paramXmlPullParser.getAttributeValue(this.xsi, "nil");
	      String str3 = paramXmlPullParser.getAttributeValue(null, "id");
	      if (localObject2 == null)
	        localObject2 = paramXmlPullParser.getAttributeValue(this.xsi, "null");
	      Object localObject3;
	      Object localObject4;
	      if ((localObject2 != null) && (SoapEnvelope.stringToBoolean((String)localObject2)))
	      {
	        localObject1 = null;
	        paramXmlPullParser.nextTag();
	        paramXmlPullParser.require(3, null, str1);
	      }
	      else
	      {
	        localObject3 = paramXmlPullParser.getAttributeValue(this.xsi, "type");
	        if (localObject3 != null)
	        {
	          int i = ((String)localObject3).indexOf(':');
	          paramString2 = ((String)localObject3).substring(i + 1);
	          String str4 = i == -1 ? "" : ((String)localObject3).substring(0, i);
	          paramString1 = paramXmlPullParser.getNamespace(str4);
	        }
	        else if ((paramString2 == null) && (paramString1 == null))
	        {
	          if (paramXmlPullParser.getAttributeValue(this.enc, "arrayType") != null)
	          {
	            paramString1 = this.enc;
	            paramString2 = "Array";
	          }
	          else
	          {
	            Object[] localObj4 = getInfo(paramPropertyInfo.type, null);
	            paramString1 = (String)localObj4[0];
	            paramString2 = (String)localObj4[1];
	          }
	        }
	        localObject1 = readInstance(paramXmlPullParser, paramString1, paramString2, paramPropertyInfo);
	        if (localObject1 == null)
	          localObject1 = readUnknown(paramXmlPullParser, paramString1, paramString2);
	      }
	      if (str3 != null)
	      {
	        localObject3 = this.idMap.get(str3);
	        if ((localObject3 instanceof FwdRef))
	        {
	          localObject4 = localObject3;
	          do
	          {
	            if ((((FwdRef)localObject4).obj instanceof KvmSerializable))
	              ((KvmSerializable)((FwdRef)localObject4).obj).setProperty(((FwdRef)localObject4).index, localObject1);
	            else
	              ((Vector)((FwdRef)localObject4).obj).setElementAt(localObject1, ((FwdRef)localObject4).index);
	            localObject4 = ((FwdRef)localObject4).next;
	          }
	          while (localObject4 != null);
	        }
	        else if (localObject3 != null)
	        {
	          throw new RuntimeException("double ID");
	        }
	        this.idMap.put(str3, localObject1);
	      }
	    }
	    paramXmlPullParser.require(3, null, str1);
	    return localObject1;
	  }

  public Object readInstance(XmlPullParser paramXmlPullParser, String paramString1, String paramString2, PropertyInfo paramPropertyInfo)
    throws IOException, XmlPullParserException
  {
    Object localObject = this.qNameToClass.get(new SoapPrimitive(paramString1, paramString2, null));
    if (localObject == null)
      return null;
    if ((localObject instanceof Marshal))
      return ((Marshal)localObject).readInstance(paramXmlPullParser, paramString1, paramString2, paramPropertyInfo);
    if ((localObject instanceof SoapObject))
      localObject = ((SoapObject)localObject).newInstance();
    else
      try
      {
        localObject = ((Class)localObject).newInstance();
      }
      catch (Exception localException)
      {
        throw new RuntimeException(localException.toString());
      }
    if ((localObject instanceof KvmSerializable))
      readSerializable(paramXmlPullParser, (KvmSerializable)localObject);
    else if ((localObject instanceof Vector))
      readVector(paramXmlPullParser, (Vector)localObject, paramPropertyInfo.elementType);
    else
      throw new RuntimeException("no deserializer for " + localObject.getClass());
    return localObject;
  }

  public Object[] getInfo(Object paramObject1, Object paramObject2)
  {
    if (paramObject1 == null)
      if (((paramObject2 instanceof SoapObject)) || ((paramObject2 instanceof SoapPrimitive)))
        paramObject1 = paramObject2;
      else
        paramObject1 = paramObject2.getClass();
    Object localObject;
    if ((paramObject1 instanceof SoapObject))
    {
      localObject = (SoapObject)paramObject1;
      return new Object[] { ((SoapObject)localObject).getNamespace(), ((SoapObject)localObject).getName(), null, null };
    }
    if ((paramObject1 instanceof SoapPrimitive))
    {
      localObject = (SoapPrimitive)paramObject1;
      return new Object[] { ((SoapPrimitive)localObject).getNamespace(), ((SoapPrimitive)localObject).getName(), null, DEFAULT_MARSHAL };
    }
    if (((paramObject1 instanceof Class)) && (paramObject1 != PropertyInfo.OBJECT_CLASS))
    {
      localObject = (Object[])this.classToQName.get(((Class)paramObject1).getName());
      if (localObject != null)
        return (Object[]) localObject;
    }
    return new Object[] { this.xsd, "anyType", null, null };
  }

  public void addMapping(String paramString1, String paramString2, Class paramClass, Marshal paramMarshal)
  {
    this.qNameToClass.put(new SoapPrimitive(paramString1, paramString2, null), paramMarshal == null ? paramClass : paramMarshal);
    this.classToQName.put(paramClass.getName(), new Object[] { paramString1, paramString2, null, paramMarshal });
  }

  public void addMapping(String paramString1, String paramString2, Class paramClass)
  {
    addMapping(paramString1, paramString2, paramClass, null);
  }

  public void addTemplate(SoapObject paramSoapObject)
  {
    this.qNameToClass.put(new SoapPrimitive(paramSoapObject.namespace, paramSoapObject.name, null), paramSoapObject);
  }

  public Object getResponse()
    throws SoapFault
  {
    if ((this.bodyIn instanceof SoapFault))
      throw ((SoapFault)this.bodyIn);
    KvmSerializable localKvmSerializable = (KvmSerializable)this.bodyIn;
    return localKvmSerializable.getPropertyCount() == 0 ? null : localKvmSerializable.getProperty(0);
  }

  /** @deprecated */
  public Object getResult()
  {
    KvmSerializable localKvmSerializable = (KvmSerializable)this.bodyIn;
    return localKvmSerializable.getPropertyCount() == 0 ? null : localKvmSerializable.getProperty(0);
  }

  public void writeBody(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    this.multiRef = new Vector();
    this.multiRef.addElement(this.bodyOut);
    Object[] arrayOfObject = getInfo(null, this.bodyOut);
    paramXmlSerializer.startTag(this.dotNet ? "" : (String)arrayOfObject[0], (String)arrayOfObject[1]);
    if (this.dotNet)
      paramXmlSerializer.attribute(null, "xmlns", (String)arrayOfObject[0]);
    paramXmlSerializer.attribute(null, "id", arrayOfObject[2] == null ? "o0" : (String)arrayOfObject[2]);
    paramXmlSerializer.attribute(this.enc, "root", "1");
    writeElement(paramXmlSerializer, this.bodyOut, null, arrayOfObject[3]);
    paramXmlSerializer.endTag(this.dotNet ? "" : (String)arrayOfObject[0], (String)arrayOfObject[1]);
  }

  public void writeObjectBody(XmlSerializer paramXmlSerializer, KvmSerializable paramKvmSerializable)
    throws IOException
  {
    PropertyInfo localPropertyInfo = new PropertyInfo();
    int i = paramKvmSerializable.getPropertyCount();
    for (int j = 0; j < i; j++)
    {
      paramKvmSerializable.getPropertyInfo(j, this.properties, localPropertyInfo);
      if ((localPropertyInfo.flags & 0x1) == 0)
      {
        paramXmlSerializer.startTag(localPropertyInfo.namespace, localPropertyInfo.name);
        writeProperty(paramXmlSerializer, paramKvmSerializable.getProperty(j), localPropertyInfo);
        paramXmlSerializer.endTag(localPropertyInfo.namespace, localPropertyInfo.name);
      }
    }
  }

  protected void writeProperty(XmlSerializer paramXmlSerializer, Object paramObject, PropertyInfo paramPropertyInfo)
    throws IOException
  {
    if (paramObject == null)
    {
      paramXmlSerializer.attribute(this.xsi, this.version >= 120 ? "nil" : "null", "true");
      return;
    }
    Object[] arrayOfObject = getInfo(null, paramObject);
    if ((paramPropertyInfo.multiRef) || (arrayOfObject[2] != null))
    {
      int i = this.multiRef.indexOf(paramObject);
      if (i == -1)
      {
        i = this.multiRef.size();
        this.multiRef.addElement(paramObject);
      }
      paramXmlSerializer.attribute(null, "href", "#" + arrayOfObject[2]);
    }
    else
    {
      if ((!this.implicitTypes) || (paramObject.getClass() != paramPropertyInfo.type))
      {
        String str = paramXmlSerializer.getPrefix((String)arrayOfObject[0], true);
        paramXmlSerializer.attribute(this.xsi, "type", str + ":" + arrayOfObject[1]);
      }
      writeElement(paramXmlSerializer, paramObject, paramPropertyInfo, arrayOfObject[3]);
    }
  }

  private void writeElement(XmlSerializer paramXmlSerializer, Object paramObject1, PropertyInfo paramPropertyInfo, Object paramObject2)
    throws IOException
  {
    if (paramObject2 != null)
      ((Marshal)paramObject2).writeInstance(paramXmlSerializer, paramObject1);
    else if ((paramObject1 instanceof KvmSerializable))
      writeObjectBody(paramXmlSerializer, (KvmSerializable)paramObject1);
    else if ((paramObject1 instanceof Vector))
      writeVectorBody(paramXmlSerializer, (Vector)paramObject1, paramPropertyInfo.elementType);
    else
      throw new RuntimeException("Cannot serialize: " + paramObject1);
  }

  protected void writeVectorBody(XmlSerializer paramXmlSerializer, Vector paramVector, PropertyInfo paramPropertyInfo)
    throws IOException
  {
    if (paramPropertyInfo == null)
      paramPropertyInfo = PropertyInfo.OBJECT_TYPE;
    int i = paramVector.size();
    Object[] arrayOfObject = getInfo(paramPropertyInfo.type, null);
    paramXmlSerializer.attribute(this.enc, "arrayType", paramXmlSerializer.getPrefix((String)arrayOfObject[0], false) + ":" + arrayOfObject[1] + "[" + i + "]");
    int j = 0;
    for (int k = 0; k < i; k++)
      if (paramVector.elementAt(k) == null)
      {
        j = 1;
      }
      else
      {
        paramXmlSerializer.startTag(null, "item");
        if (j != 0)
        {
          paramXmlSerializer.attribute(this.enc, "position", "[" + k + "]");
          j = 0;
        }
        writeProperty(paramXmlSerializer, paramVector.elementAt(k), paramPropertyInfo);
        paramXmlSerializer.endTag(null, "item");
      }
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.serialization.SoapSerializationEnvelope
 * JD-Core Version:    0.6.2
 */