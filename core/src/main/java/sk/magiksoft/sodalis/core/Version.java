package sk.magiksoft.sodalis.core;

/**
 * @author wladimiiir
 */
public class Version {
    public static String getVersion() {
        return Version.class.getPackage().getImplementationVersion();
    }

    public static void main(String[] args) {
        System.out.println(getVersion());
    }
}
