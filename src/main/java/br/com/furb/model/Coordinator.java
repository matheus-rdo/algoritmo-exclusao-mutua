package br.com.furb.model;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import br.com.furb.Cluster;

public class Coordinator implements Runnable {

	private int id;
	private Queue<Runnable> resourceQueue;

	public Coordinator(int id) {
		this.id = id;
		this.resourceQueue = new ArrayBlockingQueue<>(20);
	}

	@Override
	public void run() {
		while (isClusterCoordinator()) {
			Runnable resource;
			while ((resource = resourceQueue.poll()) != null && isClusterCoordinator()) {
				resource.run();
			}
		}
	}

	public void addProcessing(Runnable process) {
		this.resourceQueue.add(process);
	}

	private boolean isClusterCoordinator() {
		Optional<Coordinator> maybeCoordinador = Cluster.getInstance().getCoordinator();
		return maybeCoordinador.isPresent() && maybeCoordinador.get().equals(this);
	}

	@Override
	public String toString() {
		return "[ID:" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinator other = (Coordinator) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
