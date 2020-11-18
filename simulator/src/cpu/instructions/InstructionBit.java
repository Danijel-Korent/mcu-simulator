/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.instructions;

import cpu.registers.Register8b_Base;

/**
 *
 * @author Danijel Korent
 */
public class InstructionBit extends Instruction
{
    private int type;
    private int bit;
    private int registerAddress;

    public InstructionBit(int op) 
    {
        super(op);
        
        type = op & MASKA_INSTR_4;
        bit  = (op & MASKA_BIT_3) >> 7;
        
        registerAddress = op & MASKA_FILE_7;
    }

    @Override
    public void execute() 
    {
        super.execute(); 
        
        Register8b_Base registarOperand = cpu.getRam(registerAddress);
        
        if ( type == OPCODE_BCF)
        {
            registarOperand.clearBit(bit);
        }
        else if (type == OPCODE_BSF)
        {
            registarOperand.setBit(bit);
        }
        else if (type == OPCODE_BTFSC)
        {
            if ( false == registarOperand.getBit(bit))
            {
                cpu.getPc().inc();
            }
        }
        else if (type == OPCODE_BTFSS)
        {
            if ( true == registarOperand.getBit(bit))
            {
                cpu.getPc().inc();
            }
        }
    }

    @Override
    public String getAsmCode() 
    {
        if (type == OPCODE_BCF)        return "BCF    " + registerAddress + ", " + bit;
        else if (type == OPCODE_BSF)   return "BSF    " + registerAddress + ", " + bit;
        else if (type == OPCODE_BTFSC) return "BTFSC  " + registerAddress + ", " + bit;
        else if (type == OPCODE_BTFSS) return "BTFSS  " + registerAddress + ", " + bit;
        
        return "Nepoznata istrukcija!! PC = " + (cpu.getPc().get()-1) + " : Inst: " + opcode;
    }
    
}
