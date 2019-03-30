package br.com.furb.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.furb.Cluster;

public class SystemProcess {

    private static final Logger log = LoggerFactory.getLogger(Cluster.class);

    private int id;

    public SystemProcess(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void requestToCoordinator() {
        if (!Cluster.getInstance().getCoordinator().isPresent()) {
            log.info("Uma eleição foi convocada pelo processo " + this.toString());
            startElection();
        }
    }

    private void startElection() {
        Cluster cluster = Cluster.getInstance();
        if (!cluster.getCoordinator().isPresent()) {
            List<SystemProcess> processes = cluster.getProcesses();
            List<SystemProcess> biggerProcesses = processes.stream().filter(p -> p.id > this.id)
                    .collect(Collectors.toList());
            if (biggerProcesses.isEmpty()) {
                cluster.setCoordinator(Optional.of(new Coordinator(this.id)));
            } else {
                biggerProcesses.stream().forEach(process -> {
                    process.startElection();
                });
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
