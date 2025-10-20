/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainClasses;

/**
 *
 * @author chela
 */
public class RegistrosControlEstado {
    private int psw; // 0 modo kernel 1 modo usuario ?? 
    private int pc;
    private int MAR;
    
    public RegistrosControlEstado(int psw, int pc, int MAR){
        this.psw = psw;
        this.pc = pc;
        this.MAR = MAR;
    }

    public int getPsw() {
        return psw;
    }

    public int getPc() {
        return pc;
    }

    public int getMAR() {
        return MAR;
    }

    /**
     * @param psw the psw to set
     */
    public void setPsw(int psw) {
        this.psw = psw;
    }

    /**
     * @param pc the pc to set
     */
    public void setPc(int pc) {
        this.pc = pc;
    }

    /**
     * @param MAR the MAR to set
     */
    public void setMAR(int MAR) {
        this.MAR = MAR;
    }
    
    
         
}
