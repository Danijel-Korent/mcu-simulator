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
public class CodeTableModel extends AbstractTableModel 
{
    CPU cpu;

    public CodeTableModel(CPU cpu) {
        super();
        this.cpu = cpu;
    }

    @Override
    public int getRowCount() {
        return 1024;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
    
    @Override
    public String getColumnName(int i) {
        if (i == 0) return "Adr.";
        else if (i == 1) return "label";
        else return "Code";
    }
    
    @Override
    public Object getValueAt(int i, int i1) {
        if (i1 == 0) return i;
        else if (i1 == 2) return cpu.ispisiInstrukciju(i);
        else return "";
    }
    
}
