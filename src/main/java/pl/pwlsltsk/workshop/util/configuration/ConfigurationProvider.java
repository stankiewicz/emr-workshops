package pl.pwlsltsk.workshop.util.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author pwlsltsk
 */
public class ConfigurationProvider {

    private final Properties properties = new Properties();

    public ConfigurationProvider() {
        try (InputStream inputStream = ConfigurationProvider.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }

    public String getMainBucketName() {

        return properties.getProperty(ConfigurationProperty.BUCKET_NAME.getLabel());
    }

    public String getUsername() {

        return properties.getProperty(ConfigurationProperty.USERNAME.getLabel());
    }

    public String getUserFolder() {

        return String.format("s3://%s/%s", getMainBucketName(), getUsername());
    }

    public String getResourcesFolder() {

        return String.format("s3://%s/resources", getMainBucketName());
    }

    public String getDataLocation() {

        return getResourcesFolder() + "/data";
    }

    public String getResultsLocation() {

        return getUserFolder() + "/results";
    }

    public String getLogsLocation() {

        return getUserFolder() + "/logs";
    }

    public String getJarsLocation() {

        return getResourcesFolder() + "/jars";
    }

    public String getAmiVersion() {

        return "3.2.1";
    }

    enum ConfigurationProperty {

        USERNAME("username"),
        BUCKET_NAME("bucket");

        private String label;

        private ConfigurationProperty(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
