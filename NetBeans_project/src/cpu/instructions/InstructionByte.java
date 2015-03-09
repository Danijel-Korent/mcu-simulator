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
    
    public final Register8b_Base registarOperand;
    public Register8b_Base registarDestination; 
    
    public InstructionByte(int op) 
    {
        super(op);
        type = opcode & MASKA_INSTR_6;
        registerAddress = opcode & MASKA_FILE_7;
        destinationIsW = ( 0 == (opcode & MASKA_INSTR_DEST) );
        
        // ToDo.: sto ako je u pitanju registar0 - indirektni registar
        registarOperand = registarDestination = registerFile.getRAM(registerAddress);
        
        if (destinationIsW )
        {
            // Ako je destinacija F, operaciju izvodimo nad samim registarOperand
            // ako je destinacija W, operaciju izvodimo na privremenom registru jer operand mora ostati nepromjenjen
            registarDestination = new Register8b_Normal();
        }
    }
    
    @Override
    public void izvrsi()
    {
        super.izvrsi(); // uvecava PC
        
        // Ukoliko je destinacija registar W - operand mora biti nepromjenjen nakon operacije, pa ga kopiramo u privremeni registar i nad privremenom vrsimo operaciju.
        // Nije najbrze rjesenje ali omogucava zajednicki/jednak kod za oba slucaja - manje koda manje gresaka.
        if (destinationIsW )
        {
            registarDestination.set(registarOperand);
        }
        
        
        if (type == OPCODE_MOVWF)
        {
            // ne koristi se registarDestination jer je kod instrukcije MOVWF destinacija uvijek operand
            registarOperand.set(registerFile.W);
        }
        else if (type == OPCODE_ADDWF)
        {        
            Flags zastavice = registarDestination.add( registerFile.W );

            registerFile.STATUS.postaviZastavice( zastavice ); 
        }
        else if (type == OPCODE_SUBWF)
        {        
            Flags zastavice = registarDestination.sub( registerFile.W );

            registerFile.STATUS.postaviZastavice( zastavice ); 
        }
        else if ( type == OPCODE_MOVF )
        {
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
            boolean newZ = registarDestination.logAnd( registerFile.W );
            
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
            boolean newZ = registarDestination.logOr( registerFile.W );
            
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
            boolean newZ = registarDestination.logXor( registerFile.W );
            
            if ( newZ )
            {
                registerFile.STATUS.setZ();
            }
            else 
            {
                registerFile.STATUS.clearZ();
            }
        }
        else if ( type ==  OPCODE_RLF)
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
        
        
        
        if (destinationIsW )
        {
            registerFile.W.set(registarDestination);
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
