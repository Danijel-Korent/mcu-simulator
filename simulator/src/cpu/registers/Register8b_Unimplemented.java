package cpu.registers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Danijel Korent
 */
public class Register8b_Unimplemented extends Register8b_Base
{
    // Sluzi za simuliranje neimplementiranih registara u adresnom prostoru mikrokontrolera
    // Implementiran kao nasljeden od Registar8 razreda jer se adresni prostor bez fizickih registara tretira jednako kao i onaj sa regitrima,
    // dakle nad adresom sa neimplementiranim registrom moguce su sve operacije kao i kod pristupa adresi sa implementiranim registrom. 
    // Citanje vrijednosti sa adrese bez registra uvijek vraca nulu kao vrijednsot na toj adresi.
    
    @Override
    public int get()
    {
        return 0;
    }
    
    @Override
    public void set(int k) 
    {
        if (k < 0 || k > 255) throw new IllegalArgumentException("Nepravilna vrijednost argumenta r: " + k);
    }
}
