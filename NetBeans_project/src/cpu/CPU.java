package cpu;

import cpu.registers.Register8b_Base;
import cpu.instructions.Instruction;
import cpu.modules.InterruptController;
import cpu.modules.Timer;

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
            rom[i] = Instruction.Instanciraj(0);
        }
        
        
        // Dodavanje pokusnog programa u programsku memoriju
        PokusniProgramRotate();
        //PokusniProgramStari();
    }
    
    public Register8b_Base getRAM(int adr)
    {
        return RegisterFile.getRAM(adr);
    }
    
    
    public short getROM(int adr)
    {
        return (short)rom[adr].opcode;
    }
    
    
    public void izvrsiInstrukciju()
    {
        timerModule.OnTick();
        
        if ( interruptController.isInterrupted() )
        {
            HwStack.push( RegisterFile.PC.get() );
            RegisterFile.PC.set( INT_VECTOR );
            interruptController.setGlobalEnable( false );
        }
        
        rom[ RegisterFile.PC.get() ].izvrsi();
    }
    
    
    public String ispisiInstrukciju(int adr)
    {
        return rom[adr].ispisi();
    }
    
    /**************************************************************************************************************/
    
    private void PokusniProgramRotate()
    {
        int i = 0;
        
        final short regOption = 0x01; 
        final short regTmr0   = 0x01;
        final short regStatus = 0x03;
        final short regPortA  = 0x05;
        final short regPortB  = 0x06;
        final short regIntcon = 0x0B;

        

        
        i += 4;
        
        // Interrupt vector
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVLW + 0b11111000); 
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVWF + 0x80 + regIntcon);
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_RETURN);  
        
        i++;
        i++;
        
        rom[0] = Instruction.Instanciraj(Instruction.OPCODE_GOTO + i);

        
        // Activete Timer prescaler, a set to 1:2
        // chage Ram bank to 1 --> Set PS and PSA bits in Option reg to 0 --> chage Ram bank to 0
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_BSF + (5 << 7) + regStatus);  // SetBit( regStatus, 5)
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVLW + 0xF0);  // w =  0b11110000
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_ANDWF + 0x80 + regOption);  // regStatus = regStatus & w
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_BCF + (5 << 7) + regStatus);  // ClearBit( regStatus, 5)
        
        // Enable interrupts
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_CALL + 4);             // GOTO 4
        
        //  Set timer to 250
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVLW + 250); 
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVWF + 0x80 + regTmr0);
        
        
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVLW + 8 + 2 +1);         // MOVLW 11     : w = 11
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_ANDLW + 8 + 4 +2);         // ANDLW 14     : w & 14
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVWF + 0x80 + regPortB);  // MOVWF 6, w   : RAM[6] = w
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_CALL + i + 5);             // CALL func1
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_RLF + 0x80 + regPortB);    // RLF 6        : RAM[6] = RAM[6] << 1
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_GOTO + i - 3);             // GOTO -3
        i++;
        i++;
        i++;                                                                             // func1:
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_RLF + 0x80 + regPortB);    // RLF 6,f  : RAM[6] = RAM[6] >> 1
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_RLF + 0x80 + regPortB);    // RLF 6,f  : RAM[6] = RAM[6] >> 1
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_RETURN);                   // RETURN
    }
    
    private void PokusniProgramStari()
    {
        int i = 0;
        rom[i++] = Instruction.Instanciraj(0);
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVLW + 133);       // MOVLW 133     : w = 133
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVWF + 0x80 + 4);        // MOVWF 4      : RAM[4] = w
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVLW + 0);       // MOVLW 0     : w = 0
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVWF + 0x80 + 0);        // MOVWF 4      : RAM[0] = w
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVLW + 127-32-1);       // MOVLW 14     : w = 14
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVWF + 0x80 + 6);        // MOVWF 4      : RAM[4] = w
        
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVLW + 14);       // MOVLW 14     : w = 14
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVWF + 0x80 + 4);        // MOVWF 4      : RAM[4] = w
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVLW + 64);       // MOVLW 64     : w = 64
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVWF + 0x80 + 15);       // MOVWF 15     : RAM[15] = w
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVF + 0);    // MOVF 0, w   A: w = RAM[0]
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_ADDWF + 15);   // ADDWF 15, w  : w = w + RAM[15] 
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVWF + 0x80 + 14);       // MOVWF 14     : RAM[14] = w
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVLW + 5);         
        rom[i++] = Instruction.Instanciraj(Instruction.OPCODE_MOVWF + 0x80 + 2);         // GOTO 5       : GOTO A
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

