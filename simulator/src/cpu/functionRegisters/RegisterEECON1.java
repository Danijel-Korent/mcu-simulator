/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.functionRegisters;

import cpu.modules.EepromController;
import cpu.registers.Register8b_Base;

/**
 *
 * @author dkorent
 */
public class RegisterEECON1 extends Register8b_Base
{
    private EepromController eeprom;

    public RegisterEECON1(EepromController eeprom) 
    {
        this.eeprom = eeprom;
    }

    @Override
    public int get() 
    {
        return eeprom.getRegEECON1();
    }

    @Override
    public void set(int newValue) 
    {
        eeprom.setRegEECON1(newValue);
    }
}
