package cpu.functionRegisters;

import cpu.registers.Register8b_Base;
import cpu.registers.Register8b_Normal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Danijel Korent
 */
public class RegisterPC 
{
    private final Register8b_Base PCH = new Register8b_Normal();
    private final Register8b_Base PCL = new Register8b_Normal();
    
    public RegisterPC() 
    {
        PCH.set(0);
        PCL.set(0);
    }

    public int get()
    {
        return (PCH.get() << 8) + PCL.get();
    }
    
    public void set(int k) 
    {
        if (k < 0 || k > 8191) 
        {
            throw new IllegalArgumentException("Nepravilna vrijednost argumenta k: " + k); // 13-bitni registar
        }
        
        k = k & 0x3FF; // 16F84 wraparound, datasheet str. 5
        
        // postavljanje nizih 8 biteva
        PCL.set( k & 0xFF );
        
        // postavljanje visih 5 biteva
        k = k & 0xF00;
        k = k >> 8;
        PCH.set(k);
    }
    
    public void inc()
    {
        PCL.add(1);
        
        // if PCL overflowed, PCH must be increased
        if (PCL.get() == 0)
        {
            PCH.add(1);
            PCH.logAnd(0x1F); //wraparound
        }
    }
    
    public Register8b_Base getPCL()
    {
        return PCL;
    }
}
