package sk.magiksoft.sodalis.core.splash;

import java.awt.*;

/**
 * @author wladimiiir
 */
public interface SplashAction extends Runnable {
    Image getSplashImage();

    String getActionName();
}
