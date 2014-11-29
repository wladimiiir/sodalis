package sk.magiksoft.sodalis.core.settings.storage;

import java.io.Serializable;

/**
 * @author wladimiiir
 */
public class Storage implements Serializable {
    private static final long serialVersionUID = 1L;
    private byte[] bytes;

    public Storage(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
