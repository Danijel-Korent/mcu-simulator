/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peripherals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 *
 * @author koki
 */
public class SevenSegDisplay 
{ 
    private Image[] imgSegmentHorisontal = new Image[2];
    private Image[] imgSegmentVertical   = new Image[2];
    private Image imgPicDisp;
    
    private Image[] segImages = new Image[7];
    
    //    0 
    //  1   2
    //    3
    //  4   5
    //    6
            
    static int[][] encodingDigitHex = new int[][]
    {
    //  0  1  2  3  4  5  6
      { 1, 1, 1, 0, 1, 1, 1 },  // 0
      { 0, 0, 1, 0, 0, 1, 0 },  // 1
      { 1, 0, 1, 1, 1, 0, 1 },  // 2
      { 1, 0, 1, 1, 0, 1, 1 },  // 3
      { 0, 1, 1, 1, 0, 1, 0 },  // 4
      { 1, 1, 0, 1, 0, 1, 1 },  // 5
      { 1, 1, 0, 1, 1, 1, 1 },  // 6
      { 1, 0, 1, 0, 0, 1, 0 },  // 7
      { 1, 1, 1, 1, 1, 1, 1 },  // 8
      { 1, 1, 1, 1, 0, 1, 1 },  // 9
      { 1, 1, 1, 1, 1, 1, 0 },  // A
      { 0, 0, 0, 1, 0, 0, 0 },  // x
      { 0, 0, 0, 1, 0, 0, 0 },  // x
      { 0, 0, 0, 1, 0, 0, 0 },  // x
      { 0, 0, 0, 1, 0, 0, 0 },  // x
      { 0, 0, 0, 1, 0, 0, 0 },  // x   
    };
    
    // ToDo:  Slike mogu biti static i biti inicializirane u "Static Initialization Block"
    static
    {
    }
    
    public SevenSegDisplay() 
    {
        URL input;
        Image imageH_white = null;
        Image imageV_white = null;

        Image imageH_red = null;
        Image imageV_red = null;

        try 
        {            
           input = getClass().getResource("/gui/images/hSegment-white.png");
           imageH_white = ImageIO.read( input );

           input = getClass().getResource("/gui/images/vSegment-white.png");
           imageV_white = ImageIO.read( input );

           input = getClass().getResource("/gui/images/hSegment-red.png");
           imageH_red = ImageIO.read( input );

           input = getClass().getResource("/gui/images/vSegment-red.png");
           imageV_red = ImageIO.read( input );
           
           input = getClass().getResource("/gui/images/PIC-7seg.png");
           imgPicDisp = ImageIO.read( input );
        } 
        catch (IOException ex) 
        {
            // Add handling
        }
       
       
        imageH_white = makeColorTransparent(imageH_white, Color.WHITE);
        imageV_white = makeColorTransparent(imageV_white, Color.WHITE);

        imageH_red = makeColorTransparent(imageH_red, Color.WHITE);
        imageV_red = makeColorTransparent(imageV_red, Color.WHITE);        
        
        //imgPicDisp = makeColorTransparent(imgPicDisp, Color.WHITE);

        
        imgSegmentHorisontal[0] = imageH_white;
        imgSegmentHorisontal[1] = imageH_red;
        
        imgSegmentVertical[0] = imageV_white;
        imgSegmentVertical[1] = imageV_red;
        
        
        set(0);
    }
    
    public void set(int value)
    {
        segImages[0] = imgSegmentHorisontal[ encodingDigitHex[value][0] ];
        segImages[1] = imgSegmentVertical  [ encodingDigitHex[value][1] ];
        segImages[2] = imgSegmentVertical  [ encodingDigitHex[value][2] ];
        segImages[3] = imgSegmentHorisontal[ encodingDigitHex[value][3] ];
        segImages[4] = imgSegmentVertical  [ encodingDigitHex[value][4] ];
        segImages[5] = imgSegmentVertical  [ encodingDigitHex[value][5] ];
        segImages[6] = imgSegmentHorisontal[ encodingDigitHex[value][6] ];
        
        onChange();
    }
    
    // buduci callback za pozivanje redraw-a ( jPanelPeripherals.repaint() )
    // Anonymous inner class -> http://stackoverflow.com/questions/355167/how-are-anonymous-inner-classes-used-in-java
    // Interface -> http://java-buddy.blogspot.de/2013/02/implement-callback-method-using.html
    public void onChange()
    {
        
    }
    
    // ToDo: hardcoded values!!
    public void draw(Graphics g)
    {
        int x = 100;
        int y = 100;
        
        g.drawImage(imgPicDisp, x - 94, y - 48, null);
        
        g.drawImage(segImages[0], x, y, null); 
        
        g.drawImage(segImages[1], x - 15, y + 15, null); 
        g.drawImage(segImages[2], x + 63, y + 15, null); 
        
        g.drawImage(segImages[3], x, y + 78, null); 
        
        g.drawImage(segImages[4], x - 15, y + 15 + 78, null); 
        g.drawImage(segImages[5], x + 63, y + 15 + 78, null); 
        
        g.drawImage(segImages[6], x, y + 78*2, null);     
    }
    
    
    private static Image makeColorTransparent (Image im, final Color color) 
    {
        ImageFilter filter = new RGBImageFilter() 
        {
            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) 
            {
                if ( ( rgb | 0xFF000000 ) == markerRGB ) 
                {
                  // Mark the alpha bits as zero - transparent
                  return 0x00FFFFFF & rgb;
                }
                else 
                {
                  // nothing to do
                  return rgb;
                }
            }
        }; 

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
    
}
