/*SdkManager: support for upgrade to Schema #3.

This changes the way the sdk manager looks for schema upgrades:
- when trying to load an XML, parse the root element and scan
  for our XLMNS. If it parses, get the version number at the end
  and use this directly to try to load using the right schema.
- In turn this allows us to distinguish between an incorrectly
  formatted XML (but using the right schema) versus something
  that is not our XML schema or not an XML at all, and then we
  can produce better errors for the user.
- We also then directly know when the schema version is above
  the known value of the tool and can then create the upgrade
  tools node accordingly.

Change-Id:I58c7d89d7b57dd4d5488f726e5f8b21cc4308451*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java
//Synthetic comment -- index 5d3e65a..fa05b29 100755

//Synthetic comment -- @@ -19,6 +19,7 @@

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.Package;
import com.android.sdkmanager.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
//Synthetic comment -- @@ -64,8 +65,12 @@
mLabel = new Label(parent, SWT.NONE);
mLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
mLabel.setText(String.format(
                "Android SDK Updater.\nRevision %1$s\nCopyright (C) 2009 The Android Open Source Project.",
                getRevision()));
}

@Override








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/RepoSource.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/RepoSource.java
//Synthetic comment -- index a7bc3f3..432db48 100755

//Synthetic comment -- @@ -26,6 +26,7 @@
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
//Synthetic comment -- @@ -35,6 +36,7 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.net.ssl.SSLKeyException;
//Synthetic comment -- @@ -166,67 +168,99 @@
ByteArrayInputStream xml = fetchUrl(url, exception);
Document validatedDoc = null;
boolean usingAlternateXml = false;
String validatedUri = null;
        if (xml != null) {
            monitor.setDescription("Validate XML");
            String uri = validateXml(xml, url, validationError, validatorFound);
            if (uri != null) {
                // Validation was successful
                validatedDoc = getDocument(xml, monitor);
                validatedUri = uri;
            } else if (validatorFound[0].equals(Boolean.TRUE)) {
                // An XML validator was found and the XML failed to validate.
                // Let's see if we can find an alternate tools upgrade to perform.
                validatedDoc = findAlternateToolsXml(xml);
                if (validatedDoc != null) {
                    validationError[0] = null;  // remove error from XML validation
                    validatedUri = SdkRepository.NS_SDK_REPOSITORY;
                    usingAlternateXml = true;
                }
            } else {
                // Validation failed because this JVM lacks a proper XML Validator
                mFetchError = "No suitable XML Schema Validator could be found in your Java environment. Please update your version of Java.";
            }
        }

        // If we failed the first time and the URL doesn't explicitly end with
// our filename, make another tentative after changing the URL.
        if (validatedDoc == null && !url.endsWith(SdkRepository.URL_DEFAULT_XML_FILE)) {
if (!url.endsWith("/")) {       //$NON-NLS-1$
url += "/";                 //$NON-NLS-1$
}
url += SdkRepository.URL_DEFAULT_XML_FILE;

xml = fetchUrl(url, exception);
            if (xml != null) {
                String uri = validateXml(xml, url, validationError, validatorFound);
                if (uri != null) {
                    // Validation was successful
                    validationError[0] = null;  // remove error from previous XML validation
                    validatedDoc = getDocument(xml, monitor);
                    validatedUri = uri;
                } else if (validatorFound[0].equals(Boolean.TRUE)) {
                    // An XML validator was found and the XML failed to validate.
                    // Let's see if we can find an alternate tools upgrade to perform.
validatedDoc = findAlternateToolsXml(xml);
if (validatedDoc != null) {
validationError[0] = null;  // remove error from XML validation
validatedUri = SdkRepository.NS_SDK_REPOSITORY;
usingAlternateXml = true;
}
                } else {
                    // Validation failed because this JVM lacks a proper XML Validator
                    mFetchError = "No suitable XML Schema Validator could be found in your Java environment. Please update your version of Java.";
}
            }

            if (validatedDoc != null) {
                // If the second tentative succeeded, indicate it in the console
                // with the URL that worked.
                monitor.setResult("Repository found at %1$s", url);

                // Keep the modified URL
                mUrl = url;
}
}

//Synthetic comment -- @@ -253,7 +287,7 @@
monitor.setResult("Failed to fetch URL %1$s, reason: %2$s", url, reason);
}

        if(validationError[0] != null) {
monitor.setResult("%s", validationError[0]);  //$NON-NLS-1$
}

//Synthetic comment -- @@ -263,6 +297,9 @@
}

if (usingAlternateXml) {

// Is the manager running from inside ADT?
// We check that com.android.ide.eclipse.adt.AdtPlugin exists using reflection.
//Synthetic comment -- @@ -319,10 +356,13 @@
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
//Synthetic comment -- @@ -361,52 +401,163 @@
}

} catch (Exception e) {
            outException[0] = e;
}

return null;
}

/**
     * Validates this XML against one of the possible SDK Repository schema, starting
     * by the most recent one.
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
                // Validation throws a bunch of possible Exceptions on failure.
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
//Synthetic comment -- @@ -476,7 +627,7 @@
}


        // Check the root element is an xsd-schema with at least the following properties:
// <sdk:sdk-repository
//    xmlns:sdk="http://schemas.android.com/sdk/android/repository/$N">
//








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepository.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepository.java
//Synthetic comment -- index 62f7a25..d29ec6f 100755

//Synthetic comment -- @@ -34,13 +34,13 @@
private static final String NS_SDK_REPOSITORY_BASE =
"http://schemas.android.com/sdk/android/repository/";                   //$NON-NLS-1$

    /** The pattern of our XML namespace. */
public static final String NS_SDK_REPOSITORY_PATTERN =
        NS_SDK_REPOSITORY_BASE + "[1-9][0-9]*";        //$NON-NLS-1$

/** The latest version of the sdk-repository XML Schema.
*  Valid version numbers are between 1 and this number, included. */
    public static final int NS_LATEST_VERSION = 2;

/** The XML namespace of the latest sdk-repository XML. */
public static final String NS_SDK_REPOSITORY = getSchemaUri(NS_LATEST_VERSION);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/com/android/sdklib/repository/SdkRepositoryTest.java b/sdkmanager/libs/sdklib/tests/com/android/sdklib/repository/SdkRepositoryTest.java
//Synthetic comment -- index bd8db05..14b0b60 100755

//Synthetic comment -- @@ -279,13 +279,14 @@
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







