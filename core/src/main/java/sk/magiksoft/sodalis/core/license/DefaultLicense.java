
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
import sk.magiksoft.sodalis.person.entity.Person;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author wladimiiir
 */
public class DefaultLicense implements License {

    private static final long serialVersionUID = 7205814128935234076L;
    private String licenseNumber;
    private Person person;
    private byte[] publicKey;
    private Map<FileProperty, byte[]> fileSignatureMap;
    private List<String> restrictions;
    private Map<String, Object> properties;
    private transient PublicKey pk;
    private boolean debugMode;

    public DefaultLicense(String licenseNumber, Person person, byte[] publicKey, Map<FileProperty, byte[]> fileSingatureMap, List<String> restrictions, Map<String, Object> properties, boolean debugMode) {
        this.licenseNumber = licenseNumber;
        this.person = person;
        this.publicKey = publicKey;
        this.fileSignatureMap = fileSingatureMap;
        this.restrictions = restrictions;
        this.properties = properties;
        this.debugMode = debugMode;
    }

    @Override
    public boolean isDebugMode() {
        return debugMode;
    }

    @Override
    public String getLicenseNumber() {
        return licenseNumber;
    }

    @Override
    public Person getLicensePerson() {
        return person;
    }

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public Object getProperty(String key, Object defaultValue) {
        return properties.containsKey(key) ? properties.get(key) : defaultValue;
    }

    @Override
    public boolean isRestricted(String key) {
        return restrictions.contains(key);
    }

    @Override
    public void verifyFiles() throws LicenseException {
        File file;

        for (Map.Entry<FileProperty, byte[]> entry : fileSignatureMap.entrySet()) {
            if (entry.getKey().getPropertyName() != null && System.getProperty(entry.getKey().getPropertyName()) != null) {
                file = new File(System.getProperty(entry.getKey().getPropertyName()));
            } else {
                file = new File(entry.getKey().getFilePath());
            }
            if (!file.exists() || !verifyFile(file, entry.getValue())) {
                throw new LicenseException(MessageFormat.format(LocaleManager.getString("license.fileVerificationFailed"), file.getAbsolutePath()));
            }
        }
    }

    private boolean verifyFile(File file, byte[] signature) {
        try {
            if (pk == null) {
                try {
                    initPublicKey();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            BufferedInputStream bufin = new BufferedInputStream(new FileInputStream(file));
            sig.initVerify(pk);
            byte[] buffer = new byte[1024];
            int size;
            while (bufin.available() != 0) {
                size = bufin.read(buffer);
                sig.update(buffer, 0, size);
            }
            bufin.close();

            return sig.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean verifyFile(File file) {
        byte[] signature = fileSignatureMap.get(file.getName());

        if (signature == null) {
            return true;
        }

        return verifyFile(file, signature);
    }

    private void initPublicKey() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey);

        pk = keyFactory.generatePublic(pubKeySpec);
    }
}