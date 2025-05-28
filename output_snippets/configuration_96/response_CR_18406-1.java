//<Beginning of snippet n. 0>


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public String getShortDescription() {
    String mUiName = ""; // Initialize mUiName to an empty string

    if (mUrl == null || mUrl.isEmpty()) {
        return mUiName;
    }

    try {
        URL url = new URL(mUrl);
        String domain = url.getHost();

        if (domain == null || domain.isEmpty()) {
            return "Invalid URL: No domain found in the provided URL: " + mUrl;
        }

        mUiName = mUiName.trim();
        if (mUiName.isEmpty() || !mUiName.contains(domain)) {
            mUiName = (mUiName.isEmpty()) ? domain : mUiName + " " + domain;
        }

    } catch (MalformedURLException e) {
        return "Invalid URL: " + mUrl;
    }

    return mUiName.trim(); // Trim before returning
}

//<End of snippet n. 0>