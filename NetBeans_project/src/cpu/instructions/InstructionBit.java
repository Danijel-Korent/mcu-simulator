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
    public int type;
    public int bit;
    private int registerAddress;
    public Register8b_Base registarOperand;

    public InstructionBit(int op) 
    {
        super(op);
        
        type = op & MASKA_INSTR_4;
        bit  = (op & MASKA_BIT_3) >> 7;
        
        registerAddress = op & MASKA_FILE_7;
        registarOperand = registerFile.getRAM(registerAddress);
    }

    @Override
    public void izvrsi() 
    {
        super.izvrsi(); 
        
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
                registerFile.PC.inc();
            }
        }
        else if (type == OPCODE_BTFSS)
        {
            if ( true == registarOperand.getBit(bit))
            {
                registerFile.PC.inc();
            }
        }
    }

    @Override
    public String ispisi() 
    {
        if (type == OPCODE_BCF)      return "BCF " + registerAddress + ", " + bit;
        else if (type == OPCODE_BSF) return "BSF " + registerAddress + ", " + bit;
        
        return "Nepoznata istrukcija!! PC = " + (registerFile.PC.get()-1) + " : Inst: " + opcode;
    }
    
}
