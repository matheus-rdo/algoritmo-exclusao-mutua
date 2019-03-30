package br.com.furb.task;

import br.com.furb.Cluster;

import java.util.TimerTask;

public class CreateProcessTask extends TimerTask {

    @Override
    public void run() {
        Cluster.getInstance().createNewProcess();
    }

}
