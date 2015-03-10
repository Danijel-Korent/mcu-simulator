package cpu.instructions;

import cpu.registers.Flags;
import cpu.registers.Register8b_Base;
import cpu.registers.Register8b_Normal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Danijel Korent
 */
public class InstructionByte extends Instruction
{
    public final int type;
    private final int registerAddress;
    public final boolean destinationIsW;
    
    public Register8b_Base registarOperand;
    public Register8b_Base registarDestination; 
    
    public InstructionByte(int op) 
    {
        super(op);
        type = opcode & MASKA_INSTR_6;
        registerAddress = opcode & MASKA_FILE_7;
        destinationIsW = ( 0 == (opcode & MASKA_INSTR_DEST) );
             
        if (destinationIsW )
        {
            registarDestination = registerFile.W;
        }
        else
        {
            registarOperand = registerFile.W;
        }
    }
    
    @Override
    public void izvrsi()
    {
        super.izvrsi(); // uvecava PC

        // .getRAM mora bit pozvan kod svakog izvrsavanja zbog bankirane memorije
        if (destinationIsW )
        {
            registarOperand = registerFile.getRAM(registerAddress);
        }
        else
        {
            registarDestination = registerFile.getRAM(registerAddress);
        }
        
        
        if (type == OPCODE_MOVWF)
        {
            registarDestination.set(registerFile.W);
        }
        else if (type == OPCODE_ADDWF)
        {        
            Flags zastavice = registarDestination.add( registarOperand );

            registerFile.STATUS.postaviZastavice( zastavice ); 
        }
        else if (type == OPCODE_SUBWF)
        {        
            Flags zastavice = registarDestination.sub( registarOperand );

            registerFile.STATUS.postaviZastavice( zastavice ); 
        }
        else if ( type == OPCODE_MOVF )
        {
            registarDestination.set(registerFile.getRAM(registerAddress));
            
            if (registarDestination.get() == 0)
            {
                registerFile.STATUS.setZ();
            }
            else 
            {
                registerFile.STATUS.clearZ();
            }
        }
        else  if (type == OPCODE_ANDWF)
        {
            boolean newZ = registarDestination.logAnd( registarOperand );
            
            if ( newZ )
            {
                registerFile.STATUS.setZ();
            }
            else 
            {
                registerFile.STATUS.clearZ();
            }
        }
        else if (type == OPCODE_IORWF)
        {
            boolean newZ = registarDestination.logOr( registarOperand );
            
            if ( newZ )
            {
                registerFile.STATUS.setZ();
            }
            else 
            {
                registerFile.STATUS.clearZ();
            }
        }
        else if (type == OPCODE_XORWF)
        {
            boolean newZ = registarDestination.logXor( registarOperand );
            
            if ( newZ )
            {
                registerFile.STATUS.setZ();
            }
            else 
            {
                registerFile.STATUS.clearZ();
            }
        }
        else
        {
            if (destinationIsW )
            {
                registarDestination.set(registarOperand);
            }

            if ( type ==  OPCODE_RLF)
            {
                boolean newC = registarDestination.rotateLeft( registerFile.STATUS.getC() );

                if ( newC )
                {
                    registerFile.STATUS.setC();
                }
                else
                {
                    registerFile.STATUS.clearC();
                }
            } 
            else if ( type ==  OPCODE_RRF)
            {
                boolean newC = registarDestination.retateRight( registerFile.STATUS.getC() );

                if ( newC )
                {
                    registerFile.STATUS.setC();
                }
                else
                {
                    registerFile.STATUS.clearC();
                }
            }
            else if ( type ==  OPCODE_COMF)
            {
                boolean newZ = registarDestination.complement();

                if ( newZ )
                 {
                     registerFile.STATUS.setZ();
                 }
                 else 
                 {
                     registerFile.STATUS.clearZ();
                 }
            }
            else if ( type ==  OPCODE_SWAPF)
            {
                registarDestination.swapNibbles();
            }
            else if ( type ==  OPCODE_DECF)
            {
                registarDestination.sub(1);

                if ( 0 == registarDestination.get() )
                {
                    registerFile.STATUS.setZ();
                }
                else 
                {
                    registerFile.STATUS.clearZ();
                }
            }
            else if ( type ==  OPCODE_INCF)
            {
                registarDestination.add(1);

                if ( 0 == registarDestination.get() )
                {
                    registerFile.STATUS.setZ();
                }
                else 
                {
                    registerFile.STATUS.clearZ();
                }
            }
            else if ( type ==  OPCODE_DECFSZ)
            {
                registarDestination.sub(1);

                if ( 0 == registarDestination.get() )
                {
                    registerFile.PC.inc();
                }
            }
            else if ( type ==  OPCODE_INCFSZ)
            {
                registarDestination.add(1);

                if ( 0 == registarDestination.get() )
                {
                    registerFile.PC.inc();
                }
            }  
            else if ( type ==  OPCODE_CLR)
            {
                registarDestination.set(0);
                registerFile.STATUS.clearZ();
            }  
        }
    }
    
    @Override
    public String ispisi()
    {
        // ToDo: ispisati i destinaciju operacije
        if (type == OPCODE_MOVWF) return "MOVWF " + registerAddress;
        else if (type == OPCODE_MOVF) return "MOVF " + registerAddress;
        else if (type == OPCODE_ADDWF) return "ADDWF " + registerAddress;
        else if (type == OPCODE_ANDWF ) return "ANDWF " + registerAddress;
        else if (type == OPCODE_IORWF ) return "IORWF " + registerAddress;
        else if (type == OPCODE_RLF ) return "RLF " + registerAddress;
        else if (type == OPCODE_RRF ) return "RRF " + registerAddress;        
        else if (type == OPCODE_CLR ) return "CLR " + registerAddress;
        else if (type == OPCODE_COMF ) return "COMF " + registerAddress;
        else if (type == OPCODE_SWAPF ) return "SWAPF " + registerAddress;



        
        return "Nepoznata istrukcija!! PC = " + (registerFile.PC.get()-1) + " : Inst: " + opcode;
    }
    
}
