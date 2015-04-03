package cpu;

import cpu.registers.Register8b_Base;
import cpu.instructions.Instruction;
import cpu.modules.InterruptController;
import cpu.modules.Timer;
import java.util.ArrayList;
import parser.AsmInstruction;
import parser.Parser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Danijel Korent
 */
public class CPU 
{
    private static final short INT_VECTOR = 0x04;
    
    // Modules
    private final InterruptController interruptController = new InterruptController();
    private final Timer timerModule = new Timer(interruptController);
    
    // Memory
    private Instruction[]           rom = new Instruction[1024];
    private final StackMemory       HwStack = new StackMemory(8);
    public final RegisterFileMemory RegisterFile = new RegisterFileMemory(timerModule, interruptController);
    
    public CPU() 
    { 
        Instruction.setRegisterFile(RegisterFile);
        Instruction.setStack(HwStack);
        
        // popunjavanje programske memorije
        for(int i=0; i < 1024; i++) 
        {
            rom[i] = Instruction.getInstance(0);
        }
        
        
        // Dodavanje pokusnog programa u programsku memoriju
        String AsmCode = "";
       
        AsmCode = PokusniProgramRotate();
        //AsmCode = PokusniProgramParser();
        
        ParseAssemblerCode( AsmCode );
    }
    
    public Register8b_Base getRAM(int adr)
    {
        return RegisterFile.getRam(adr);
    }
    
    
    public short getROM(int adr)
    {
        return (short)rom[adr].opcode;
    }
    
    
    public void ExecuteInstruction()
    {
        timerModule.onTick();
        
        if ( interruptController.isInterrupted() )
        {
            HwStack.push( RegisterFile.PC.get() );
            RegisterFile.PC.set( INT_VECTOR );
            interruptController.setGlobalEnable( false );
        }
        
        rom[ RegisterFile.PC.get() ].execute();
    }
    
    
    public String ispisiInstrukciju(int adr)
    {
        return rom[adr].getAsmCode();
    }
    
    public void ParseAssemblerCode( String text )
    {
        ArrayList<AsmInstruction> instructions = new ArrayList<>();
        
        instructions = Parser.Parse( text );

        int i = 0;
        for( AsmInstruction instruction : instructions )
        {
            int opcode = instruction.GetOpcode();
            
            rom[i++] = Instruction.getInstance( opcode );
        }
    }
    
    /**************************************************************************************************************/
    
    private String PokusniProgramParser()
    {
        return  
                "    ;***** VARIABLE DEFINITIONS\n" +
                "    w_temp        EQU     0x0C        ; variable used for context saving \n" +
                "    status_temp   EQU     0x0D        ; variable used for context saving\n" +
                "\n" +
                "\n" +
                "\n" +
                " STATUS EQU 3\n" +
                "\n" +
                "\n" +
                "reset:\n" +
                "\n" +
                "    ;**********************************************************************\n" +
                "    ORG     0x000             ; processor reset vector\n" +
                "    goto    main              ; go to beginning of program\n" +
                "\n" +
                "\n" +
                "    ORG     0x004             ; interrupt vector location\n" +
                "movwf   w_temp            ; save off current W register contents\n" +
                "movf	STATUS,w          ; move status register into W register\n" +
                "movwf	status_temp       ; save off contents of STATUS register\n" +
                "\n" +
                "\n" +
                "; isr code can go here or be located as a call subroutine elsewhere\n" +
                "\n" +
                "\n" +
                "main:		movf    status_temp,w     ; retrieve copy of STATUS register\n" +
                "		movwf	STATUS            ; restore pre-isr STATUS register contents\n" +
                "		swapf   w_temp,f\n" +
                "		swapf   w_temp,w          ; restore pre-isr W register contents\n" +
                "		retfie                    ; return from interrupt\n" +
                "               bcf STATUS, 3 ;test\n" +
                "call reset"
                ;
    }
    
    private String PokusniProgramRotate()
    {
        
        String ret =
                ""
                + "\n" + "OPTION equ 0x01"
                + "\n" + "TMR0   equ 0x01"
                + "\n" + "STATUS equ 0x03"
                + "\n" + "PORTA  equ 0x05"
                + "\n" + "PORTB  equ 0x06"
                + "\n" + "INTCON equ 0x0b"
                + "\n" + ""
                + "\n" + "GOTO start"
                + "\n" + ""
                + "\n" + "ORG 0x04"
                + "\n" + "MOVLW 0x78"
                + "\n" + "MOVWF INTCON    ;enable interrupt sources, clear int flags"
                + "\n" + "RETFIE          ;return and set global int enable "
                + "\n" + ""
                + "\n" + "ORG 10"
                + "\n" + "start:"
                + "\n" + "BSF STATUS, 5   ;select bank 1"
                + "\n" + "MOVLW 0xF0"
                + "\n" + "ANDWF OPTION, f ;activate prescaler and set to 2:1 "
                + "\n" + "BCF STATUS, 5   ;select bank 0"
                + "\n" + "CALL 4          ;to enable interrupts"
                + "\n" + "MOVLW 251"
                + "\n" + "IORWF TMR0, f   ;change timer value"
                + "\n" + ""
                + "\n" + "petlja:"
                //+ "\n" + "MOVLW 11"
                + "\n" + "ANDLW 0xA0"
                + "\n" + "MOVWF PORTB"
                + "\n" + "CALL procedura"
                + "\n" + "RLF PORTB, f"
                + "\n" + "GOTO petlja"
                + "\n" + ""
                + "\n" + "org 0x20"
                + "\n" + "procedura:"
                + "\n" + "RLF PORTB, f"
                + "\n" + "RLF PORTB, f"
                + "\n" + "RETURN"
                
                + "\n" + ""
                ;
        
        return ret;
    }
    
    private void PokusniProgramStari()
    {
        int i = 0;
        rom[i++] = Instruction.getInstance(0);
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVLW + 133);       // MOVLW 133     : w = 133
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVWF + 4);        // MOVWF 4      : RAM[4] = w
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVLW + 0);       // MOVLW 0     : w = 0
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVWF + 0);        // MOVWF 4      : RAM[0] = w
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVLW + 127-32-1);       // MOVLW 14     : w = 14
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVWF + 6);        // MOVWF 4      : RAM[4] = w
        
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVLW + 14);       // MOVLW 14     : w = 14
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVWF + 4);        // MOVWF 4      : RAM[4] = w
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVLW + 64);       // MOVLW 64     : w = 64
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVWF + 15);       // MOVWF 15     : RAM[15] = w
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVF + 0);    // MOVF 0, w   A: w = RAM[0]
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_ADDWF + 15);   // ADDWF 15, w  : w = w + RAM[15] 
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVWF + 14);       // MOVWF 14     : RAM[14] = w
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVLW + 5);         
        rom[i++] = Instruction.getInstance(Instruction.OPCODE_MOVWF + 2);         // GOTO 5       : GOTO A
    }
    
    /*
    public void ispisiStanje()
    {
        System.out.println("\nPC = " + PC + " | W = " + Integer.toHexString(w.get()) );
        System.out.println("Iduca instrukcija: " + instrukcija.ispisi(ROM[PC]));
    }
    
    
    
    public void ispisiInstrukcije(int brojRedaka)
    {
        for(int i = 0; i < brojRedaka; i++)
        {
            System.out.println(Integer.toHexString(i) + " : " + instrukcija.ispisi(ROM[i]));
        }
    }
    
    public void ispisiROM(int brojRedaka)
    {
        System.out.println("\nIspis ROM-a");
        System.out.println("Adr\t+0\t+1\t+2\t+3\t+4\t+5\t+6\t+7");
        
        for(int i = 0; i < 1024/8 && i < brojRedaka; i++)
        {
            System.out.print( i*8 + "\t");
            for(int x = 0; x < 8; x++)
                System.out.print(Integer.toHexString(ROM[i*8+x]) + "\t");
            System.out.println();
        }
    }
   
    
    public void ispisiRAM(int brojRedaka)
    {
        System.out.println("\nIspis RAM-a");
        System.out.println("Adr\t+0\t+1\t+2\t+3\t+4\t+5\t+6\t+7");
        
        for(int i = 0; i < 128/8 && i < brojRedaka; i++)
        {
            System.out.print( i*8 + "\t");
            for(int x = 0; x < 8; x++)
                System.out.print(Integer.toHexString( (RAM[i*8+x]).get() ) + "\t");
            System.out.println();
        }
    }
    */
}

