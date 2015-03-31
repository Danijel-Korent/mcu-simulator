/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author dkorent
 */
public class AsmInstruction 
{
    public int type;
    public int arg1;
    public int arg2;
    public String label;
    
    public AsmInstruction( int type )
    {
        this.type = type;
        arg1 = 0;
        arg2 = 0;
        label = "";
    }
    
    public int GetOpcode()
    {
        return type + arg1 + arg2;
    }
    
    @Override
    public String toString()
    {
        if( type == 0) return "NOP";
        
        return Integer.toHexString( type ) + " : " + label + Integer.toHexString( arg1 ) + " : " + Integer.toHexString( arg2 );
    }
}
