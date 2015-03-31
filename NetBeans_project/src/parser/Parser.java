/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import cpu.instructions.Instruction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 *
 * @author dkorent
 */
public class Parser 
{
    
    public Parser()
    {

    }
    
    public String Parse( String text)
    {
        String retStr = "";
        String tempStr = "";
        Integer i;
        
        ArrayList<AsmInstruction> instructions = new ArrayList<>();
        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<String> words = new ArrayList<>();
        
        try
        {
            words = ParseText( text );

            i = 0;
            for( String item : words)
            {
                tempStr += i.toString() + "\t" + item.toString() + "\n";
                i++;
            }
            retStr += tempStr + "\n\n"; // + retStr;
        }
        catch( Exception e)
        {
            retStr += e.toString();
        }
        
        
        try
        {
            tokens = ParseWords( words );

            i = 0;
            tempStr = "";
            for( Token item : tokens)
            {
                tempStr += i.toString() + "\t" + item.toString() + "\n";
                i++;
            }
            retStr += tempStr + "\n\n"; //+ retStr;
        }
        catch( Exception e)
        {
            retStr += e.toString();
        }
            
        
        
        try
        {
            instructions = ParseTokens( tokens );
            
            i = 0;
            tempStr = "";
            for( AsmInstruction item : instructions)
            {
                tempStr += i.toString() + "\t" + item.toString() + "\n";
                i++;
            }
            retStr += tempStr + "\n\n"; // + retStr;
        }
        catch( Exception e)
        {
            retStr += e.toString();
        }
        

        return retStr;
    }
    
    
    // parse list of tokens into AsmInstruction list
    private ArrayList<AsmInstruction> ParseTokens( ArrayList<Token> tokens )
    {
        ArrayList<AsmInstruction> instructions = new ArrayList<>(); 
        
        HashMap<String, Integer> labels = new HashMap<>();
        HashMap<String, Integer> equList = new HashMap<>();
        
        int tokenIterator = 0;
        int adressIterator = 0;
        
        /***************** Parse tokens and generate list of AsmInstructions ************/
        while( tokenIterator < tokens.size() )
        {
            Token token = tokens.get( tokenIterator++ );
            
            /*** If start token is instruction   ***/
            if( token.type <= Token.TYPE_INSTRUCTION_ARG_A )
            {
                AsmInstruction instruction = new AsmInstruction( token.value );
                
                // Instruction with no operand
                if( token.type == Token.TYPE_INSTRUCTION_ARG_NONE )
                {
                    
                }
                // instruction with file operand
                else if( token.type == Token.TYPE_INSTRUCTION_ARG_F )
                {
                    Token nextToken = tokens.get( tokenIterator++ );
                    
                    if( equList.containsKey( nextToken.word ) )
                    {
                        instruction.arg1 = equList.get(nextToken.word);
                    }
                    else
                    {
                        instruction.arg1 = Integer.decode( nextToken.word );
                    }
                }
                // instruction with file and destination operand
                else if( token.type == Token.TYPE_INSTRUCTION_ARG_F_D )
                {
                    Token nextToken;
                    
                    nextToken = tokens.get( tokenIterator++ );
                    
                    if( equList.containsKey( nextToken.word ) )
                    {
                        instruction.arg1 = equList.get(nextToken.word);
                    }
                    else
                    {
                        instruction.arg1 = Integer.decode( nextToken.word );
                    }
                    
                    nextToken = tokens.get( tokenIterator++ );
                    
                    if( nextToken.type == Token.TYPE_OPERATOR && nextToken.value == Token.OPERATOR_COMMA )
                    {
                        nextToken = tokens.get( tokenIterator++ );
                        
                        if( nextToken.type == Token.TYPE_OPERATOR )
                        {
                            if( nextToken.value == Token.OPERATOR_F )
                            {
                                instruction.arg2 = 0x80; // set the destination bit!
                            }
                        }
                    }
                    else
                    {
                        throw new IllegalArgumentException("Missing coma after fist argument!! ");
                    }
                }
                // instruction with file and bit address operand
                else if( token.type == Token.TYPE_INSTRUCTION_ARG_F_B )
                {
                    Token nextToken;
                    
                    nextToken = tokens.get( tokenIterator++ );
                    
                    if( equList.containsKey( nextToken.word ) )
                    {
                        instruction.arg1 = equList.get(nextToken.word);
                    }
                    else
                    {
                        instruction.arg1 = Integer.decode( nextToken.word );
                    }
                    
                    nextToken = tokens.get( tokenIterator++ );
                    
                    if( nextToken.type == Token.TYPE_OPERATOR && nextToken.value == Token.OPERATOR_COMMA )
                    {
                        nextToken = tokens.get( tokenIterator++ );
                        
                        int value = 0;
                        if( equList.containsKey( nextToken.word ) )
                        {
                            value = equList.get(nextToken.word);
                        }
                        else
                        {
                            value = Integer.decode( nextToken.word );
                        }
                        
                        instruction.arg2 = value << 7; // bit address is encoded in bits 7,8,9
                    }
                    else
                    {
                        throw new IllegalArgumentException("Missing coma after fist argument!! ");
                    }
                }
                // instruction with code address operand
                else if( token.type == Token.TYPE_INSTRUCTION_ARG_A )
                {
                    Token nextToken = tokens.get( tokenIterator++ );
                    int val = nextToken.word.charAt(0);
                    
                    if ( (val >= 65 && val <= 90) || (val >= 97 && val <= 122) )
                    {
                        instruction.label = nextToken.word;
                        instruction.arg1 = -1;
                    }
                    else
                    {
                        instruction.arg1 = Integer.decode( nextToken.word );
                    }
                }
                
                instructions.add( instruction );
                adressIterator++;
            }
            
            /*** Else If start token is operator ***/
            else if( token.type == Token.TYPE_OPERATOR )
            {
                
                if( token.value == Token.OPERATOR_ORG )
                {
                    Token valToken = tokens.get( tokenIterator++ );
                    int address = Integer.decode( valToken.word );
                    
                    if( adressIterator > address )
                    {
                        throw new IllegalArgumentException("Current instruction address bigger than ORG address!! ");
                    }
                    
                    for( ; adressIterator < address; adressIterator++)
                    {
                        instructions.add( new AsmInstruction( Instruction.OPCODE_NOP ) );
                    }
                }
                else if( token.value == Token.OPERATOR_END )
                {
                    // stop parsing
                }
                else
                {
                    throw new IllegalArgumentException("non valid start operator!! ");
                }
            }
            
            /*** Else If start token is unknown ***/
            else if( token.type == Token.TYPE_UNKNOWN && tokenIterator+1 < tokens.size() )
            {
                Token nextToken = tokens.get( tokenIterator++ );
                
                // if next token == EQU
                if( nextToken.type == Token.TYPE_OPERATOR && nextToken.value == Token.OPERATOR_EQU )
                {
                    Token valToken = tokens.get( tokenIterator++ );
                    
                    if( valToken.type == Token.TYPE_UNKNOWN )
                    {
                        // todo: dodati try .. catch na .decode metodi
                        equList.put(token.word, Integer.decode( valToken.word ));
                    }
                    else
                    {
                        throw new IllegalArgumentException("No value after EQU token!");
                    }
                }
                // if next token == :
                else if( nextToken.type == Token.TYPE_OPERATOR && nextToken.value == Token.OPERATOR_COLON )     
                {
                    labels.put(token.word, adressIterator);
                }
                // no EQU or : after unknown word
                else                    
                {
                    throw new IllegalArgumentException("no EQU or : after unknown word!! ");
                }
            }
        }
        
        /***************** Replace labels with numerical addresses in list of AsmInstructions ************/
        
        for( AsmInstruction instruction : instructions)
        {
            if( instruction.arg1 == -1 )
            {
                if( labels.containsKey(instruction.label) )
                {
                    instruction.arg1 = labels.get( instruction.label );
                }
                else
                {
                    throw new IllegalArgumentException("Unknown address label!! ");
                }
            }
        }
        
        return instructions;
    }
    
    // Parse string list of words into list of tokens
    private ArrayList<Token> ParseWords( ArrayList<String> words )
    {
        ArrayList<Token> tokens = new ArrayList<>();
        
        //Integer i = 0;
        for( String word : words )
        {
            word = word.toLowerCase( Locale.ROOT );
            Token token = Token.Parse(word);
            tokens.add( token );
            
            //retStr += i.toString() + ":  \t" + word + " \t " + token.toString();
            //i++;
        }
        
        return tokens;
    }
    
    // Parse text string and split it into string list of words and operators (while ignoring comments)
    private ArrayList<String> ParseText( String text )
    {
        ArrayList<String> tokens = new ArrayList<>();
        int firstLetterPtr = 0;
        
        final int MODE_NONE    = 0;
        final int MODE_TOKEN   = 1;
        final int MODE_IGNORE  = 2;
        int parseMode = 0;
        
        for( int x=0; x < text.length(); x++)
        {
            int val = (int) text.charAt(x);
            
            // If a..z || A..Z || 0..9 || underscore
            if( (val >= 65 && val <= 90) || (val >= 97 && val <= 122) || (val >= 48 && val <= 57) || val == 95)
            {
                if( MODE_NONE == parseMode )
                {
                    firstLetterPtr = x;
                    parseMode = MODE_TOKEN;
                }
            }
            else
            {             
                if( MODE_TOKEN == parseMode )
                {
                    tokens.add( text.substring(firstLetterPtr, x) );
                    parseMode = MODE_NONE;
                }
                
                if( val == 59) // char ;
                {
                    parseMode = MODE_IGNORE;
                }
                else if ( val == 10 || val == 13) // line feed or carriage return -> new line
                {
                    parseMode = MODE_NONE;
                }
                else if( val == 58 || val == 44 ) // , or :
                {
                    if( parseMode != MODE_IGNORE )
                    {
                        tokens.add( text.substring(x, x+1) );
                    }
                }
            }
        }
        
        // Special case: if we were parsing token and got to end of string before adding that token to the list
        if( MODE_TOKEN == parseMode )
        {
            tokens.add( text.substring(firstLetterPtr, text.length()) );
        }
        
        return tokens;
    }
}
