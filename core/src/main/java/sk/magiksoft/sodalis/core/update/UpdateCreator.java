package sk.magiksoft.sodalis.core.update;

import java.io.File;

/**
 * @author wladimiiir
 */
public class UpdateCreator {
    public static void main(String[] args) {
        if (args.length != 3) {
            return;
        }

        File updatesFile = new File(args[0]);
        String forVersion = args[1];
        File updateZipFile = new File(args[2]);

        UpdateManager.getInstance().generateUpdateZipFile(updatesFile, forVersion, updateZipFile);
    }
}
