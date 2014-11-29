package sk.magiksoft.sodalis.core.splash;

import java.awt.*;

/**
 * @author wladimiiir
 */
public abstract class AbstractSplashAction implements SplashAction {

    private Image splashImage;
    private String actionName;

    public AbstractSplashAction(Image splashImage, String actionName) {
        this.splashImage = splashImage;
        this.actionName = actionName;
    }

    @Override
    public Image getSplashImage() {
        return splashImage;
    }

    @Override
    public String getActionName() {
        return actionName;
    }
}
