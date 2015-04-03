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
public class Register8b_Standard extends Register8b_Base
{
    private int value;

    public Register8b_Standard() 
    {
        value = 0;
    }

    @Override
    public int get()
    {
        return value;
    }
    
    @Override
    public void set(int newVal) 
    {
        if (newVal < 0 || newVal > 255) throw new IllegalArgumentException("Nedopustena vrijednost argumenta r: " + newVal);
        this.value = newVal;
    }
}
