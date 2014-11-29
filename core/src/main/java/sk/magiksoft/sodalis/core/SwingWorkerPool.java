package sk.magiksoft.sodalis.core;

import javax.swing.*;

/**
 * @author wladimiiir
 * @since 2010/11/25
 */
public class SwingWorkerPool {
    private static SwingWorkerPool instance = new SwingWorkerPool();

    public static SwingWorkerPool getInstance() {
        return instance;
    }

    private SwingWorkerPool() {
    }

    private SwingWorker swingWorker;


}

