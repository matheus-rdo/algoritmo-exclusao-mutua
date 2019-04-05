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
        int id = generateProcessId();
        SystemProcess process = new SystemProcess(id);
        log.info("Criando novo processo " + process.toString());
        this.processes.add(process);
    }

    public void desactivateCoordinator() {
        if (coordinator.isPresent()) {
            coordinator.get().interrupt();
            log.info(String.format("Coordenador %s foi desativado", coordinator.get()));
        }
        this.coordinator = Optional.empty();
    }

    private int generateProcessId() {
        int id = new Random().nextInt(100);
        boolean exists = processes.stream().filter(p -> p.getId() == id).findAny().isPresent();
        if (exists)
            return generateProcessId();

        return id;
    }

    public Optional<Coordinator> getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(SystemProcess processCoordinator) {
        this.coordinator = Optional.of(new Coordinator(processCoordinator.getId()));
        processCoordinator.interrupt();
        this.processes.remove(processCoordinator);
        if (coordinator.isPresent()) {
            log.info(String.format("Processo %s foi eleito o novo coordenador", coordinator.get().toString()));
        }
    }

    public List<SystemProcess> getProcesses() {
        return processes;
    }

}
