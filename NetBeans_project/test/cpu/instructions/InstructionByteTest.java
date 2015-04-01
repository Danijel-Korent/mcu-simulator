/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpu.instructions;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import cpu.RegisterFileMemory;
import cpu.StackMemory;
import cpu.modules.InterruptController;
import cpu.modules.Timer;

/**
 *
 * @author dkorent
 */
public class InstructionByteTest 
{
    
    private final int destinationW = 0x0;
    private final int destinationF = 0x0080;
    
    private final int GPR_START = 0x0C;
    private final int GPR_END   = 0x4F;
    
    private RegisterFileMemory RegisterFile;
    private StackMemory Stack;
    
    // Konstruktor
    public InstructionByteTest() 
    {
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
    }
    
    @Before
    public void setUp() 
    {
        InterruptController interruptController = new InterruptController();
        Timer timerModule = new Timer( interruptController );
        
        RegisterFile = new RegisterFileMemory( timerModule, interruptController );
        Stack = new StackMemory(8);
        
        Instruction.setRegisterFile( RegisterFile );
        Instruction.setStack( Stack );
    }
    
    @After
    public void tearDown() 
    {
    }

    /**
     * Test of izvrsi method, of class InstructionByte.
     */
    @Test
    public void testIzvrsi() 
    {

    }

    /**
     * Test of ispisi method, of class InstructionByte.
     */
    @Test
    public void test_ADDWF() 
    {
        Instruction testInstr;
        
        int fileAddress = GPR_START;
        int fileVal = 10; 
        int regW = 5;
        
        
        // Test with destination F
        testInstr = Instruction.Instanciraj( Instruction.OPCODE_ADDWF + destinationF + fileAddress );
        
        RegisterFile.getRAM(fileAddress).set( fileVal );
        RegisterFile.W.set( regW );
        
        testInstr.izvrsi();
        
        assertEquals( 1, RegisterFile.PC.get());
        assertEquals( regW, RegisterFile.W.get() );
        assertEquals( fileVal + regW, RegisterFile.getRAM(fileAddress).get());
        
        
        // Test with destination W
        testInstr = Instruction.Instanciraj( Instruction.OPCODE_ADDWF + destinationW + fileAddress );
        
        RegisterFile.getRAM(fileAddress).set( fileVal );
        RegisterFile.W.set( regW );
        
        testInstr.izvrsi();
        
        assertEquals( 2, RegisterFile.PC.get());
        assertEquals( regW + fileVal, RegisterFile.W.get() );
        assertEquals( fileVal, RegisterFile.getRAM(fileAddress).get());
    }
    
    @Test
    public void test_MOVF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_MOVWF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_RLF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_RRF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_ANDWF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_IORWF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_XORWF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_CLR() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_COMF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_SWAPF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_SUBWF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_DECF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_DECFSZ() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_INCF() 
    {
        //assertEquals(expResult, result);
    }
    
    @Test
    public void test_INCFSZ() 
    {
        //assertEquals(expResult, result);
    }
}
