package sk.magiksoft.sodalis.core.data.postgresql;

/**
 * @author wladimiiir
 */
public class PostgreSQLInstaller {

    public static void main(String[] args) {
        System.setProperty("installation", "TRUE");

        PostgreSQLManager.initDB();
        final PostgreSQLManager manager = new PostgreSQLManager();

        manager.createUser();
        manager.recreateDB();

        System.exit(0);
    }
}
