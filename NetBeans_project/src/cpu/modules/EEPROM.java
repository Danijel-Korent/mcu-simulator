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
    
    private int[] data = new int[64];

    private final int writeSeq1 = 0x55;
    private final int writeSeq2 = 0xAA;
    private final int waitWriteBit = 0xBB;
    private int sequenceState = 0;
    
    
    public EEPROM() 
    {
        for( int value : data)
        {
            value = 0;
        }
        
        sequenceState = writeSeq1;
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
        
        // if write in progress
        //      cnt--;
        //      if cnt == 0 and regAdr < 65
        //          data[regAdr] = regEEDATA;
        //          intCtrl.setIntFlag()
        //          regEECON1 -> setIntFlag
        //          clearWriteBit();
        
    }
    
    
    /************ Private methods ************/
    
    private void onRead()
    {
        // if adr <= 64
        //      regEEDATA = data[regAdr];
        //      clearReadBit();
    }
    
    private void onWrite()
    {
        if( sequenceState ==  waitWriteBit)
        {
            if( getWriteEnabled() )
            {
                // set timer
                // set write in progress
            }
        }
    }
    
    
    private boolean getWriteEnabled()
    {
        return false;
    }
    
    private void clearReadBit()
    {
        
    }
    
    private void clearWriteBit()
    {
        
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

    public void setRegEECON1(int regEECON1) 
    {
        this.regEECON1 = regEECON1;
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
