package br.com.furb.model;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Coordinator {

    private int id;
    private Queue<Runnable> queue;


    public Coordinator(int id) {
        this.id = id;
        this.queue = new LinkedList();
        //TODO: Ficar procesando seus recursos disponíveis.
    }

    public void addProcessing(Runnable process) {
        this.queue.add(process);
    }

    @Override
    public String toString() {
        return "[ID:" + id + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinator that = (Coordinator) o;
        return id == that.id &&
                Objects.equals(queue, that.queue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
