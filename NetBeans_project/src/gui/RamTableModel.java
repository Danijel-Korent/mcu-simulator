package gui;


import cpu.CPU;
import javax.swing.table.AbstractTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Koki
 */
public class RamTableModel extends AbstractTableModel 
{
    CPU cpu;

    public RamTableModel(CPU cpu) {
        super();
        this.cpu = cpu;
    }


    @Override
    public int getRowCount() {
        return 16;
    }

    @Override
    public int getColumnCount() {
        return 9;
    }
    
    @Override
    public String getColumnName(int i) {
        if (i == 0) return "Adr.";
        else return "+" + (i-1);
    }
    
    @Override
    public Object getValueAt(int i, int i1) 
    {
        if (i1 == 0)
        {
            int val = i*8;
            if (cpu.RegisterFile.STATUS.getRP0())
            {
                val += 0x80;
            }
            return Integer.toHexString(val);
        }

        return Integer.toHexString( cpu.getRAM( (byte)(i*8 + i1-1) ).get());
    }  
}
