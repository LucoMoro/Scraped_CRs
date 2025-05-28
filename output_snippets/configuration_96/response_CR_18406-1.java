//<Beginning of snippet n. 0>

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public String getShortDescription() {
    if (mUrl == null || mUrl.isEmpty()) {
        return mUiName != null ? mUiName : "";
    }

    String domain = "";
    try {
        URL url = new URL(mUrl);
        domain = url.getHost();
    } catch (MalformedURLException e) {
        return "Invalid URL: " + mUiName != null ? mUiName : "";
    }

    if (domain != null && !domain.isEmpty() && mUiName != null) {
        if (mUiName.trim().endsWith(" ")) {
            mUiName = mUiName.trim();
        }
        if (!mUiName.contains(domain)) {
            mUiName += " " + domain;
        }
    } else {
        return mUiName != null ? mUiName : "";
    }

    return mUiName != null ? mUiName : "";
}

//<End of snippet n. 0>