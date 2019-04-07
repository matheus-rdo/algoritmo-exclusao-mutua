package br.com.furb.model;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import br.com.furb.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.furb.Cluster;

public class SystemProcess implements Runnable {

	private static final long MIN_PROCESS_TIME = 5000;
	private static final long MAX_PROCESS_TIME = 15000;

	private static final long MIN_CONSUME_DELAY = 5000;
	private static final long MAX_CONSUME_DELAY = 15000;

	private static final Logger log = LoggerFactory.getLogger(SystemProcess.class);
	private static final Logger coordinatorLog = LoggerFactory.getLogger(Coordinator.class);

	private int id;

	public SystemProcess(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void consumeResource() {
		Optional<Coordinator> maybeCoordinator = Cluster.getInstance().getCoordinator();
		if (!maybeCoordinator.isPresent()) {
			log.info("Uma eleição foi convocada pelo processo " + this.toString());
			startElection();
		} else {
			log.info("{} enviou um recurso para o coordenador", toString());
			maybeCoordinator.get().addProcessing(getResourceProcess());
		}
	}

	public Runnable getResourceProcess() {
		return () -> {
			long processingMs = RandomUtil.nextLong(MIN_PROCESS_TIME, MAX_PROCESS_TIME);
			long seconds = TimeUnit.MILLISECONDS.toSeconds(processingMs);
			coordinatorLog.info(
					String.format("Processando recurso do processo %s por %d segundos", this.toString(), seconds));
			sleep(processingMs);
		};
	}

	@Override
	public void run() {
		while (Cluster.getInstance().getProcesses().contains(this)) {
			long timeToConsumeResource = RandomUtil.nextLong(MIN_CONSUME_DELAY, MAX_CONSUME_DELAY);
			sleep(timeToConsumeResource);
			consumeResource();
		}
	}

	private void sleep(long milisseconds) {
		try {
			Thread.sleep(milisseconds);
		} catch (InterruptedException e) {
		}
	}

	private void startElection() {
		Cluster cluster = Cluster.getInstance();
		if (!cluster.getCoordinator().isPresent()) {
			List<SystemProcess> processes = cluster.getProcesses();
			List<SystemProcess> biggerProcesses = processes.stream().filter(p -> p.id > this.id)
					.collect(Collectors.toList());
			if (biggerProcesses.isEmpty()) {
				cluster.setCoordinator(this);
			} else {
				biggerProcesses.stream().forEach(process -> process.startElection());
			}
		}
	}

	@Override
	public String toString() {
		return "[ID:" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		SystemProcess other = (SystemProcess) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
