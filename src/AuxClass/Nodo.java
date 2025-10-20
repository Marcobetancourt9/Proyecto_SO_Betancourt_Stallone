/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AuxClass;

/**
 *
 * @author chela
 */
public class Nodo <T>{
    private T tInfo;
    private Nodo <T> pNext;
    
    //Constructor
    public Nodo (T elem){
        this.tInfo=elem;
        this.pNext=null;
        
    } 

    /**
     * @return the tInfo
     */
    public T gettInfo() {
        return tInfo;
    }

    /**
     * @param tInfo the tInfo to set
     */
    public void settInfo(T tInfo) {
        this.tInfo = tInfo;
    }

    /**
     * @return the pNext
     */
    public Nodo <T> getpNext() {
        return pNext;
    }

    /**
     * @param pNext the pNext to set
     */
    public void setpNext(Nodo <T> pNext) {
        this.pNext = pNext;
    }
    
}


