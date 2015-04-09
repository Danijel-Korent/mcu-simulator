/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.modules;

/**
 *
 * @author koki
 */
public class EEPROM 
{
    private int regEEADR;
    private int regEEDATA;
    private int regEECON1;
    private int regEECON2;
    
    private final int dataSize = 64;
    private int[] data = new int[dataSize];

    private final int writeSeq1 = 0x55;
    private final int writeSeq2 = 0xAA;
    private final int waitWriteBit = 0xBB;
    private int sequenceState = 0;
    
    private boolean writeInProgress;
    private int writeTimer;
    private final int writeTimerDefault = 5;
    
    private InterruptController interuptController;
    
    private final int BIT_EECON1_EEIF  = 0x10;
    private final int BIT_EECON1_WRERR = 0x08;
    private final int BIT_EECON1_WREN  = 0x04;
    private final int BIT_EECON1_WR    = 0x02;
    private final int BIT_EECON1_RD    = 0x01;
    
    public EEPROM( InterruptController intCtrl) 
    {
        for( int value : data)
        {
            value = 0;
        }
        
        interuptController = intCtrl;
        
        sequenceState = writeSeq1;
        writeInProgress = false;
        writeTimer = writeTimerDefault;
    }
    
    public void onTick()
    {
        if( sequenceState == writeSeq1 && regEECON2 ==  writeSeq1)
        {
            sequenceState = writeSeq2;
        }
        else if( sequenceState == writeSeq2 && regEECON2 ==  writeSeq2 )
        {
            sequenceState = waitWriteBit;
        }
        else
        {
            sequenceState = writeSeq1;
        }
        
        if( writeInProgress )
        {
            writeTimer--;
            
            if( writeTimer <= 0)
            {
                if( regEEADR < dataSize )
                {
                    data[regEEADR] = regEEDATA;
                }
                
                setIntFlag();
                clearWriteBit();
            }
        }
    }
    
    
    /************ Private methods ************/
    
    private void onRead()
    {
        if( regEEADR < dataSize )
        {
            regEEDATA = data[regEEADR];
        }
        else
        {
            regEEDATA = 0;
        }
        
        clearReadBit();    
    }
    
    private void onWrite()
    {
        if( sequenceState ==  waitWriteBit)
        {
            if( getWriteEnabled() )
            {
                writeTimer = writeTimerDefault;
                writeInProgress = true;
            }
        }
    }
    
    
    private boolean getWriteEnabled()
    {
        return (regEECON1 & 0x4) == 0x4;
    }
    
    private void clearReadBit()
    {
        regEECON1 &= BIT_EECON1_RD;
    }
    
    private void clearWriteBit()
    {
        regEECON1 &= BIT_EECON1_WR;
    }
    
    private void setIntFlag()
    {        
        interuptController.setInterruptFlag( InterruptController.FLAG_EEPROM, true );
        regEECON1 |= 0x10;
    }
    
    /************ Register Getters & Setters ************/
    
    public int getRegEEADR() 
    {
        return regEEADR;
    }

    public void setRegEEADR(int regEEADR) 
    {
        this.regEEADR = regEEADR;
    }

    public int getRegEEDATA() 
    {
        return regEEDATA;
    }

    public void setRegEEDATA(int regEEDATA) 
    {
        this.regEEDATA = regEEDATA;
    }

    public int getRegEECON1() 
    {
        return regEECON1;
    }

    public void setRegEECON1(int newValue) 
    {
        newValue &= 0x1F; // only first five bits settable
        int changedBits = regEECON1 ^ newValue; // Bitwise XOR 
        regEECON1 = newValue & 0x1C; // WR and RD bit only settable
        
        if( (newValue & BIT_EECON1_EEIF) == BIT_EECON1_EEIF)
        {
            setIntFlag();
        }

        if( (newValue & BIT_EECON1_WR) == BIT_EECON1_WR)
        {
            regEECON1 |= BIT_EECON1_WR;
            onWrite();
        }
        
        if( (newValue & BIT_EECON1_RD) == BIT_EECON1_RD)
        {
            regEECON1 |= BIT_EECON1_RD;
            onRead();
        }
    }
    
    public int getRegEECON2() 
    {
        return 0;
    }
    
    public void setRegEECON2(int regEECON2) 
    {
        this.regEECON2 = regEECON2;
    }
}
