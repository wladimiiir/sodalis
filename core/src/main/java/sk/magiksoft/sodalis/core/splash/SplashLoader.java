package sk.magiksoft.sodalis.core.splash;

import java.awt.*;
import java.util.List;

/**
 * @author wladimiiir
 */
public interface SplashLoader {
    String getTitle();

    Image getIconImage();

    List<SplashAction> getSplashActions();

    void loaderFinished();

    void loaderCancelled(Throwable e);
}
