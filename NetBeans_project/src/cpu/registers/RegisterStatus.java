package cpu.registers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Danijel Korent
 */
public class RegisterStatus extends Register8b_Standard 
{
    
    public RegisterStatus() 
    {
        super.set(0x18); // prilikom ukljucenja mikroupravljaca bitovi TO i PD imaju vrijednost "1", datasheet str.7
    }

    
    @Override
    public void set(int newValue) 
    {
        if (newValue < 0 || newValue > 255) throw new IllegalArgumentException("Nepravilna vrijednost argumenta r: " + newValue);
        super.set((newValue & 0x27) | (this.get() & 0x18)); // bitovi 3 i 4 (TO i PD) su non-writable, datasheet str.8
    }
    
    public void postaviZastavice(Flags zastavice)
    {
        if (zastavice.c) setC();
        else clearC();
        
        if (zastavice.dc) setDC();
        else clearDC();
        
        if (zastavice.z) setZ();
        else clearZ();
    }
    
    public boolean getC()
    {
        return getBit(0);
    }
    
    public void setC()
    {
        setBit(0);
    }
    
    public void clearC()
    {
        clearBit(0);
    }
    
    public boolean getDC()
    {
        return getBit(1);
    }
    
    public void setDC()
    {
        setBit(1);
    }
    
    public void clearDC()
    {
        clearBit(1);
    }
    
    public boolean getZ()
    {
        return getBit(2);
    }
    
    public void setZ()
    {
        setBit(2);
    }
    
    public void clearZ()
    {
        clearBit(2);
    }
    
    public boolean getRP0()
    {
        return getBit(5);
    }
    
    public void setRP0()
    {
        setBit(5);
    }
    
    public void clearRP0()
    {
        clearBit(5);
    }
}
