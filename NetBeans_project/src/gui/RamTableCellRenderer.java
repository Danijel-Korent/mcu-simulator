/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import cpu.CPU;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
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
        this.setHorizontalAlignment( JLabel.CENTER );
        
        //if ( column != 0 && row*8+column-1 == cpu.RegisterFile.PC.get()) setBackground(Color.yellow);
        //setHorizontalAlignment(JLabel.CENTER);
        if( column != 0 )
        {
            int address = (row*8 + column) - 1;
            
            if( address < 0xc )
            {
                //setBackground(new Color(135,200,250));
                setBackground(new Color(255,255,240));
                
                String tipText = "Special purpose registers";

                switch (address) 
                {
                    case 0:  tipText = "INDF";
                             break;
                    case 1:  tipText = "TMR0 / OPTION";
                             break;
                    case 2:  tipText = "PCL";
                             break;
                    case 3:  tipText = "STATUS";
                             break;
                    case 4:  tipText = "FSR";
                             break;
                    case 5:  tipText = "PORTA / TRISA";
                             break;
                    case 6:  tipText = "PORTB / TRISB";
                             break;
                    case 7:  tipText = "Unimplemented";
                             break;
                    case 8:  tipText = "EEDATA / EECON1";
                             break;
                    case 9:  tipText = "EEADR / EECON2";
                             break;
                    case 10: tipText = "PCLATH";
                             break;
                    case 11: tipText = "INTCON";
                             break;
                }
        
                this.setToolTipText( tipText );
            }
            else if ( address < 0x50 )
            {
                setBackground(new Color(240,255,240));
                this.setToolTipText("General purpose registers");
            }
            else
            {
                setBackground(new Color(255,240,240));
                this.setToolTipText("Unimplemented address space");
            }
        }
        else
        {
            this.setFont( this.getFont().deriveFont(Font.BOLD) );
            
            if( cpu.RegisterFile.STATUS.getRP0())
            {
                setBackground(new Color(230,230,255));
            }
            else
            {
                setBackground(new Color(240,240,255));
            }
            
        }
      
        String str = (value == null) ? "" : value.toString();
        setText( str );
        //this.setToolTipText(row + ":" + column);


        //if (isSelected) setBackground(new Color(255,255,255));
        return this;  
    }
    
}
