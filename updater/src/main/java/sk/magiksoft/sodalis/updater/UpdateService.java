package sk.magiksoft.sodalis.updater;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * User: wladimiiir
 * Date: 6/15/14
 * Time: 8:02 PM
 */
@WebService()
public class UpdateService {
    private static final String MODULE_KEY_PREFIX = "module.";
    private static final String UPDATES_XML_FILENAME = System.getProperty("updates.xml", "/home/wladimiiir/-development/--build--/sodalis/updates.xml");
    private static final File UPDATES_XML_FILE = new File(UPDATES_XML_FILENAME);
    private static final Comparator<Element> UPDATE_COMPARATOR = new Comparator<Element>() {

        @Override
        public int compare(Element o1, Element o2) {
            String v1 = o1.getAttributeValue("version");
            String v2 = o2.getAttributeValue("version");

            return VERSION_COMPARATOR.compare(v1, v2);
        }
    };
    private static final Comparator<String> VERSION_COMPARATOR = new Comparator<String>() {

        @Override
        public int compare(String v1, String v2) {
            v1 = v1.substring(v1.lastIndexOf(".") + 1);
            v2 = v2.substring(v2.lastIndexOf(".") + 1);

            return Integer.valueOf(v1) - Integer.valueOf(v2);
        }
    };

    @WebMethod
    public String createUpdate(@WebParam(name = "properties") HashMap<String, String> properties) {
        Set<File> files = new HashSet<>();
        ZipEntry zipEntry;
        ZipOutputStream zos;
        BufferedInputStream bis;
        File updateFile;
        Document updateDocument;
        File updateZipFile;
        String moduleName;
        String currentVersion;
        byte[] bytes = new byte[2048];
        int count;


        updateZipFile = new File("update_" + System.currentTimeMillis() + ".zip");

        for (String key : properties.keySet()) {
            if (!key.startsWith(MODULE_KEY_PREFIX)) {
                continue;
            }

            moduleName = key.substring(MODULE_KEY_PREFIX.length());
            currentVersion = properties.get(key);
            updateFile = new File("updates-" + moduleName + ".xml");
            if (!updateFile.exists()) {
                continue;
            }
            try {
                updateDocument = new SAXBuilder().build(updateFile);
                if (currentVersion.equals(getActualVersion(updateDocument))) {
                    continue;
                }
                files.addAll(getUpdateFilesForVersion(updateDocument, currentVersion));
            } catch (JDOMException ex) {
                Logger.getLogger(UpdateService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UpdateService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (files.isEmpty()) {
            return null; //no update
        }
        try {
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(updateZipFile)));
            for (File file : files) {
                zipEntry = new ZipEntry(file.getPath());
                zos.putNextEntry(zipEntry);
                bis = new BufferedInputStream(new FileInputStream(file));
                while ((count = bis.read(bytes, 0, 2048)) != -1) {
                    zos.write(bytes, 0, count);
                }
                zos.closeEntry();
                bis.close();
            }
            zos.close();

            return updateZipFile.getPath();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UpdateService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UpdateService.class.getName()).log(Level.SEVERE, null, ex);
        }


        return null;
    }

    private String getActualVersion(Document updateDocument) {
        List<Element> updates = new ArrayList<Element>(updateDocument.getRootElement().getChildren());

        Collections.sort(updates, Collections.reverseOrder(UPDATE_COMPARATOR));

        for (Element update : updates) {
            return update.getAttributeValue("version");
        }

        return null;
    }

    private List<File> getUpdateFilesForVersion(Document updateDocument, String forVersion) {
        Set<File> files = new HashSet<File>();
        List<Element> updates = new ArrayList<Element>(updateDocument.getRootElement().getChildren());

        Collections.sort(updates, Collections.reverseOrder(UPDATE_COMPARATOR));

        for (Element update : updates) {
            if (VERSION_COMPARATOR.compare(update.getAttributeValue("version"), forVersion) <= 0) {
                break;
            }
            for (int i = 0; i < update.getChildren().size(); i++) {
                Element fileElement = (Element) update.getChildren().get(i);
                files.add(new File(fileElement.getTextTrim()));
            }
        }

        return new ArrayList<File>(files);
    }
}
