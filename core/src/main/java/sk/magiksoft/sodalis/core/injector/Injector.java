package sk.magiksoft.sodalis.core.injector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class Injector {

    private static Map<String, Object> resourceMap = new HashMap<String, Object>();

    private Injector() {
    }

    public static void registerResource(Class clazz, Object object) {
        resourceMap.put(clazz.getName(), object);
    }

    public static void inject(Object object) {
        final Class clazz = object.getClass();
        Resource resource;

        for (Field field : clazz.getDeclaredFields()) {
            resource = field.getAnnotation(Resource.class);
            if (resource == null) {
                continue;
            }
            if ((resource.name().isEmpty() && resourceMap.get(field.getType().getName()) == null)
                    || (!resource.name().isEmpty() && resourceMap.get(resource.name()) == null)) {
                continue;
            }

            field.setAccessible(true);
            try {
                field.set(object, resource.name().isEmpty()
                        ? resourceMap.get(field.getType().getName())
                        : resourceMap.get(resource.name()));
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }
}
