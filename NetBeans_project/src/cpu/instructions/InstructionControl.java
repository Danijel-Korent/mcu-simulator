/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpu.instructions;

/**
 *
 * @author dkorent
 */
public class InstructionControl extends Instruction
{
    private final int value;
    private final int type;
    
    public InstructionControl(int op) 
    {
        super(op);
        
        if ( op > 0x1000 )
        {
            // instrukcije sa ukodiranom vrijednoscu
            type = opcode & MASKA_INSTR_3;
            value = opcode & MASKA_LITERAL_11;
        }
        else
        {
            // instrukcije bez vrijednosti
            type = op;
            value = 0;
        }
    }

    @Override
    public void execute() 
    {
        super.execute();
        
        // ToDo: premjestiti u kontrolne instukcije 
        if (type == OPCODE_GOTO)
        { 
            // ToDo: Svaka "jump" instrukcija ucitava PCLATH registar u gornja 2 bita PC-a. Nije prioritet jer nema efekta kod 16F84 - ima samo 1k memorije
            // Datasheet, str. 39: 
            // GOTO is an unconditional branch. The eleven-bit immediate value isloaded into PC bits <10:0>. 
            // The upper bits of PC are loaded from PCLATH<4:3>. GOTO is a two cycle instruction
            // Datasheet, str.7 : 
            //The upper byte of the program counter is not directly accessible. PCLATH is a slave register for PC<12:8>. The contents
            // of PCLATH can be transferred to the upper byte of the program counter, but the contents of PC<12:8> are never transferred to PCLATH.
            cpu.getPc().set(value);
        }
        else if (type == OPCODE_CALL)
        {
            cpu.pushStack( cpu.getPc().get() );
            cpu.getPc().set(value );
        }
        else if (type == OPCODE_RETURN)
        {
            cpu.getPc().set( cpu.popStack() );
        }
        else if (type == OPCODE_RETFIE)
        {
            cpu.getPc().set( cpu.popStack() );
            cpu.enableGlobalInterrupts();
        }
    }
    
    @Override
    public String getAsmCode() 
    {
        if (type == OPCODE_GOTO )      return "GOTO   " + value;
        else if (type == OPCODE_CALL ) return "CALL   " + value;
        else if (type == OPCODE_RETURN ) return "RETURN";
        else if (type == OPCODE_RETFIE ) return "RETFIE";
        else if (type == OPCODE_NOP ) return "NOP";
        else if (type == OPCODE_SLEEP) return "SLEEP";
        else if (type == OPCODE_CLRWDT) return "CLRWDT";
        
        return "Kontrolna Instr.";
    }
}
