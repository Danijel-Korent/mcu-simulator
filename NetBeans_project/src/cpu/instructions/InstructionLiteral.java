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
    public void execute()
    {
        super.execute(); // uvecava PC
        
        if (type == OPCODE_MOVLW) 
        {
            cpu.getW().set(value);
        }
        else  if (type == OPCODE_ANDLW)
        {
            boolean newZ = cpu.getW().logAnd(value);
            
            if ( newZ )
            {
                cpu.getStatus().setZ();
            }
            else 
            {
                cpu.getStatus().clearZ();
            }
        }
        else if (type == OPCODE_IORLW)
        {
            boolean newZ = cpu.getW().logOr(value);
            
            if ( newZ )
            {
                cpu.getStatus().setZ();
            }
            else 
            {
                cpu.getStatus().clearZ();
            }
        }
        else if (type == OPCODE_XORLW)
        {
            boolean newZ = cpu.getW().logXor(value);
            
            if ( newZ )
            {
                cpu.getStatus().setZ();
            }
            else 
            {
                cpu.getStatus().clearZ();
            }
        }
        else if (type == OPCODE_ADDLW)
        {
            Flags zastavice = cpu.getW().add( value );

            cpu.getStatus().postaviZastavice( zastavice ); 
        }
        else if (type == OPCODE_SUBLW)
        {
            Flags zastavice = cpu.getW().sub(value );

            cpu.getStatus().postaviZastavice( zastavice ); 
        }
        else if (type == OPCODE_RETLW)
        {
            cpu.getW().set( value );
            cpu.getPc().set( cpu.popStack() );
        }
    }
    
    @Override
    public String getAsmCode()
    {
        if (type == OPCODE_MOVLW) return "MOVLW " + value;
        else if (type == OPCODE_ANDLW ) return "ANDLW " + value;        
        else if (type == OPCODE_ADDLW ) return "ADDLW " + value;
        else if (type == OPCODE_ANDLW ) return "ANDLW " + value;        
        else if (type == OPCODE_IORLW ) return "IORLW " + value;
        else if (type == OPCODE_XORLW ) return "XORLW " + value;
        else if (type == OPCODE_RETLW ) return "RETLW " + value;

        
        return "Nepoznata LIT istrukcija!! PC = " + (cpu.getPc().get()-1) + " : Inst: " + opcode;
    }
    
}
