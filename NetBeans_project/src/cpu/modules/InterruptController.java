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

    public static final int FLAG_PORTB   = 0;
    public static final int FLAG_RB0     = 1;
    public static final int FLAG_TIMER   = 2;
    public static final int FLAG_EEPROM  = 3;
    
    private static final int BIT_PORTB_INT = 1 << 0;
    private static final int BIT_RB0_INT   = 1 << 1;
    private static final int BIT_TIMER_INT = 1 << 2;
    private static final int BIT_PORTB_EN  = 1 << 3;
    private static final int BIT_RB0_EN    = 1 << 4;
    private static final int BIT_TIMER_EN  = 1 << 5;
    private static final int BIT_EEPROM_EN = 1 << 5;
    private static final int BIT_GLOBAL_EN = 1 << 7;
      
    
    private final int MAX_INT_SOURCES = 4;
    
    private boolean[] intFlag;
    private boolean[] intEnable;
    private boolean globalEnabled;
    
    private int regIntcon;
    
    public InterruptController() 
    {
        intFlag = new boolean[MAX_INT_SOURCES];
        intEnable = new boolean[MAX_INT_SOURCES];
        globalEnabled = false;
        
        regIntcon = 0;
    }

    public boolean isInterrupted()
    {
        boolean retVal = false;
        
        if ( globalEnabled )
        {
            for( int i=0; i < MAX_INT_SOURCES; i++)
            {
                if ( intFlag[i] && intEnable[i] )
                {
                    retVal = true;
                }
            }
        }
        
        return retVal;
    }
    
    public boolean getGlobalEnable() 
    {
        return globalEnabled;
    }

    public void setGlobalEnable( boolean globalMask ) 
    {
        this.globalEnabled = globalMask;
        
        if( globalMask )
        {
            regIntcon |= BIT_GLOBAL_EN;
        }
        else
        {
            regIntcon &= ~BIT_GLOBAL_EN;
        }
    }
    
    public void setInterruptFlag( int index, boolean value )
    {
        if (index >= MAX_INT_SOURCES) throw new IllegalArgumentException("Too big intFlag index: " + index);
        
        intFlag[index] = value;
        
        // update value of INTCON register
        regIntcon &= 0b111;
        if( intFlag[FLAG_PORTB] ) regIntcon |= BIT_PORTB_INT;
        if( intFlag[FLAG_RB0] )   regIntcon |= BIT_RB0_INT;
        if( intFlag[FLAG_TIMER] ) regIntcon |= BIT_TIMER_INT;
    }
    
    public boolean getInterruptFlag( int index )
    {
        if (index >= MAX_INT_SOURCES) throw new IllegalArgumentException("Too big intFlag index: " + index);
        
        return intFlag[index];
    }
    
    public void setInterruptEnable( int index, boolean value )
    {
        if (index >= MAX_INT_SOURCES) throw new IllegalArgumentException("Too big intMask index: " + index);
        
        intEnable[index] = value;
        
        // update value of INTCON register
        regIntcon &= 0b01111000;
        if( intEnable[FLAG_PORTB] )  regIntcon |= BIT_PORTB_EN;
        if( intEnable[FLAG_RB0] )    regIntcon |= BIT_RB0_EN;
        if( intEnable[FLAG_TIMER] )  regIntcon |= BIT_TIMER_EN;
        if( intEnable[FLAG_EEPROM] ) regIntcon |= BIT_EEPROM_EN;
    }
    
    public boolean getInterruptEnable( int index )
    {
        if (index >= MAX_INT_SOURCES) throw new IllegalArgumentException("Too big intMask index: " + index);
        
        return intEnable[index];
    }
    
    public int getRegIntcon()
    {
        return regIntcon;
    }
    
    public void setRegIntcon( int newValue )
    {
        int changedBits = regIntcon ^ newValue; // Bitwise XOR 
        

        if((changedBits & BIT_PORTB_INT) != 0)
        {
            this.setGlobalEnable((newValue & BIT_PORTB_INT) != 0);
        }
        
        if((changedBits & BIT_RB0_INT) != 0)
        {
            this.setGlobalEnable((newValue & BIT_RB0_INT) != 0);
        }
        
        if((changedBits & BIT_TIMER_INT) != 0)
        {
            this.setGlobalEnable((newValue & BIT_TIMER_INT) != 0);
        }
        
        if((changedBits & BIT_PORTB_EN) != 0)
        {
            this.setGlobalEnable((newValue & BIT_PORTB_EN) != 0);
        }
        
        if((changedBits & BIT_RB0_EN) != 0)
        {
            this.setGlobalEnable((newValue & BIT_RB0_EN) != 0);
        }
        
        if((changedBits & BIT_TIMER_EN) != 0)
        {
            this.setGlobalEnable((newValue & BIT_TIMER_EN) != 0);
        }
        
        if((changedBits & BIT_EEPROM_EN) != 0)
        {
            this.setGlobalEnable((newValue & BIT_EEPROM_EN) != 0);
        }
        
        if((changedBits & BIT_GLOBAL_EN) != 0)
        {
            this.setGlobalEnable((newValue & BIT_GLOBAL_EN) != 0);
        }

        // NOTE: no need to change regIntcon, it's  changed inside set* function calls
    }
}
