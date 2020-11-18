/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import cpu.CpuExternalInterface;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author dkorent
 */
public class StackTableModel extends AbstractTableModel
{
    
    private CpuExternalInterface cpu;

    public StackTableModel( CpuExternalInterface cpu ) 
    {
        this.cpu = cpu;
    }

    @Override
    public int getRowCount() 
    {
        return 8;
    }

    @Override
    public int getColumnCount() 
    {
        return 1;
    }

    @Override
    public Object getValueAt(int i, int i1) 
    {
        int value = cpu.getStackData( i1 );
        return String.format("%04X", value);
    }
    
}
