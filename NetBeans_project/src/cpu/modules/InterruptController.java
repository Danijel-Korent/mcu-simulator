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
public class InterruptController 
{
    private int regIntcon;

    
    public InterruptController() 
    {
        regIntcon = 0;
    }
    
    public boolean isInterrupted()
    {
        return false;
    }
    
    public int getRegIntcon()
    {
        return 0;
    }
    
    public void setRegIntcon( int newVal)
    {
        
    }
}
