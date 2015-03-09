/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Graphics;
import javax.swing.JPanel;
import peripherals.SevenSegDisplay;

/**
 *
 * @author koki
 */
public class JPanelPeripherals extends JPanel
{
       
    SevenSegDisplay testDisplay;

    public JPanelPeripherals() 
    {
        testDisplay = null;
    }
    
    // Privremena funkcija za testiranje sa jednim elementom
    // Zamijenit ce se sa Add/remove funkcijom za dodavanje genericke Peripherals klase
    public void set( SevenSegDisplay disp)
    {
        testDisplay = disp;
    }
    
   
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        if  (null != testDisplay)
        {
            testDisplay.draw(g);
        }
    }
    
}
