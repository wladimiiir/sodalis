package sk.magiksoft.sodalis.core.entity;

/**
 * @author wladimiiir
 */
public class EmailServer extends AbstractDatabaseEntity {

    private String emailAddress;
    private String fullName;
    private String hostname;
    private String username;
    private byte[] password;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public void updateFrom(DatabaseEntity entity) {
        if (!(entity instanceof EmailServer)) {
            return;
        }
        EmailServer server = (EmailServer) entity;

        this.emailAddress = server.emailAddress;
        this.fullName = server.fullName;
        this.hostname = server.hostname;
        this.username = server.username;
        this.password = server.password;
    }
}
