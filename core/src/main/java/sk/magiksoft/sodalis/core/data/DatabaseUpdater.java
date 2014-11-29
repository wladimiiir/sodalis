package sk.magiksoft.sodalis.core.data;

/**
 * @author wladimiiir
 */
public class DatabaseUpdater {
    public static void main(String[] args) {
        new DatabaseUpdater().updateDB();
    }

    private void updateDB() {
        SchemaCreator.updateDBSchema();
    }
}
