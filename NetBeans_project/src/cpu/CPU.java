package cpu;

import cpu.functionRegisters.RegisterPC;
import cpu.registers.Register8b_Base;
import cpu.instructions.Instruction;
import cpu.modules.InterruptController;
import cpu.modules.Timer;
import cpu.registers.RegisterStatus;
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
public class CPU implements CpuExternalInterface
{
    private static final short INT_VECTOR = 0x04;
    
    // Modules
    private InterruptController interruptController;
    private Timer timerModule;
    
    // Memory
    private StackMemory HwStack;
    private RegisterFileMemory RegisterFile;
    private Instruction[] rom = new Instruction[1024];
    
    private boolean isInIsr;
    
    public CPU() 
    { 
        clearRom();
        reset();
        
        Instruction.setCpuInterface( (CpuInternalInterface)this );
        
        
        // Dodavanje pokusnog programa u programsku memoriju
        String AsmCode = "";
       
        AsmCode = PokusniProgramRotate();
        //AsmCode = PokusniProgramParser();
        
        ParseAssemblerCode( AsmCode );
    }
    
    /******************************** Public methods ********************************************************************************************/
    
    public final void reset()
    {
        isInIsr = false;
        
        interruptController = new InterruptController();
        timerModule = new Timer(interruptController);
        HwStack = new StackMemory(8);
        RegisterFile = new RegisterFileMemory(timerModule, interruptController);
    }
    
    public final void clearRom()
    {
          // popunjavanje programske memorije
        for(int i=0; i < 1024; i++) 
        {
            rom[i] = Instruction.getInstance(0);
        }
    }
    
    public void executeInstruction()
    {
        timerModule.onTick();
        
        if ( interruptController.isInterrupted() )
        {
            HwStack.push( RegisterFile.PC.get() );
            RegisterFile.PC.set( INT_VECTOR );
            interruptController.setGlobalEnable( false );
            isInIsr = true;
        }
        
        rom[ RegisterFile.PC.get() ].execute();
    }
    
    public String ParseAssemblerCode( String text )
    {
        Parser parser = new Parser();
        ArrayList<AsmInstruction> instructions = parser.Parse( text );
        
        clearRom();

        if( instructions.size() > 0)
        {
            this.clearRom();
        }
        
        int i = 0;
        for( AsmInstruction instruction : instructions )
        {
            int opcode = instruction.GetOpcode();
            
            rom[i++] = Instruction.getInstance( opcode );
        }
        
        
        return parser.getErrorMsg();
    }

    /******************************** CpuExternalInterface implementation ***********************************************************************/
    
    @Override
    public Instruction getRom(int address) 
    {
        return rom[address];
    }

    @Override
    public int getActiveBank() 
    {
        if (RegisterFile.STATUS.getRP0())
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public boolean isIsr() 
    {
        return isInIsr;
    }

    @Override
    public void pushStack(int val) 
    {
        HwStack.push( val );
    }

    @Override
    public int popStack() 
    {
        return HwStack.pop();
    }

    @Override
    public Register8b_Base getRam(int address) 
    {
        return RegisterFile.getRam( address );
    }

    @Override
    public RegisterPC getPc()
    {
        return RegisterFile.PC;
    }

    @Override
    public Register8b_Base getW() 
    {
        return RegisterFile.W;
    }

    @Override
    public RegisterStatus getStatus() 
    {
        return RegisterFile.STATUS;
    }
    

    @Override
    public Register8b_Base getRegPortA() 
    {
        return RegisterFile.PORTA;
    }

    @Override
    public Register8b_Base getRegPortB() 
    {
        return RegisterFile.PORTB;
    }

    @Override
    public void enableGlobalInterrupts() 
    {
        interruptController.setGlobalEnable( true );
        isInIsr = false;
    }
    
    @Override
    public int getStackData(int address) 
    {
        return this.HwStack.getData(address);
    }

    @Override
    public int getStackPointer() 
    {
        return this.HwStack.getStackPointer();
    }
    
    /******************************** Private Methods *******************************************************************************************/
    
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
}

