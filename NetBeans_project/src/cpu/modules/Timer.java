/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.modules;

/**
 *
 * @author dkorent
 */
public class Timer 
{
    private int counter;
    private int prescaler;
    
    public Timer()
    {
        counter = 0;
        prescaler = 0;
    }

    public void OnTick()
    {
        // ToDo: add prescaler logic
        counter++;
        
        if ( counter > 255 )
        {
            counter = 0;
            // ToDo signal overflow
        }
    }
    
    public int Get()
    {
        return counter;
    }
    
    public void Set( int val)
    {
        if (counter < 0 || counter > 255) throw new IllegalArgumentException("Nedopustena vrijednost argumenta r: " + val);
        
        counter = val;
        prescaler = 0; // datasheet, str 21.: Writing to TMR0 when the prescaler is assigned to Timer0 will clear the prescaler count
    }
}
