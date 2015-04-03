/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpu.instructions;

import cpu.CPU;
import cpu.functionRegisters.RegisterPC;
import cpu.registers.Register8b_Base;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


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
    
    private CPU cpu;
    private RegisterPC regPc;
    private Register8b_Base regW;
    
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
        cpu = new CPU();
        
        regPc = cpu.getPc();
        regW  = cpu.getW();
        
        Instruction.setCpuInterface( cpu );
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
        int valueFileReg = 10; 
        int valueRegW = 5;
        
        // Test with destination F
        testInstr = Instruction.getInstance( Instruction.OPCODE_ADDWF + destinationF + fileAddress );
        
        cpu.getRam(fileAddress).set(valueFileReg );
        cpu.getW().set(valueRegW );
        
        testInstr.execute();
        
        assertEquals( 1, cpu.getPc().get());
        assertEquals(valueRegW, regW.get() );
        assertEquals(valueFileReg + valueRegW, cpu.getRam(fileAddress).get());
        
        
        // Test with destination W
        testInstr = Instruction.getInstance( Instruction.OPCODE_ADDWF + destinationW + fileAddress );
        
        cpu.getRam(fileAddress).set(valueFileReg );
        regW.set(valueRegW );
        
        testInstr.execute();
        
        assertEquals( 2, regPc.get());
        assertEquals(valueRegW + valueFileReg, regW.get() );
        assertEquals(valueFileReg, cpu.getRam(fileAddress).get());
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
