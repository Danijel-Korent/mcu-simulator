/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu;

import cpu.functionRegisters.RegisterPC;
import cpu.registers.Register8b_Base;

/**
 *
 * @author dkorent
 */
public interface CpuInternalInterface 
{
    void pushStack( int val);
    int  popStack();
    
    Register8b_Base getRam( int address );
    Register8b_Base getW();
    Register8b_Base getStatus();
    RegisterPC getPc();
    
    void enableGlobalInterrupts();
}
