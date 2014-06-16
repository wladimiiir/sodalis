
/***********************************************\
 *  Copyright (c) 2010 by Ing.Vladimir Hrusovsky *
 *  Sodalis 2007-2011                            *
 *  http://www.sodalis.sk                        *
 \***********************************************/
    
     
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.magiksoft.sodalis.core.license;

import sk.magiksoft.sodalis.core.locale.LocaleManager;
import sk.magiksoft.sodalis.core.logger.LoggerManager;
import sk.magiksoft.sodalis.core.security.CryptoUtils;

import javax.crypto.CipherInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.MessageFormat;

/**
 * @author wladimiiir
 */
public class LicenseManager {

    private static final File LICENSE_FILE = new File("data/hudini/license");
    private static final byte[] ABCD = new byte[]{-11, -45, -109, 37, 102, -77, 8, -32, -97, -71, 45, -22, -3, -65, 126, 59};

    protected License license;

    public LicenseManager() throws LicenseException {
        init();
    }

    protected void init() throws LicenseException {
        loadLicense();
    }

    private void loadLicense() throws LicenseException {
        if (!LICENSE_FILE.exists()) {
            throw new LicenseException(LocaleManager.getString("license.fileNotFound"));
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new CipherInputStream(
                    new FileInputStream(LICENSE_FILE), CryptoUtils.getDecryptCipher(new String(CryptoUtils.getDecryptCipher("property.password").doFinal(ABCD)))));
            license = (License) ois.readObject();

            ois.close();
        } catch (Exception ex) {
            LoggerManager.getInstance().error(getClass(), ex);
            throw new LicenseException(LocaleManager.getString("license.invalidFile"));
        }
    }

    protected void verifyFiles() throws LicenseException {
        if (license.isDebugMode()) {
            return;
        }

        license.verifyFiles();
    }

    public void verifyFile(File file) throws LicenseException {
        if (!license.verifyFile(file)) {
            throw new LicenseException(MessageFormat.format(LocaleManager.getString("license.fileVerificationFailed"), file.getAbsolutePath()));
        }
    }

    public License getLicense() {
        return license;
    }
}