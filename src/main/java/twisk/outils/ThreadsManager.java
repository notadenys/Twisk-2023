package twisk.outils;

import javafx.concurrent.Task;

import java.util.ArrayList;

public final class ThreadsManager {
    // The field must be declared volatile so that double check lock would work correctly.
    private static volatile ThreadsManager instance;

    private final ArrayList<Thread> threads;

    private ThreadsManager() {
        threads = new ArrayList<>();
    }

    public static ThreadsManager getInstance() {
        ThreadsManager result = instance;
        if (result != null) {
            return result;
        }
        // Thread safe creation of instance
        synchronized (ThreadsManager.class) {
            if (instance == null) {
                instance = new ThreadsManager();
            }
            return instance;
        }
    }

    public void lancer(Task<Void> task) {
        Thread thread = new Thread(task);
        threads.add(thread);
        thread.start();
    }

    public void detruireTout() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    public boolean isEmpty() {
        return threads.isEmpty();
    }
}
