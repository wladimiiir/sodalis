
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/


package sk.magiksoft.sodalis.core;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 11/25/10
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
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

