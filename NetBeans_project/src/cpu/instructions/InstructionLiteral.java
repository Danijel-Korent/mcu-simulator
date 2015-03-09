package cpu.instructions;

import cpu.registers.Flags;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Danijel Korent
 */
public class InstructionLiteral extends Instruction
{
    public final int value;
    public final int type;
    
    public InstructionLiteral(int op) 
    {
        super(op);

        type = opcode & MASKA_INSTR_6;
        value = op & MASKA_LITERAL_8;
    }
    
    @Override
    public void izvrsi()
    {
        super.izvrsi(); // uvecava PC
        
        if (type == OPCODE_MOVLW) 
        {
            registerFile.W.set(value);
        }
        else  if (type == OPCODE_ANDLW)
        {
            boolean newZ = registerFile.W.logAnd(value);
            
            if ( newZ )
            {
                registerFile.STATUS.setZ();
            }
            else 
            {
                registerFile.STATUS.clearZ();
            }
        }
        else if (type == OPCODE_IORLW)
        {
            boolean newZ = registerFile.W.logOr(value);
            
            if ( newZ )
            {
                registerFile.STATUS.setZ();
            }
            else 
            {
                registerFile.STATUS.clearZ();
            }
        }
        else if (type == OPCODE_XORLW)
        {
            boolean newZ = registerFile.W.logXor(value);
            
            if ( newZ )
            {
                registerFile.STATUS.setZ();
            }
            else 
            {
                registerFile.STATUS.clearZ();
            }
        }
        else if (type == OPCODE_ADDLW)
        {
            Flags zastavice = registerFile.W.add(value );

            registerFile.STATUS.postaviZastavice( zastavice ); 
        }
        else if (type == OPCODE_SUBLW)
        {
            Flags zastavice = registerFile.W.sub(value );

            registerFile.STATUS.postaviZastavice( zastavice ); 
        }
    }
    
    @Override
    public String ispisi()
    {
        if (type == OPCODE_MOVLW) return "MOVLW " + value;
        else if (type == OPCODE_ANDLW ) return "ANDLW " + value;        
        else if (type == OPCODE_ADDLW ) return "ADDLW " + value;
        else if (type == OPCODE_ANDLW ) return "ANDLW " + value;        
        else if (type == OPCODE_IORLW ) return "IORLW " + value;
        else if (type == OPCODE_XORLW ) return "XORLW " + value;

        
        return "Nepoznata LIT istrukcija!! PC = " + (registerFile.PC.get()-1) + " : Inst: " + opcode;
    }
    
}
