package br.com.furb.model;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Coordinator implements Runnable {

    private int id;
    private Queue<Runnable> resourceQueue;

    private Thread thread;

    public Coordinator(int id) {
        this.id = id;
        this.resourceQueue = new LinkedList();
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void interrupt() {
        this.thread.interrupt();
    }

    @Override
    public void run() {
        while (true) {
            Runnable resource;
            while ((resource = resourceQueue.poll()) != null) {
                resource.run();
            }
        }
    }

    public void addProcessing(Runnable process) {
        this.resourceQueue.add(process);
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
                Objects.equals(resourceQueue, that.resourceQueue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
