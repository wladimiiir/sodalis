/*
 * Copyright (c) 2011
 */

package sk.magiksoft.sodalis.core.action;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: wladimiiir
 * Date: 6/24/11
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Checker extends EventListener {
    boolean check();
}
