package sk.magiksoft.sodalis.core.function;

/**
 * @author wladimiiir
 * @since 2011/1/21
 */
public interface ResultFunction<R, O> {
    R apply(O object);
}
