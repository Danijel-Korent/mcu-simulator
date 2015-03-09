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

/**
 *
 * @author dkorent
 */
public class InstructionByteTest 
{
    private final int bitD = 0x0080;
    
    private RegisterFileMemory RegisterFile;
    private StackMemory Stack;
    
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
        RegisterFile = new RegisterFileMemory();
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
        /*
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
        */
    }

    /**
     * Test of ispisi method, of class InstructionByte.
     */
    @Test
    public void test_ADDWF() 
    {
        Instruction testInstr;
        
        int fileAddress = 15;
        int fileVal = 10; 
        int regW = 5;
        
        testInstr = Instruction.Instanciraj( Instruction.OPCODE_ADDWF + bitD + fileAddress );
        
        RegisterFile.getRAM(fileAddress).set( fileVal );
        RegisterFile.W.set( regW );
        
        testInstr.izvrsi();
        
        assertEquals( RegisterFile.PC.get() , 1);
        assertEquals( RegisterFile.W.get() , regW);
        assertEquals( RegisterFile.getRAM(fileAddress).get() , fileVal + regW);
        
        
        //assertEquals(expResult, result);
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
