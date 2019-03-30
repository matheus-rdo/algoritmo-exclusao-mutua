package br.com.furb.task;

import java.util.TimerTask;

import br.com.furb.Cluster;

public class DesactivateCoordinatorTask extends TimerTask {

    @Override
    public void run() {
        Cluster.getInstance().desactivateCoordinator();
    }

}
