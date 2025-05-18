//<Beginning of snippet n. 0>
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.Package;
import com.android.sdkmanager.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;

Label mLabel = new Label(parent, SWT.NONE);
mLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
mLabel.setText(String.format(
                "Android SDK Updater.\nRevision %1$s\nCopyright (C) 2009 The Android Open Source Project.",
                getRevision()));
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Arrays;

import javax.xml.transform.stream.StreamSource;

ByteArrayInputStream xml = fetchUrl(url, exception);
Document validatedDoc = null;
boolean usingAlternateXml = false;
String validatedUri = null;

if (xml != null) {
    monitor.setDescription("Validate XML");
    String uri = validateXml(xml, url, validationError, validatorFound);
    if (uri != null) {
        validatedDoc = getDocument(xml, monitor);
        validatedUri = uri;
    } else if (validatorFound[0].equals(Boolean.TRUE)) {
        validatedDoc = findAlternateToolsXml(xml);
        if (validatedDoc != null) {
            validationError[0] = null;
            validatedUri = SdkRepository.NS_SDK_REPOSITORY;
            usingAlternateXml = true;
        }
    } else {
        mFetchError = "XML Schema Validator not found. Please update your Java version.";
    }
}

if (validatedDoc == null && !url.endsWith(SdkRepository.URL_DEFAULT_XML_FILE)) {
    if (!url.endsWith("/")) {
        url += "/";
    }
    url += SdkRepository.URL_DEFAULT_XML_FILE;

    xml = fetchUrl(url, exception);
    if (xml != null) {
        String uri = validateXml(xml, url, validationError, validatorFound);
        if (uri != null) {
            validationError[0] = null;
            validatedDoc = getDocument(xml, monitor);
            validatedUri = uri;
        } else if (validatorFound[0].equals(Boolean.TRUE)) {
            validatedDoc = findAlternateToolsXml(xml);
            if (validatedDoc != null) {
                validationError[0] = null;
                validatedUri = SdkRepository.NS_SDK_REPOSITORY;
                usingAlternateXml = true;
            }
        } else {
            mFetchError = "XML Schema Validator not found. Please update your Java version.";
        }
    }

    if (validatedDoc != null) {
        monitor.setResult("Repository found at %1$s", url);
        mUrl = url;
    }
} else {
    monitor.setResult("Failed to fetch URL %1$s, reason: %2$s", url, reason);
}

if (validationError[0] != null) {
    monitor.setResult("%s", validationError[0]);
}

if (usingAlternateXml) {
    // Handle alternate XML logic
}

private ByteArrayInputStream fetchUrl(String urlString, Exception[] outException) {
    try {
        URL url = new URL(urlString);
        return new ByteArrayInputStream(url.openStream().readAllBytes());
    } catch (Exception e) {
        outException[0] = e;
    }
    return null;
}

private String validateXml(ByteArrayInputStream xml, String url, String[] outError, Boolean[] validatorFound) {
    String lastError = null;
    String extraError = null;
    for (int version = SdkRepository.NS_LATEST_VERSION; version >= 1; version--) {
        try {
            Validator validator = getValidator(version);
            if (validator == null) {
                lastError = "No suitable XML Schema Validator found. Please consider updating your Java.";
                validatorFound[0] = Boolean.FALSE;
                continue;
            }

            validatorFound[0] = Boolean.TRUE;
            xml.reset();
            validator.validate(new StreamSource(xml));
            return SdkRepository.getSchemaUri(version);
        } catch (Exception e) {
            lastError = "XML verification failed. Error: %2$s";
            extraError = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
        }
    }
    
    if (lastError != null) {
        outError[0] = String.format(lastError, url, extraError);
    }
    return null;
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
private static final String NS_SDK_REPOSITORY_BASE =
"http://schemas.android.com/sdk/android/repository/";

public static final String NS_SDK_REPOSITORY_PATTERN =
        NS_SDK_REPOSITORY_BASE + "[1-9][0-9]*";

public static final int NS_LATEST_VERSION = 3;

public static final String NS_SDK_REPOSITORY = getSchemaUri(NS_LATEST_VERSION);
//<End of snippet n. 2>

//<Beginning of snippet n. 3>
fail();
}

public void testLicenseIdNotFound() throws Exception {
    String document = "<?xml version=\"1.0\"?>" +
            OPEN_TAG +
            "<r:license id=\"lic1\"> some license </r:license> " +
            "<r:tool> <r:uses-license ref=\"lic2\" /> <r:revision>1</r:revision> " +
            "<r:archives> <r:archive os=\"any\"> <r:size>1</r:size> <r:checksum>2822ae37115ebf13412bbef91339ee0d9454525e</r:checksum> " +
            "<r:url>url</r:url> </r:archive> </r:archives> </r:tool>" +
            CLOSE_TAG;
}
//<End of snippet n. 3>