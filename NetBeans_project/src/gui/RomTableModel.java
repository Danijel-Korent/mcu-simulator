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
public class RomTableModel extends AbstractTableModel 
{
    CPU cpu;

    public RomTableModel(CPU cpu) {
        super();
        this.cpu = cpu;
    }
    
    

    @Override
    public int getRowCount() {
        return 128;
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
    public Object getValueAt(int i, int i1) {
        if (i1 == 0) return Integer.toHexString(i*8);
        return Integer.toHexString(cpu.getROM( i*8 + i1-1 ));
    }
    
}
