/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.functionRegisters;

import cpu.modules.Timer;
import cpu.registers.Register8b_Base;

/**
 *
 * @author dkorent
 */
public class RegisterOption extends Register8b_Base
{
    public static final short PS     = 0b00000111;
    public static final short PSA    = 0b00001000;    
    public static final short T0SE   = 0b00010000;
    public static final short T0CS   = 0b00100000;
    public static final short INTEDG = 0b01000000;    
    public static final short RBPU   = 0b10000000;

    
    private int regValue;
    private Timer timer;
    
    public RegisterOption( Timer tmr) 
    {
        super();
        
        timer = tmr;
        this.set( 0xFF );
    }
    
    
    @Override
    public int get() 
    {
        return regValue;
    }

    @Override
    final public void set(int newValue) 
    {
        int changedBits = regValue ^ newValue; // Bitwise XOR 
        
        if((changedBits & PS) != 0)
        {
            timer.setPrescalerSetting(newValue & PS);
        }
        
        if((changedBits & PSA) != 0)
        {
            timer.setPrescalerActive( (newValue & PSA) == 0 );
        }
        
        if((changedBits & T0CS) != 0)
        {
            // Timer clock source select
        }
        
        regValue = newValue;
    }
}
