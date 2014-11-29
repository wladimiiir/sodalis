package sk.magiksoft.sodalis.core.license;

import java.io.Serializable;

/**
 * @author wladimiiir
 */
public class FileProperty implements Serializable {
    private String filePath;
    private String propertyName;

    public FileProperty(String filePath, String propertyName) {
        this.filePath = filePath;
        this.propertyName = propertyName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
