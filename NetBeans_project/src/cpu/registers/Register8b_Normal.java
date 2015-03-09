/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.registers;

/**
 *
 * @author dkorent
 */
public class Register8b_Normal extends Register8b_Base
{
    private int r;

    public Register8b_Normal() 
    {
        r = 0;
    }

    @Override
    public int get()
    {
        return r;
    }
    
    @Override
    public void set(int k) 
    {
        if (k < 0 || k > 255) throw new IllegalArgumentException("Nedopustena vrijednost argumenta r: " + k);
        this.r = k;
    }
}
