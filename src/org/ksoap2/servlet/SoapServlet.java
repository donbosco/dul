package org.ksoap2.servlet;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

public class SoapServlet extends HttpServlet
{
  SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(120);
  Hashtable instanceMap = new Hashtable();

  protected Object getInstance(HttpServletRequest paramHttpServletRequest)
  {
    if (paramHttpServletRequest.getPathInfo() == null)
      return this;
    Object localObject = this.instanceMap.get(paramHttpServletRequest.getPathInfo());
    return localObject != null ? localObject : this;
  }

  public void publishClass(Class paramClass, String paramString)
  {
    Method[] arrayOfMethod = paramClass.getMethods();
    for (int i = 0; i < arrayOfMethod.length; i++)
      if (Modifier.isPublic(arrayOfMethod[i].getModifiers()))
      {
        Class[] arrayOfClass = arrayOfMethod[i].getParameterTypes();
        PropertyInfo[] arrayOfPropertyInfo = new PropertyInfo[arrayOfClass.length];
        for (int j = 0; j < arrayOfClass.length; j++)
        {
          arrayOfPropertyInfo[j] = new PropertyInfo();
          arrayOfPropertyInfo[j].type = arrayOfClass[j];
        }
        publishMethod(paramClass, paramString, arrayOfMethod[i].getName(), arrayOfPropertyInfo);
      }
  }

  public void publishInstance(String paramString, Object paramObject)
  {
    this.instanceMap.put(paramString, paramObject);
  }

  public void publishMethod(Class paramClass, String paramString1, String paramString2, PropertyInfo[] paramArrayOfPropertyInfo)
  {
    SoapObject localSoapObject = new SoapObject(paramString1, paramString2);
    for (int i = 0; i < paramArrayOfPropertyInfo.length; i++)
      localSoapObject.addProperty(paramArrayOfPropertyInfo[i], null);
    this.envelope.addTemplate(localSoapObject);
  }

  public void publishMethod(Class paramClass, String paramString1, String paramString2, String[] paramArrayOfString)
  {
    Method[] arrayOfMethod = paramClass.getMethods();
    for (int i = 0; i < arrayOfMethod.length; i++)
      if ((arrayOfMethod[i].getName().equals(paramString2)) && (arrayOfMethod[i].getParameterTypes().length == paramArrayOfString.length))
      {
        Class[] arrayOfClass = arrayOfMethod[i].getParameterTypes();
        PropertyInfo[] arrayOfPropertyInfo = new PropertyInfo[arrayOfClass.length];
        for (int j = 0; j < arrayOfClass.length; j++)
        {
          arrayOfPropertyInfo[j] = new PropertyInfo();
          arrayOfPropertyInfo[j].name = paramArrayOfString[j];
          arrayOfPropertyInfo[j].type = arrayOfClass[j];
        }
        publishMethod(paramClass, paramString1, paramString2, arrayOfPropertyInfo);
        return;
      }
    throw new RuntimeException("Method not found!");
  }

  public SoapSerializationEnvelope getEnvelope()
  {
    return this.envelope;
  }

  public void setEnvelope(SoapSerializationEnvelope paramSoapSerializationEnvelope)
  {
    this.envelope = paramSoapSerializationEnvelope;
  }

  public void doPost(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException, IOException
  {
	    Object localObject2;
		try
	    {
	      Object localObject1 = getInstance(paramHttpServletRequest);
	      localObject2 = new KXmlParser();
	      ((XmlPullParser)localObject2).setInput(paramHttpServletRequest.getInputStream(), paramHttpServletRequest.getCharacterEncoding());
	      ((XmlPullParser)localObject2).setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
	      this.envelope.parse((XmlPullParser)localObject2);
	      SoapObject localSoapObject1 = (SoapObject)this.envelope.bodyIn;
	      SoapObject localSoapObject2 = invoke(localObject1, localSoapObject1);
	      System.out.println("result: " + localSoapObject2);
	      this.envelope.bodyOut = localSoapObject2;
	      paramHttpServletResponse.setContentType("text/xml; charset=utf-8");
	      paramHttpServletResponse.setHeader("Connection", "close");
	      localObject1 = new StringWriter();
	      localObject2 = new KXmlSerializer();
	      ((XmlSerializer)localObject2).setOutput((Writer)localObject1);
	      try
	      {
	        this.envelope.write((XmlSerializer)localObject2);
	      }
	      catch (Exception localException1)
	      {
	        localException1.printStackTrace();
	      }
	      ((XmlSerializer)localObject2).flush();
	      System.out.println("result xml: " + localObject1);
	      PrintWriter localPrintWriter1 = paramHttpServletResponse.getWriter();
	      localPrintWriter1.write(((StringWriter)localObject1).toString());
	      localPrintWriter1.close();
	    }
	    catch (SoapFault localSoapFault)
	    {
	      localSoapFault.printStackTrace();
	      this.envelope.bodyOut = localSoapFault;
	      paramHttpServletResponse.setStatus(500);
	      paramHttpServletResponse.setContentType("text/xml; charset=utf-8");
	      paramHttpServletResponse.setHeader("Connection", "close");
	      StringWriter localStringWriter1 = new StringWriter();
	      localObject2 = new KXmlSerializer();
	      ((XmlSerializer)localObject2).setOutput(localStringWriter1);
	      try
	      {
	        this.envelope.write((XmlSerializer)localObject2);
	      }
	      catch (Exception localException2)
	      {
	        localException2.printStackTrace();
	      }
	      ((XmlSerializer)localObject2).flush();
	      System.out.println("result xml: " + localStringWriter1);
	      PrintWriter localPrintWriter2 = paramHttpServletResponse.getWriter();
	      localPrintWriter2.write(localStringWriter1.toString());
	      localPrintWriter2.close();
	    }
	    catch (Throwable localThrowable)
	    {
	      localThrowable.printStackTrace();
	      localObject2 = new SoapFault();
	      ((SoapFault)localObject2).faultcode = "Server";
	      ((SoapFault)localObject2).faultstring = localThrowable.getMessage();
	      this.envelope.bodyOut = localObject2;
	      paramHttpServletResponse.setStatus(500);
	      paramHttpServletResponse.setContentType("text/xml; charset=utf-8");
	      paramHttpServletResponse.setHeader("Connection", "close");
	      StringWriter localStringWriter2 = new StringWriter();
	      localObject2 = new KXmlSerializer();
	      ((XmlSerializer)localObject2).setOutput(localStringWriter2);
	      try
	      {
	        this.envelope.write((XmlSerializer)localObject2);
	      }
	      catch (Exception localException3)
	      {
	        localException3.printStackTrace();
	      }
	      ((XmlSerializer)localObject2).flush();
	      System.out.println("result xml: " + localStringWriter2);
	      PrintWriter localPrintWriter3 = paramHttpServletResponse.getWriter();
	      localPrintWriter3.write(localStringWriter2.toString());
	      localPrintWriter3.close();
	    }
	    finally
	    {
	      paramHttpServletResponse.setContentType("text/xml; charset=utf-8");
	      paramHttpServletResponse.setHeader("Connection", "close");
	      StringWriter localStringWriter3 = new StringWriter();
	      KXmlSerializer localKXmlSerializer = new KXmlSerializer();
	      localKXmlSerializer.setOutput(localStringWriter3);
	      try
	      {
	        this.envelope.write(localKXmlSerializer);
	      }
	      catch (Exception localException4)
	      {
	        localException4.printStackTrace();
	      }
	      localKXmlSerializer.flush();
	      System.out.println("result xml: " + localStringWriter3);
	      PrintWriter localPrintWriter4 = paramHttpServletResponse.getWriter();
	      localPrintWriter4.write(localStringWriter3.toString());
	      localPrintWriter4.close();
	    }
	    paramHttpServletResponse.flushBuffer();
	  }

  SoapObject invoke(Object paramObject, SoapObject paramSoapObject)
    throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
  {
    String str = paramSoapObject.getName();
    Class[] arrayOfClass = new Class[paramSoapObject.getPropertyCount()];
    Object[] arrayOfObject = new Object[paramSoapObject.getPropertyCount()];
    PropertyInfo localPropertyInfo = new PropertyInfo();
    Hashtable localHashtable = new Hashtable();
    for (int i = 0; i < arrayOfClass.length; i++)
    {
      paramSoapObject.getPropertyInfo(i, localHashtable, localPropertyInfo);
      arrayOfClass[i] = ((Class)localPropertyInfo.type);
      arrayOfObject[i] = paramSoapObject.getProperty(i);
    }
    Method localMethod = paramObject.getClass().getMethod(str, arrayOfClass);
    Object localObject = localMethod.invoke(paramObject, arrayOfObject);
    System.out.println("result:" + localObject);
    SoapObject localSoapObject = new SoapObject(paramSoapObject.getNamespace(), str + "Response");
    if (localObject != null)
      localSoapObject.addProperty("return", localObject);
    return localSoapObject;
  }
}

/* Location:           dulux-signed.jar
 * Qualified Name:     org.ksoap2.servlet.SoapServlet
 * JD-Core Version:    0.6.2
 */