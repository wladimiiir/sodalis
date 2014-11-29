package sk.magiksoft.sodalis.core.settings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author wladimiiir
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SettingsValue {
    int columnWidth() default 1;

    Class customComponentClass() default Object.class;

    String regex() default "";

    int minValue() default Integer.MIN_VALUE;

    int maxValue() default Integer.MAX_VALUE;
}
