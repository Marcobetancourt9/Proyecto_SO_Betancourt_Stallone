/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AuxClass;

/**
 *
 * @author chela
 */
public class Cola<T> {

    private Nodo<T> head;
    private Nodo<T> tail;
    private int size;

    public Cola() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void vaciar() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.head == null;
    }

    public void empty() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        System.out.println();
        if (isEmpty()) {
            this.head = nuevo;
            this.tail = nuevo;
        } else {
            this.tail.setpNext(nuevo);
            this.tail = nuevo;
        }
        this.size++;
    }

    public void desencolar() {
        if (!isEmpty()) {
            if (this.size == 1) {
                empty();
            } else {
                this.head = this.head.getpNext();
                this.size--;
            }
        }
    }

    public String travel() {
        String toPrint = "";
        if (!isEmpty()) {
            Nodo<T> actual = this.head;
            while (actual != null) {
                toPrint += actual.gettInfo() + "-->";
                actual = actual.getpNext();
            }
        }
        return toPrint;
    }

    public List generarLista() {
        List listaProcesos = new List("");
        if (!isEmpty()) {
            Nodo<T> actual = this.head;
            while (actual != null) {
                listaProcesos.append(actual);
                actual = actual.getpNext();
            }
        }
        return listaProcesos;
    }

    public void desencolarEspecifico(T dato) {
        if (isEmpty()) {
            System.out.println("La cola está vacía.");
            return;
        }

        // Caso especial: si el nodo a eliminar es el head
        if (this.head.gettInfo().equals(dato)) {
            desencolar(); // Usa el método existente para desencolar el primer nodo
            return;
        }

        Nodo<T> actual = this.head;
        Nodo<T> anterior = null;

        while (actual != null) {
            if (actual.gettInfo().equals(dato)) {
                // Nodo encontrado
                if (anterior != null) {
                    anterior.setpNext(actual.getpNext()); // Salta el nodo actual
                    if (actual == this.tail) {
                        this.tail = anterior; // Actualiza el tail si es necesario
                    }
                }
                this.size--; // Disminuye el tamaño
                System.out.println("Nodo con dato " + dato + " desencolado.");
                return;
            }
            anterior = actual;
            actual = actual.getpNext();
        }

        System.out.println("Nodo con dato " + dato + " no encontrado en la cola.");
    }

    public Nodo<T> getHead() {
        return head;
    }

    public void setHead(Nodo<T> head) {
        this.head = head;
    }

    public Nodo<T> getTail() {
        return tail;
    }

    public void setTail(Nodo<T> tail) {
        this.tail = tail;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
