/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpu.registers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import junit.runner.Version;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

// Salabahter: http://tutorials.jenkov.com/java-unit-testing/asserts.html

/**
 *
 * @author dkorent
 */
public class Register8bTest {
    
    public Register8bTest() 
    {
    }
    
    @Rule
    public TestRule watcher = new TestWatcher() 
    {
       @Override
       protected void starting(Description description) 
       {
          System.out.println("Registar8 test: " + description.getMethodName());
       }
    };
    
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
    }
    
    @After
    public void tearDown() 
    {
    }

    @Test
    public void testInit() 
    {
        Register8b_Standard instance = new Register8b_Standard();

        assertEquals("Value on init = 0", 0, instance.get());
    }

    /**
     * Test of get method, of class Registar8.
     */
    @Test
    public void testGetSet() 
    {
        Register8b_Standard instance = new Register8b_Standard();
        
        instance.set(255);
        assertEquals("Set 255", 255, instance.get());
        
        instance.set(0);
        assertEquals("Set 0", 0, instance.get());
        
        instance.set(1);
        assertEquals("Set 1", 1, instance.get());
    }

    /**
     * Test of set method, of class Registar8.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSet_intSmall() 
    {
        Register8b_Standard instance = new Register8b_Standard();
        instance.set(-5);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSet_intBig() 
    {
        Register8b_Standard instance = new Register8b_Standard();
        instance.set(256);
    }

    /**
     * Test of set method, of class Registar8.
     */
    @Test
    public void testSet_Registar8() 
    {
        Register8b_Standard reg1 = new Register8b_Standard();
        Register8b_Standard reg2 = new Register8b_Standard();
        Register8b_Standard instance = new Register8b_Standard();

        reg1.set(5);
        instance.set(reg1);
        assertEquals("Set reg(5)", 5, instance.get());
        
        reg2.set(10);
        instance.set(reg2);
        assertEquals("Set reg(10)", 10, instance.get());
        assertNotSame("Not same instance", instance, reg2);
    }

    /**
     * Test of add method, of class Registar8.
     */
    @Test
    public void testAdd_Registar8() 
    {
        Register8b_Standard reg1 = new Register8b_Standard();
        Register8b_Standard reg2 = new Register8b_Standard();
        Register8b_Standard instance = new Register8b_Standard();
        
        instance.set(7);
        
        reg1.add(5);
        instance.add(reg1);
        assertEquals("Add 7 + reg(5)", 12, instance.get());
        
        reg2.add(10);
        instance.add(reg2);
        assertEquals("Add 12 + reg(10)", 22, instance.get());
        assertNotSame("Not same instance", instance, reg2);
    }

    /**
     * Test of add method, of class Registar8.
     */
    @Test
    public void testAdd_int() 
    {
        Register8b_Standard instance = new Register8b_Standard();
        
        instance.set(10);
        
        instance.add(13);
        assertEquals("Add 10 + 13", 23, instance.get());
        
        instance.add(24);
        assertEquals("Add 7 + 5", 47, instance.get());
    }
    
    @Test
    public void testOverflow() 
    {
        Register8b_Standard instance = new Register8b_Standard();
        
        instance.set(255);
        instance.add(1);
        assertEquals("255 + 1", 0, instance.get());
        
        instance.set(255);
        instance.add(2);
        assertEquals("255 + 2", 1, instance.get());
        
        instance.set(255);
        instance.add(255);
        assertEquals("255 + 255", 254, instance.get());
    }
    
    @Test
    public void testUnderflow() 
    {
        // TODO: implementirati oduzimanje
        
        /*
        System.out.println("get");
        Registar8 instance = new Registar8();
        
        instance.set(0);
        instance.add(1);
        assertEquals("255 + 1", 0, instance.get());
        
        instance.set(255);
        instance.add(2);
        assertEquals("255 + 2", 1, instance.get());
        
        instance.set(255);
        instance.add(255);
        assertEquals("255 + 255", 254, instance.get());
        */
    }

    /**
     * Test of getBit method, of class Registar8.
     */
    @Test
    public void testGetBit() 
    {
        // ToDo: test argument exeption
        
        Register8b_Standard instance = new Register8b_Standard();
        
        instance.set(128*1 + 64*0 + 32*0 + 16*1 + 8*1 + 4*0 + 2*1 + 1);
        
        assertEquals("Get bit 7", true, instance.getBit(7));
        assertEquals("Get bit 6", false, instance.getBit(6));
        assertEquals("Get bit 5", false, instance.getBit(5));
        assertEquals("Get bit 4", true, instance.getBit(4));
        assertEquals("Get bit 3", true, instance.getBit(3));
        assertEquals("Get bit 2", false, instance.getBit(2));
        assertEquals("Get bit 1", true, instance.getBit(1));
        assertEquals("Get bit 0", true, instance.getBit(0));
    }

    /**
     * Test of setBit method, of class Registar8.
     */
    @Test
    public void testSetBit() 
    {
        Register8b_Standard instance = new Register8b_Standard();
        
        instance.set(2);
        instance.setBit(0);
        assertEquals("Set bit 0", 3, instance.get());
        
        instance.set(3);
        instance.setBit(0);
        assertEquals("Set bit 0", 3, instance.get());
        
        instance.set(2);
        instance.setBit(7);
        assertEquals("Set bit 7", 128 + 2, instance.get());
        
        instance.set(1);
        instance.setBit(5);
        assertEquals("Set bit 5", 32 + 1, instance.get());
        
        instance.set(1);
        instance.setBit(6);
        assertEquals("Set bit 6", 64 + 1, instance.get());
    }

    /**
     * Test of clearBit method, of class Registar8.
     */
    @Test
    public void testClearBit() 
    {
        Register8b_Standard instance = new Register8b_Standard();
        
        instance.set(2);
        instance.clearBit(0);
        assertEquals("Clear bit 0", 2, instance.get());
        
        instance.set(3);
        instance.clearBit(0);
        assertEquals("Clear bit 0", 2, instance.get());
        
        instance.set(255);
        instance.clearBit(7);
        assertEquals("Clear bit 0", 255 - 128, instance.get());
        
        instance.set(255);
        instance.clearBit(6);
        assertEquals("Clear bit 0", 255 - 64, instance.get());
        
        instance.set(255);
        instance.clearBit(5);
        assertEquals("Clear bit 0", 255 - 32, instance.get());
    }

    /**
     * Test of toString method, of class Registar8.
     */
    @Test
    public void testToString() 
    {

    }
    
    
    
}
