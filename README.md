# mcu-simulator

Simulator of PIC16F84A microcontroller, with embedded assembler (done for my Bachelor's thesis)

Simulator supports:
* All instructions except "SLEEP" and "CLRWDT"
* PORT & TRIS registers
* Indirect addressing (FSR & INDF)
* Jumps by PCL
* Hardware stack
* Memory banking
* Interrupt controller
* Timer
* EEPROM module

Assembler supports:
* All instructions
* Labels
* Comments
* "equ" and "org" directives
