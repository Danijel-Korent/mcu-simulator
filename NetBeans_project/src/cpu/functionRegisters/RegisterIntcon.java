/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.functionRegisters;

import cpu.modules.InterruptController;
import cpu.registers.Register8b_Base;

/**
 *
 * @author dkorent
 */
public class RegisterIntcon extends Register8b_Base
{
    private final InterruptController controller;
    
    public RegisterIntcon( InterruptController interruptController  ) 
    {
        controller = interruptController;
    }

    @Override
    public int get() 
    {
       return controller.getRegIntcon();
    }

    @Override
    public void set(int newValue) 
    {
        controller.setRegIntcon(newValue);
    }
}
