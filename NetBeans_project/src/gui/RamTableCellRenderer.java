/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import cpu.CPU;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author dkorent
 */
public class RamTableCellRenderer extends JLabel
implements TableCellRenderer 
{
    CPU cpu;
    
    Color zuta = new Color(22, 55, 0);

    public RamTableCellRenderer(CPU cpu) 
    {
        super();
        this.cpu = cpu;
        setOpaque(true);

    }

    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
    {
        setBackground(Color.white);
        //if ( column != 0 && row*8+column-1 == cpu.RegisterFile.PC.get()) setBackground(Color.yellow);
        //setHorizontalAlignment(JLabel.CENTER);
        if( column != 0 )
        {
            if ((row*8 + column) < 0xc +1 )
            {
                //setBackground(new Color(135,200,250));
                setBackground(new Color(255,255,240));
            }
            else if ((row*8 + column) < 0x50 +1)
            {
                setBackground(new Color(240,255,240));
            }
            else
            {
                setBackground(new Color(255,240,240));
            }
        }
        else
        {
            //setBackground(new Color(250,250,250));
        }
      
        String str = (value == null) ? "" : value.toString();
        setText( " " + str );
        this.setToolTipText(row + ":" + column);


        //if (isSelected) setBackground(new Color(255,255,255));
        return this;  
    }
    
}
