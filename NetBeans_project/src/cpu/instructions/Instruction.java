package cpu.instructions;

import cpu.RegisterFileMemory;
import cpu.StackMemory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Danijel Korent
 */
public class Instruction 
{
    // Prema opcode strukturi sve intrukcije se generalno mogu svrstati u tri "klase" - byte-oriented, bit-oriented  i literal, mada struktura nije potpuno uniformna i postoje izuzeci i odstupanja.    
        
    // maske za filtriranje bitova koji definiraju vrste instrukcije (prvih x bitova)
    public static final short MASKA_INSTR_7 = 0x3F80;
    public static final short MASKA_INSTR_6 = 0x3F00;
    public static final short MASKA_INSTR_4 = 0x3C00;
    public static final short MASKA_INSTR_3 = 0x3800;
    public static final short MASKA_INSTR_DEST = 0x0080;
   
    // maske za filtriranje bitova koji definiraju operande
    public static final short MASKA_LITERAL_8 = 0x00FF;
    public static final short MASKA_LITERAL_11 = 0x07FF;
    public static final short MASKA_BIT_3 = 0x0380;
    public static final short MASKA_FILE_7 = 0x007F;

    
    // byte-oriented instrukcije
    public static final short OPCODE_ADDWF = 0x0700;
    public static final short OPCODE_MOVF  = 0x0800;
    public static final short OPCODE_MOVWF = 0x0000;
    public static final short OPCODE_RLF   = 0x0D00;
    public static final short OPCODE_RRF   = 0x0C00;
    public static final short OPCODE_ANDWF = 0x0500;
    public static final short OPCODE_IORWF = 0x0400;
    public static final short OPCODE_XORWF = 0x0600;
    public static final short OPCODE_CLR   = 0x0100;
    public static final short OPCODE_COMF  = 0x0900;
    public static final short OPCODE_SWAPF = 0x0E00;
    public static final short OPCODE_SUBWF = 0x0200;
    public static final short OPCODE_DECF   = 0x0300;
    public static final short OPCODE_DECFSZ = 0x0B00;
    public static final short OPCODE_INCF   = 0x0A00;
    public static final short OPCODE_INCFSZ = 0x0F00;    

    // bit-oriented instrukcije
    public static final short OPCODE_BCF   = 0x1000;
    public static final short OPCODE_BSF   = 0x1400;
    public static final short OPCODE_BTFSC = 0x1800;
    public static final short OPCODE_BTFSS = 0x1C00;
    
    // literal instrukcije
    public static final short OPCODE_MOVLW = 0x3000;
    public static final short OPCODE_ANDLW = 0x3900;
    public static final short OPCODE_ADDLW = 0x3E00; 
    public static final short OPCODE_IORLW = 0x3800; 
    public static final short OPCODE_XORLW = 0x3A00;
    public static final short OPCODE_SUBLW = 0x3C00;

    // kontrolne insturkcije
    public static final short OPCODE_NOP    = 0x0;
    public static final short OPCODE_GOTO   = 0x2800;
    public static final short OPCODE_CALL   = 0x2000;
    public static final short OPCODE_RETURN = 0x0008;
    public static final short OPCODE_RETLW  = 0x3400;
    
    
    protected static RegisterFileMemory registerFile;
    protected static StackMemory stack;
    public final int opcode;
    
    public Instruction(int op) 
    {
        this.opcode = op;
    }
    
    public void izvrsi()
    {
        registerFile.PC.inc();
    }
    
    public String ispisi()
    {
        return "Unknown";
    }
    
    // *** Static Metode ***
    
    public static Instruction Instanciraj(int op)
    {
        if ( op < 0x0080 )
        {   
            // NOP, RET, SLEEP, CLRWDT, ....
            return new InstructionControl(op);
        } 
        else if ( op < 0x1000 ) return new InstructionByte(op);
        else if ( op < 0x2000 ) return new InstructionBit(op);
        else if ( op < 0x3000 ) return new InstructionControl(op);
        else if ( op < 0x4000 )return new InstructionLiteral(op);
        else throw new IllegalArgumentException("Nedopustena vrijednost opcode-a: " + op);
    }
    
    public static void setRegisterFile(RegisterFileMemory c)
    {
        registerFile = c;
    }
    
    public static void setStack(StackMemory s)
    {
        stack = s;
    }
    
}

