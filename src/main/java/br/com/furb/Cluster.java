package br.com.furb;

import br.com.furb.model.Coordinator;
import br.com.furb.model.SystemProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Cluster {

	private static final Logger log = LoggerFactory.getLogger(Cluster.class);

	private static Cluster instance;

	private List<SystemProcess> processes;

	private Optional<Coordinator> coordinator;

	private Cluster() {
		this.processes = new ArrayList<>();
		this.coordinator = Optional.empty();
	}

	public static Cluster getInstance() {
		return instance == null ? instance = new Cluster() : instance;
	}

	public void createNewProcess() {
		SystemProcess process = generateProcess();
		log.info("Criando novo processo " + process.toString());
		this.processes.add(process);
		start(process);
	}

	public List<SystemProcess> getProcesses() {
		return processes;
	}

	public void desactivateCoordinator() {
		if (coordinator.isPresent()) {
			log.info(String.format("Coordenador %s foi desativado", coordinator.get()));
		}
		this.coordinator = Optional.empty();
	}

	public Optional<Coordinator> getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(SystemProcess processCoordinator) {
		this.coordinator = Optional.of(new Coordinator(processCoordinator.getId()));
		this.processes.remove(processCoordinator);
		if (coordinator.isPresent()) {
			start(this.coordinator.get());
			log.info(String.format("Processo %s foi eleito o novo coordenador", coordinator.get().toString()));
		}
	}

	private SystemProcess generateProcess() {
		SystemProcess randomProcess = new SystemProcess(new Random().nextInt(100));
		boolean exists = processes.stream().filter(p -> p.equals(randomProcess)).findAny().isPresent();
		if (exists)
			return generateProcess();

		return randomProcess;
	}

	private void start(Runnable runnable) {
		new Thread(runnable).start();
	}

}
