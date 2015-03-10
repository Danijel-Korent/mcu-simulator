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
public abstract class Register8b_Base
{
    public Register8b_Base() 
    {
    }

    /***************** Abstract methods ****************/
    
    abstract public int get();
    abstract public void set(int newValue);
    
    
    
    /**************** Implemented methods **************/
    
    public void set(Register8b_Base reg)
    {
        this.set(reg.get());
    }

    public boolean getBit(int b) {
        if (b < 0 || b > 7) throw new IllegalArgumentException("Nedopustena vrijednost argumenta b: " + b);
        int i = 1;
        i = i << b;
        return (this.get() & i) > 0;
    }
    
    public void setBit(int b)
    {
        if (b < 0 || b > 7) throw new IllegalArgumentException("Nedopustena vrijednost argumenta b: " + b);
        int i = 1;
        i = i << b;
        i = i | this.get();
        this.set(i);
    }
    
    public void clearBit(int b)
    {
        if (b < 0 || b > 7) throw new IllegalArgumentException("Nedopustena vrijednost argumenta b: " + b);
        int i = 1;
        i = i << b;
        i = ~i;
        i = i & this.get();
        this.set(i);
    }
    
    
    /* ###################### Aritmeticke operacije ###################### */
    
    public Flags add(Register8b_Base reg)
    {
        return this.add(reg.get());
    }
    
    public Flags add(int k)
    {
        if (k < 0 || k > 255) throw new IllegalArgumentException("Nedopustena vrijednost argumenta k: " + k);
        
        Flags zastavice = new Flags();
        int rez;
        
        rez = this.get() + k;
        if (rez > 255) 
        {
            rez -= 256;
            zastavice.c = true;
        }
        
        if (rez == 0) zastavice.z = true;
        if ((this.get() & 0xF + (k & 0xF) ) > 0xF) zastavice.dc = true;
        
        
        this.set(rez);
        return zastavice;
    }
    
    public Flags sub(Register8b_Base reg)
    {
        return this.sub(reg.get());
    }
    
    public Flags sub(int k)
    {
        if (k < 0 || k > 255) throw new IllegalArgumentException("Nedopustena vrijednost argumenta k: " + k);
        
        Flags zastavice = new Flags();
        int rez;
        
        zastavice.c = true;
        zastavice.dc = true;
        
        rez = this.get() - k;
        if (rez < 0) 
        {
            rez += 256;
            zastavice.c = false;
        }
        
        if (rez == 0) zastavice.z = true;
        if ((this.get() & 0xF - (k & 0xF) ) < 0) zastavice.dc = false;
        
        
        this.set(rez);
        return zastavice;
    }
    
    /* ###################### Logicke operacije ###################### */
    
    // vraca vrijednost zastavice Z nakon operacije
    public boolean logAnd(Register8b_Base reg)
    {
        return this.logAnd( reg.get() );
    }
    
    // vraca vrijednost zastavice Z nakon operacije
    public boolean logAnd(int k)
    {
        if (k < 0 || k > 255) throw new IllegalArgumentException("Nedopustena vrijednost argumenta k: " + k);
        
        this.set( this.get() & k );
        
        return ( this.get() == 0);
    }
    
    // vraca vrijednost zastavice Z nakon operacije
    public boolean logOr(Register8b_Base reg)
    {
        return this.logOr( reg.get() );
    }
    
    // vraca vrijednost zastavice Z nakon operacije
    public boolean logOr(int k)
    {
        if (k < 0 || k > 255) throw new IllegalArgumentException("Nedopustena vrijednost argumenta k: " + k);
        
        this.set( this.get() ^ k );
        
        return ( this.get() == 0);
    }
    
    // vraca vrijednost zastavice Z nakon operacije
    public boolean logXor(Register8b_Base reg)
    {
        return this.logXor( reg.get() );
    }
    
    // vraca vrijednost zastavice Z nakon operacije
    public boolean logXor(int k)
    {
        if (k < 0 || k > 255) throw new IllegalArgumentException("Nedopustena vrijednost argumenta k: " + k);
        
        this.set( this.get() ^ k );
        
        return ( this.get() == 0);
    }
    
    // vraca novu vrijednost carry zastavice
    public boolean rotateLeft(boolean carryBit)
    {
        int ret, newVal;
        
        ret = this.get() & 128; // dohvati najvisi bit
        
        newVal = (this.get() << 1) & 0xFF;
        if (carryBit) newVal += 1;
        
        this.set(newVal);
        return ret > 0;
    }
    
    // vraca novu vrijednost carry zastavice
    public boolean retateRight(boolean carryBit)
    {
        int ret, newVal;
        
        ret = this.get() & 1; // dohvati najnizi bit
        
        newVal = this.get() >> 1;
        if (carryBit) newVal += 0x80;
        
        this.set(newVal);
        return ret > 0;
    }
    
    // vraca novu vrijednost "zero" zastavice
    public boolean complement()
    {
        int temp = ~this.get() & 0xFF;
        
        this.set( temp );
        
        return (temp == 0);
    }
    
    public void swapNibbles()
    {
        int temp;
        
        temp = this.get() & 0x0F;
        temp = temp << 4;
        
        this.set((this.get() >> 4) + temp );
    }
    
    @Override
    public String toString()
    {
        return Integer.toString( this.get() );
    }
}
