/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu;

import cpu.functionRegisters.RegisterOption;
import cpu.functionRegisters.RegisterTmr0;
import cpu.modules.Timer;
import cpu.registers.Register8b_Base;
import cpu.registers.Register8b_Normal;
import cpu.registers.RegisterStatus;
import cpu.registers.Register8b_Unimplemented;
import cpu.functionRegisters.RegisterPC;

/**
 *
 * @author Danijel Korent
 */
public class RegisterFileMemory 
{
    
    private final Register8b_Base[] ramBank0 = new Register8b_Base[128];
    private final Register8b_Base[] ramBank1 = new Register8b_Base[128];
    
    public final RegisterPC PC = new RegisterPC();
    
    public final Register8b_Base W = new Register8b_Normal();
    public final RegisterStatus STATUS = new RegisterStatus();
    private final Register8b_Base FSR = new Register8b_Normal();
    
    public final Register8b_Base PORTA = new Register8b_Normal();
    public final Register8b_Base PORTB = new Register8b_Normal();
    
    final Register8b_Base TRISA = new Register8b_Normal();
    final Register8b_Base TRISB = new Register8b_Normal();

    // ToDo: 
    public RegisterFileMemory(Timer timerModule) 
    {
        
        Register8b_Unimplemented regNeimplemetiran = new Register8b_Unimplemented();
        
        RegisterTmr0 tmr0 = new RegisterTmr0(timerModule);
        RegisterOption regOption = new RegisterOption(timerModule);

        // Privremeno popunjavanje SFR adresnog prostora sa opcim registrima
        for(int i=1; i < 0x0C; i++) 
        {
            ramBank0[i]= new Register8b_Normal();
            ramBank1[i]= new Register8b_Normal();
        }

        // Popunjavanja GPR adresnog prostora
        for(int i=0x0C; i < 0x50; i++)
            {
                Register8b_Base reg = new Register8b_Normal();
                ramBank0[i]= reg;
                ramBank1[i]= reg;
            }
        
        // Popunjavanje adresnog prostora koji nije fizicki implementiran, datasheet str.6
        for(int i=0x50; i < 0x80; i++) 
        {
            ramBank0[i]= regNeimplemetiran;
            ramBank1[i]= regNeimplemetiran;
        }

        // popunjavanja SFR adresnog prostora sa specijalnim registrima koji su do sad implementirani
        ramBank0[0] = regNeimplemetiran; // indirektno citanje mem. lokacije "0" (FSR=0) vraca rezultat "0", datasheet str.11
        ramBank1[0] = regNeimplemetiran; // TODO: provjeriti postavljanje STATUS biteva, datasheet str.11 - "Writing to the INDF register indirectly results in a no-operation although STATUS bits may be affected."
        ramBank0[1] = tmr0;
        ramBank1[1] = regOption;
        ramBank0[2] = PC.getPCL();
        ramBank1[2] = PC.getPCL();
        ramBank0[3] = STATUS;
        ramBank1[3] = STATUS;
        ramBank0[4] = FSR;
        ramBank1[4] = FSR;
        
        // TODO: potrebna nova klasa za ove registre
        ramBank0[5] = PORTA; // TODO: ne može se zapisivati u bitove koji su ulazni (TRIS = '1')
        ramBank0[6] = PORTB; // TODO: bitovi 7, 6, 5 su neimplementirani i uvijek na "0" - datasheet str.7
        ramBank1[5] = TRISA;
        ramBank1[6] = TRISB; // TODO: bitovi 7, 6, 5 su neimplementirani i uvijek na "0" - datasheet str.7
        
        ramBank0[7] = regNeimplemetiran;
        ramBank1[7] = regNeimplemetiran;
    }
    
    public Register8b_Base getRAM(int adr)
    {   
        if (adr == 0) return getRAMindirektno();
        
        if (STATUS.getRP0())
        {
            return ramBank1[adr];
        }
        else
        {
            return ramBank0[adr];
        }
    }
    
    public Register8b_Base getRAMindirektno()
    {
        // datasheet str8.:  Indirect addressing uses the present value of the RP0 bit for access into the banked areas of data memory.
        // datasheet str14.: Nacrtano suprotno od gornje tvrdnje
        if (FSR.get() > 127)
        {
            return ramBank1[FSR.get() - 128];
        }
        else
        {
            return ramBank0[FSR.get()];
        }
    }
}
