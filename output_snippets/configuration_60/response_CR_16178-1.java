//<Beginning of snippet n. 0>



import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.Package;
import com.android.sdkmanager.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
mLabel = new Label(parent, SWT.NONE);
mLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
mLabel.setText(String.format(
                "Android SDK Updater.\nRevision %1$s\nCopyright (C) 2009 The Android Open Source Project.",
                getRevision()));
}

@Override

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.net.ssl.SSLKeyException;

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
        
        // Check XML namespace
        String namespace = validatedDoc.getDocumentElement().getNamespaceURI();
        if (!SdkRepository.NS_SDK_REPOSITORY_PATTERN.equals(namespace)) {
            validationError[0] = "Invalid XML namespace: " + namespace;
            return;
        }
        
        // Extracted version from the document
        int schemaVersion = Integer.parseInt(validatedDoc.getDocumentElement().getAttribute("version"));
        if (schemaVersion > SdkRepository.NS_LATEST_VERSION) {
            createUpgradeToolsNode(validatedDoc);
        }
    } else if (validatorFound[0].equals(Boolean.TRUE)) {
        validatedDoc = findAlternateToolsXml(xml);
        if (validatedDoc != null) {
            validationError[0] = null;  
            validatedUri = SdkRepository.NS_SDK_REPOSITORY;
            usingAlternateXml = true;
        }
    } else {
        mFetchError = "No suitable XML Schema Validator could be found in your Java environment. Please update your version of Java.";
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

            // Check XML namespace
            String namespace = validatedDoc.getDocumentElement().getNamespaceURI();
            if (!SdkRepository.NS_SDK_REPOSITORY_PATTERN.equals(namespace)) {
                validationError[0] = "Invalid XML namespace: " + namespace;
                return;
            }
            
            // Extracted version from the document
            int schemaVersion = Integer.parseInt(validatedDoc.getDocumentElement().getAttribute("version"));
            if (schemaVersion > SdkRepository.NS_LATEST_VERSION) {
                createUpgradeToolsNode(validatedDoc);
            }
        } else if (validatorFound[0].equals(Boolean.TRUE)) {
            validatedDoc = findAlternateToolsXml(xml);
            if (validatedDoc != null) {
                validationError[0] = null;  
                validatedUri = SdkRepository.NS_SDK_REPOSITORY;
                usingAlternateXml = true;
            }
        } else {
            mFetchError = "No suitable XML Schema Validator could be found in your Java environment. Please update your version of Java.";
        }
    }

    if (validatedDoc != null) {
        monitor.setResult("Repository found at %1$s", url);
        mUrl = url;
    }
}

monitor.setResult("Failed to fetch URL %1$s, reason: %2$s", url, reason);

if(validationError[0] != null) {
    monitor.setResult("%s", validationError[0]);  
}

if (usingAlternateXml) {
// Is the manager running from inside ADT?
// We check that com.android.ide.eclipse.adt.AdtPlugin exists using reflection.
}

/**
 * Fetches the document at the given URL and returns it as a string.
 * Returns null if anything wrong happens and write errors to the monitor.
 *
 * References:
 * Java URL Connection: http://java.sun.com/docs/books/tutorial/networking/urls/readingWriting.html
 * Java URL Reader: http://java.sun.com/docs/books/tutorial/networking/urls/readingURL.html
 * Java set Proxy: http://java.sun.com/docs/books/tutorial/networking/urls/_setProxy.html
 */
private ByteArrayInputStream fetchUrl(String urlString, Exception[] outException) {
    URL url;
    try {
        // Add URL fetching logic here, if necessary
    } catch (Exception e) {
        outException[0] = e;
    }

    return null;
}

/**
 * Validates this XML against one of the possible SDK Repository schema, starting by the most recent one.
 * If the XML was correctly validated, returns the schema that worked.
 * If no schema validated the XML, returns null.
 */
private String validateXml(ByteArrayInputStream xml, String url,
                           String[] outError, Boolean[] validatorFound) {
    String lastError = null;
    String extraError = null;
    for (int version = SdkRepository.NS_LATEST_VERSION; version >= 1; version--) {
        try {
            Validator validator = getValidator(version);
            if (validator == null) {
                lastError = "XML verification failed for %1$s.\nNo suitable XML Schema Validator could be found in your Java environment. Please consider updating your version of Java.";
                validatorFound[0] = Boolean.FALSE;
                continue;
            }

            validatorFound[0] = Boolean.TRUE;
            xml.reset();
            validator.validate(new StreamSource(xml));
            return SdkRepository.getSchemaUri(version);

        } catch (Exception e) {
            lastError = "XML verification failed for %1$s.\nError: %2$s";
            extraError = e.getMessage();
            if (extraError == null) {
                extraError = e.getClass().getName();
            }
        }
    }

    if (lastError != null) {
        outError[0] = String.format(lastError, url, extraError);
    }
    return null;
}

/**
// Validate XML namespace ensure it’s sdk:sdk-repository
// <sdk:sdk-repository
//    xmlns:sdk="http://schemas.android.com/sdk/android/repository/$N">
*/

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


private static final String NS_SDK_REPOSITORY_BASE =
"http://schemas.android.com/sdk/android/repository/";                   //$NON-NLS-1$

/** The pattern of our XML namespace. */
public static final String NS_SDK_REPOSITORY_PATTERN =
        NS_SDK_REPOSITORY_BASE + "[1-9][0-9]*";        //$NON-NLS-1$

/** The latest version of the sdk-repository XML Schema.
* Valid version numbers are between 1 and this number, included. */
public static final int NS_LATEST_VERSION = 2;

/** The XML namespace of the latest sdk-repository XML. */
public static final String NS_SDK_REPOSITORY = getSchemaUri(NS_LATEST_VERSION);

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


fail();
}

    /** A document an unknown license id. */
public void testLicenseIdNotFound() throws Exception {
// we define a license named "lic1" and then reference "lic2" instead
String document = "<?xml version=\"1.0\"?>" +
OPEN_TAG +
"<r:license id=\"lic1\"> some license </r:license> " +
"<r:tool> <r:uses-license ref=\"lic2\" /> <r:revision>1</r:revision> " +
"<r:archives> <r:archive os=\"any\"> <r:size>1</r:size> <r:checksum>2822ae37115ebf13412bbef91339ee0d9454525e</r:checksum> " +
"<r:url>url</r:url> </r:archive> </r:archives> </r:tool>" +
CLOSE_TAG;

//<End of snippet n. 3>
