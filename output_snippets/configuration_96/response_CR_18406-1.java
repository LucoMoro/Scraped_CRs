//<Beginning of snippet n. 0>
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public String getShortDescription() {
    String domain = "";
    Logger logger = Logger.getLogger(getClass().getName());
    
    if (mUrl == null || mUrl.isEmpty()) {
        return mUiName != null ? mUiName : ""; // Return empty if mUrl is null or empty
    }

    try {
        URL url = new URL(mUrl);
        domain = url.getHost();
    } catch (MalformedURLException e) {
        logger.log(Level.SEVERE, "MalformedURLException: {0}", e.getMessage());
        return mUiName != null ? mUiName : ""; // Return mUiName in case of error, or empty if null
    }

    if (mUiName != null && !mUiName.isEmpty()) {
        if (!mUiName.toLowerCase().contains(domain.toLowerCase())) {
            mUiName += " " + domain;
        }
        return mUiName;
    }
    return domain; // Return domain if mUiName is null or empty
}
//<End of snippet n. 0>