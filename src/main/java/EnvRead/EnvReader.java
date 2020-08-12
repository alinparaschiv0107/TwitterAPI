package EnvRead;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvReader {

    private static final Properties properties = new Properties();

    static {
        InputStream is = EnvReader.class.getClassLoader().getResourceAsStream("env.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getApiKey() {
        return properties.getProperty("apiKey");
    }

    public static String getApiSecret() {
        return properties.getProperty("apiSecret");
    }

    public static String getAccessToken() {
        return properties.getProperty("accessToken");
    }

    public static String getAccessSecret() {
        return properties.getProperty("accessSecret");
    }

    public static String getBaseUri() { return properties.getProperty("baseUri");}

    public static String getBasePathStatus() { return properties.getProperty("basePathStatus");}

    public static String getBasePathFavorites() { return properties.getProperty("basePathFavo");}

    public static String getScreenName() { return properties.getProperty("screenName");}
}
