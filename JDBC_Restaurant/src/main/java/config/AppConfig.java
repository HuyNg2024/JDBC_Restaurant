package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration manager.
 * Reads database and application settings from config.properties.
 */
public class AppConfig {

    private static AppConfig instance;
    private final Properties properties;

    private static final String CONFIG_FILE = "config.properties";

    // Default values
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/restaurantmanage";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "12345";
    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";

    private AppConfig() {
        properties = new Properties();
        loadConfig();
    }

    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    private void loadConfig() {
        // Try loading from file system first
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            System.out.println("✅ Config loaded from: " + CONFIG_FILE);
        } catch (IOException e) {
            // Try loading from classpath
            try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
                if (input != null) {
                    properties.load(input);
                    System.out.println("✅ Config loaded from classpath");
                } else {
                    System.out.println("⚠️ Config file not found, using defaults");
                    setDefaults();
                }
            } catch (IOException ex) {
                System.out.println("⚠️ Error loading config, using defaults");
                setDefaults();
            }
        }
    }

    private void setDefaults() {
        properties.setProperty("db.url", DEFAULT_URL);
        properties.setProperty("db.user", DEFAULT_USER);
        properties.setProperty("db.password", DEFAULT_PASSWORD);
        properties.setProperty("db.driver", DEFAULT_DRIVER);
        properties.setProperty("app.name", "Restaurant Management System");
        properties.setProperty("app.version", "2.0");
    }

    public String getDbUrl() {
        return properties.getProperty("db.url", DEFAULT_URL);
    }

    public String getDbUser() {
        return properties.getProperty("db.user", DEFAULT_USER);
    }

    public String getDbPassword() {
        return properties.getProperty("db.password", DEFAULT_PASSWORD);
    }

    public String getDbDriver() {
        return properties.getProperty("db.driver", DEFAULT_DRIVER);
    }

    public String getAppName() {
        return properties.getProperty("app.name", "Restaurant Management System");
    }

    public String getAppVersion() {
        return properties.getProperty("app.version", "2.0");
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Reload configuration from file.
     */
    public void reload() {
        properties.clear();
        loadConfig();
    }
}
