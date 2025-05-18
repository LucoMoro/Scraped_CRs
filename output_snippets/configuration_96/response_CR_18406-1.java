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

    String domain = "";
    try {
        URL url = new URL(mUrl);
        domain = url.getHost();
    } catch (MalformedURLException e) {
        return "Invalid URL: " + mUiName;
    }

    if (domain != null && !domain.isEmpty()) {
        mUiName = mUiName.trim();
        if (!mUiName.isEmpty() && !mUiName.contains(domain)) {
            mUiName += " " + domain;
        }
    }

    return mUiName;
}

//<End of snippet n. 0>