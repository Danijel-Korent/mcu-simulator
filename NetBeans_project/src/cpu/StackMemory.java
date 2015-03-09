/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cpu;

/**
 *
 * @author dkorent
 */
public class StackMemory 
{
    private final int size;
    private final int stack[];
    private int pointer;
    
    public StackMemory(int size) 
    {
        this.size = size;
        stack = new int[size];
        pointer = 0;
    }
    
    public void push(int val)
    {
        if (val < 0 || val > 8192) throw new IllegalArgumentException("Nedopustena vrijednost argumenta val: " + val);
        
        stack[pointer] = val;
        
        pointer++;
        
        if (pointer == size)
        {
            pointer = 0;
        }
    }
    
    public int pop()
    {
        pointer--;
        
        if (pointer < 0)
        {
            pointer = size - 1;
        }
        
        return stack[pointer];
    }
}
