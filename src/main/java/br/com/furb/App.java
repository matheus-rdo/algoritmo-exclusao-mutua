package br.com.furb;

import br.com.furb.task.CreateProcessTask;
import br.com.furb.task.DesactivateCoordinatorTask;

import java.util.Timer;

public class App {

    private static final int CREATE_PROCESS_DELAY = 40000;
    private static final int DESACTIVATE_COORDINATOR_DELAY = 100000;

    public static void main(String[] args) throws InterruptedException {
        scheduleTasks();
    }

    /**
     * Agenda as rotinas periodicas
     */
    private static void scheduleTasks() {
        Timer timer = new Timer();
        timer.schedule(new DesactivateCoordinatorTask(), DESACTIVATE_COORDINATOR_DELAY, DESACTIVATE_COORDINATOR_DELAY);
        timer.schedule(new CreateProcessTask(), CREATE_PROCESS_DELAY, CREATE_PROCESS_DELAY);
    }

}
