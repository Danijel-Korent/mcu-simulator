/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu;

import cpu.instructions.Instruction;
import cpu.registers.Register8b_Base;
//import cpu.registers.Register8b_Base;

/**
 *
 * @author dkorent
 */
public interface CpuExternalInterface extends CpuInternalInterface
{ 
    Instruction     getRom( int address );
    //Register8b_Base GetRam( int address );
    
    
    //Register8b_Base GetPc();
    //Register8b_Base GetW();
    //Register8b_Base GetStatus();
    
    Register8b_Base getRegPortA();
    Register8b_Base getRegPortB();
    
    int getActiveBank();
    boolean isIsr();
    
    int getStackData( int address );
    int getStackPointer();
    
    //get stack content and pointer??
}
