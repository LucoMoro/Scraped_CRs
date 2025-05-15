//<Beginning of snippet n. 0>


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

private String mUrl; // Assuming mUrl is defined elsewhere in the class
private String mUiName; // Assuming mUiName is defined elsewhere in the class

public String getShortDescription() {
    String domain = null;

    try {
        URL url = new URL(mUrl);
        domain = url.getHost();
    } catch (MalformedURLException e) {
        e.printStackTrace(); // Error handling for invalid URL
        return mUiName; // Return original mUiName if URL is invalid
    }

    if (domain != null && mUiName != null && !mUiName.contains(domain)) {
        mUiName += " " + domain; // Append the domain to mUiName if it's not present
    }
    
    return mUiName != null && mUiName.length() > 0 ? mUiName : mUrl;
}

//<End of snippet n. 0>