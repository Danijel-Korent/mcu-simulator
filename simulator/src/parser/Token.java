/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import cpu.instructions.Instruction;
import java.util.HashMap;

/**
 *
 * @author dkorent
 */
public class Token 
{
    // ToDo: zamjeniti sa enum-ovima?
    public static final int TYPE_INSTRUCTION_ARG_NONE = 0;
    public static final int TYPE_INSTRUCTION_ARG_F = 1;
    public static final int TYPE_INSTRUCTION_ARG_F_D = 2;
    public static final int TYPE_INSTRUCTION_ARG_F_B = 3;
    public static final int TYPE_INSTRUCTION_ARG_A = 4;
    
    public static final int TYPE_OPERATOR = 9;
    public static final int TYPE_UNKNOWN = 66;
    
    public static final int OPERATOR_COMMA = 0;
    public static final int OPERATOR_COLON = 1;
    public static final int OPERATOR_EQU = 2;
    public static final int OPERATOR_ORG = 3;
    public static final int OPERATOR_END = 4;
    public static final int OPERATOR_F = 5;
    public static final int OPERATOR_W = 6;
    
    private static final HashMap<String, Token> lookupTable = new HashMap<>();

    
    public int value;
    public int type;
    public String word;
    
    public Token(int value, int type) 
    {
        this.value = value;
        this.type = type;
        this.word = "";
    }
    
    // Static initializer
    static
    {
        // Operators
        lookupTable.put(",", new Token( OPERATOR_COMMA, TYPE_OPERATOR ) );
        lookupTable.put(":", new Token( OPERATOR_COLON, TYPE_OPERATOR ) );
        lookupTable.put("equ", new Token( OPERATOR_EQU, TYPE_OPERATOR ) );
        lookupTable.put("org", new Token( OPERATOR_ORG, TYPE_OPERATOR ) );
        lookupTable.put("f", new Token( OPERATOR_F, TYPE_OPERATOR ) );
        lookupTable.put("w", new Token( OPERATOR_W, TYPE_OPERATOR ) );
        
        // Instructions
        lookupTable.put("addwf",  new Token( Instruction.OPCODE_ADDWF,  TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("andwf",  new Token( Instruction.OPCODE_ANDWF,  TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("clrf",   new Token( Instruction.OPCODE_CLRF,   TYPE_INSTRUCTION_ARG_F ) );
        lookupTable.put("clrw",   new Token( Instruction.OPCODE_CLRW,   TYPE_INSTRUCTION_ARG_NONE ) );
        lookupTable.put("comf",   new Token( Instruction.OPCODE_COMF,   TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("decf",   new Token( Instruction.OPCODE_DECF,   TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("decfsz", new Token( Instruction.OPCODE_DECFSZ, TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("incf",   new Token( Instruction.OPCODE_INCF,   TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("incfsz", new Token( Instruction.OPCODE_INCFSZ, TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("iorwf",  new Token( Instruction.OPCODE_IORWF,  TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("movf",   new Token( Instruction.OPCODE_MOVF,   TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("movwf",  new Token( Instruction.OPCODE_MOVWF,  TYPE_INSTRUCTION_ARG_F ) );
        lookupTable.put("nop",    new Token( Instruction.OPCODE_NOP,    TYPE_INSTRUCTION_ARG_NONE ) );
        lookupTable.put("rlf",    new Token( Instruction.OPCODE_RLF,    TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("rrf",    new Token( Instruction.OPCODE_RRF,    TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("subwf",  new Token( Instruction.OPCODE_SUBWF,  TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("swapf",  new Token( Instruction.OPCODE_SWAPF,  TYPE_INSTRUCTION_ARG_F_D ) );
        lookupTable.put("xorwf",  new Token( Instruction.OPCODE_XORWF,  TYPE_INSTRUCTION_ARG_F_D ) );
        
        lookupTable.put("bcf",    new Token( Instruction.OPCODE_BCF,    TYPE_INSTRUCTION_ARG_F_B ) );
        lookupTable.put("bsf",    new Token( Instruction.OPCODE_BSF,    TYPE_INSTRUCTION_ARG_F_B ) );
        lookupTable.put("btfsc",  new Token( Instruction.OPCODE_BTFSC,  TYPE_INSTRUCTION_ARG_F_B ) );
        lookupTable.put("btfss",  new Token( Instruction.OPCODE_BTFSS,  TYPE_INSTRUCTION_ARG_F_B ) );
        
        lookupTable.put("addlw",  new Token( Instruction.OPCODE_ADDLW,  TYPE_INSTRUCTION_ARG_F ) );
        lookupTable.put("andlw",  new Token( Instruction.OPCODE_ANDLW,  TYPE_INSTRUCTION_ARG_F ) );
        lookupTable.put("call",   new Token( Instruction.OPCODE_CALL,   TYPE_INSTRUCTION_ARG_A ) );
        lookupTable.put("clrwdt", new Token( Instruction.OPCODE_CLRWDT, TYPE_INSTRUCTION_ARG_NONE ) );
        lookupTable.put("goto",   new Token( Instruction.OPCODE_GOTO,   TYPE_INSTRUCTION_ARG_A ) );
        lookupTable.put("iorlw",  new Token( Instruction.OPCODE_IORLW,  TYPE_INSTRUCTION_ARG_F ) );
        lookupTable.put("movlw",  new Token( Instruction.OPCODE_MOVLW,  TYPE_INSTRUCTION_ARG_F ) );
        lookupTable.put("retfie", new Token( Instruction.OPCODE_RETFIE, TYPE_INSTRUCTION_ARG_NONE ) );
        lookupTable.put("retlw",  new Token( Instruction.OPCODE_RETLW,  TYPE_INSTRUCTION_ARG_F ) );
        lookupTable.put("return", new Token( Instruction.OPCODE_RETURN, TYPE_INSTRUCTION_ARG_NONE ) );
        lookupTable.put("sleep",  new Token( Instruction.OPCODE_SLEEP,  TYPE_INSTRUCTION_ARG_NONE ) );
        lookupTable.put("sublw",  new Token( Instruction.OPCODE_SUBLW,  TYPE_INSTRUCTION_ARG_F ) );
        lookupTable.put("xorlw",  new Token( Instruction.OPCODE_XORLW,  TYPE_INSTRUCTION_ARG_F ) );
    }
    
    
    public static Token Parse( String word)
    {
        Token keyword = new Token(0, TYPE_UNKNOWN);
        
        if( lookupTable.containsKey(word) )
        {
            keyword = lookupTable.get(word);
        }
        
        keyword.word = word;
        
        return keyword;
    }
    
    @Override
    public String toString()
    {
        return word + "\t Key:   " +  Integer.toString(type) + "\t" + Integer.toString(value) ;
    }
}
