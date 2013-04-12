/* $Id: DuluxMyColourApplet.java,v 1.1.1.3 2009-06-03 01:19:17 jlee Exp $ */

/** Don't package me.  Some browsers can't access applets in packages. */

import dulux.*;
import java.awt.*;
import java.applet.*;
import java.io.*;

/**
 *
 */
public class DuluxMyColourApplet extends Applet
{
    public void init()
    {
        //try {
            setLayout(new BorderLayout());
            //add(new DuluxMyColour(null);
        //} catch (IOException e) {
            // TODO
       // }
    }
    
    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
        //System.out.println("Paint!");
        super.paint(g);
    }

    public void start()
    {
    }

    public void stop()
    {
    }
}
