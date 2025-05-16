//<Beginning of snippet n. 0>
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public String getShortDescription() {
    if (mUrl == null || mUrl.length() == 0 || mUiName == null) {
        return mUrl;
    }

    String domain = "";
    try {
        URL url = new URL(mUrl);
        domain = url.getHost();
    } catch (MalformedURLException e) {
        return mUrl;
    }

    if (!mUiName.contains(domain) && !domain.isEmpty()) {
        mUiName += " (" + domain + ")";
    }
    
    return mUiName;
}
//<End of snippet n. 0>