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
public class RegisterTmr0 extends Register8b_Base
{
    private final Timer timerModule;

    public RegisterTmr0(Timer timerModule) 
    {
        super();
        this.timerModule = timerModule;
    }
    
    @Override
    public int get()
    {
        return timerModule.get();
    }
    
    @Override
    public void set(int k) 
    {
        if (k < 0 || k > 255) throw new IllegalArgumentException("Nepravilna vrijednost argumenta r: " + k);
        
        timerModule.set(k);
    }
}
