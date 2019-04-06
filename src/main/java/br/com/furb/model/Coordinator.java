package br.com.furb.model;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import br.com.furb.Cluster;

public class Coordinator implements Runnable {

	private int id;
	private Queue<Runnable> resourceQueue;

	public Coordinator(int id) {
		this.id = id;
		this.resourceQueue = new LinkedList<Runnable>();
	}

	@Override
	public void run() {
		Optional<Coordinator> maybeCoordinador;
		while ((maybeCoordinador = Cluster.getInstance().getCoordinator()).isPresent()
				&& maybeCoordinador.get().equals(this)) {
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
