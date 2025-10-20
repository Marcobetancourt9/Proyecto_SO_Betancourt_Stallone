/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AuxClass;
import AuxClass.List;
/**
 *
 * @author chela
 */
    public class Conjunto<T> {

    private List list;
    
    public Conjunto() {
        this.list = new List("lista-1");
    }

    public void insertar(T info) {
        if (!contiene(info)) {
            list.append(info);
        }
    }

    public boolean contiene(T info) {
        Nodo<T> nodo = list.obtenerNodo(info);
        return nodo != null;
    }

    public List<T> getLista() {
        return list;
    }

    public void setLista(List<T> lista) {
        this.list = lista;
    }
}
