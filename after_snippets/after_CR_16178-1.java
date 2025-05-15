
//<Beginning of snippet n. 0>



import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.repository.SdkRepository;
import com.android.sdkmanager.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
mLabel = new Label(parent, SWT.NONE);
mLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
mLabel.setText(String.format(
                "Android SDK Updater.\n" +
                "Revision %1$s\n" +
                "Repository XML Schema #%2$d\n" +
                "Copyright (C) 2009-2010 The Android Open Source Project.",
                getRevision(),
                SdkRepository.NS_LATEST_VERSION));
}

@Override

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLKeyException;
ByteArrayInputStream xml = fetchUrl(url, exception);
Document validatedDoc = null;
boolean usingAlternateXml = false;
        boolean usingAlternateUrl = false;
String validatedUri = null;

        // If the original URL can't be fetched and the URL doesn't explicitly end with
// our filename, make another tentative after changing the URL.
        if (xml == null && !url.endsWith(SdkRepository.URL_DEFAULT_XML_FILE)) {
if (!url.endsWith("/")) {       //$NON-NLS-1$
url += "/";                 //$NON-NLS-1$
}
url += SdkRepository.URL_DEFAULT_XML_FILE;

xml = fetchUrl(url, exception);
            usingAlternateUrl = true;
        }

        if (xml != null) {
            monitor.setDescription("Validate XML");

            for (int tryOtherUrl = 0; tryOtherUrl < 2; tryOtherUrl++) {
                // Explore the XML to find the potential XML schema version
                int version = getXmlSchemaVersion(xml);

                if (version >= 1 && version <= SdkRepository.NS_LATEST_VERSION) {
                    // This should be a version we can handle. Try to validate it
                    // and report any error as invalid XML syntax,

                    String uri = validateXml(xml, url, version, validationError, validatorFound);
                    if (uri != null) {
                        // Validation was successful
                        validatedDoc = getDocument(xml, monitor);
                        validatedUri = uri;

                        if (usingAlternateUrl && validatedDoc != null) {
                            // If the second tentative succeeded, indicate it in the console
                            // with the URL that worked.
                            monitor.setResult("Repository found at %1$s", url);

                            // Keep the modified URL
                            mUrl = url;
                        }
                    } else if (validatorFound[0].equals(Boolean.FALSE)) {
                        // Validation failed because this JVM lacks a proper XML Validator
                        mFetchError = validationError[0];
                    } else {
                        // We got a validator but validation failed. We know there's
                        // what looks like a suitable root element with a suitable XMLNS
                        // so it must be a genuine error of an XML not conforming to the schema.
                    }
                } else if (version > SdkRepository.NS_LATEST_VERSION) {
                    // The schema used is more recent than what is supported by this tool.
                    // Tell the user to upgrade, pointing him to the right version of the tool
                    // package.

validatedDoc = findAlternateToolsXml(xml);
if (validatedDoc != null) {
validationError[0] = null;  // remove error from XML validation
validatedUri = SdkRepository.NS_SDK_REPOSITORY;
usingAlternateXml = true;
}

                } else if (version < 1 && tryOtherUrl == 0 && !usingAlternateUrl) {
                    // This is obviously not one of our documents.
                    mFetchError = String.format(
                            "Failed to find an XML for the repository at URL '%1$s'",
                            url);

                    // If we haven't already tried the alternate URL, let's do it now.
                    // We don't capture any fetch exception that happen during the second
                    // fetch in order to avoid hidding any previous fetch errors.
                    if (!url.endsWith(SdkRepository.URL_DEFAULT_XML_FILE)) {
                        if (!url.endsWith("/")) {       //$NON-NLS-1$
                            url += "/";                 //$NON-NLS-1$
                        }
                        url += SdkRepository.URL_DEFAULT_XML_FILE;

                        xml = fetchUrl(url, null /*outException*/);

                        // Loop to try the alternative document
                        if (xml != null) {
                            usingAlternateUrl = true;
                            continue;
                        }
                    }
                } else if (version < 1 && usingAlternateUrl && mFetchError == null) {
                    // The alternate URL is obviously not a valid XML either.
                    // We only report the error if we failed to produce one earlier.
                    mFetchError = String.format(
                            "Failed to find an XML for the repository at URL '%1$s'",
                            url);
}

                // If we get here either we succeeded or we ran out of alternatives.
                break;
}
}

monitor.setResult("Failed to fetch URL %1$s, reason: %2$s", url, reason);
}

        if (validationError[0] != null) {
monitor.setResult("%s", validationError[0]);  //$NON-NLS-1$
}

}

if (usingAlternateXml) {
            // We found something using the "alternate" XML schema (that is the one made up
            // to support schema upgrades). That means the user can only install the tools
            // and needs to upgrade them before it download more stuff.

// Is the manager running from inside ADT?
// We check that com.android.ide.eclipse.adt.AdtPlugin exists using reflection.
* Fetches the document at the given URL and returns it as a string.
* Returns null if anything wrong happens and write errors to the monitor.
*
     * References: <br/>
     * Java URL Connection: http://java.sun.com/docs/books/tutorial/networking/urls/readingWriting.html <br/>
     * Java URL Reader: http://java.sun.com/docs/books/tutorial/networking/urls/readingURL.html <br/>
     * Java set Proxy: http://java.sun.com/docs/books/tutorial/networking/urls/_setProxy.html <br/>
     *
     * @param urlString The URL to load, as a string.
     * @param outException If non null, where to store any exception that happens during the fetch.
*/
private ByteArrayInputStream fetchUrl(String urlString, Exception[] outException) {
URL url;
}

} catch (Exception e) {
            if (outException != null) {
                outException[0] = e;
            }
}

return null;
}

/**
     * Validates this XML against one of the requested SDK Repository schemas.
* If the XML was correctly validated, returns the schema that worked.
     * If it doesn't validate, returns null and store the error in outError[0].
     * If we can't find a validator, returns null and set validatorFound[0] to false.
*/
    private String validateXml(InputStream xml, String url, int version,
String[] outError, Boolean[] validatorFound) {

        if (xml == null) {
            return null;
        }

        try {
            Validator validator = getValidator(version);

            if (validator == null) {
                validatorFound[0] = Boolean.FALSE;
                outError[0] = String.format(
                        "XML verification failed for %1$s.\nNo suitable XML Schema Validator could be found in your Java environment. Please consider updating your version of Java.",
                        url);
                return null;
            }

            validatorFound[0] = Boolean.TRUE;

            // Reset the stream if it supports that operation.
            // At runtime we use a ByteArrayInputStream which can be reset; however for unit tests
            // we use a FileInputStream that doesn't support resetting and is read-once.
try {
xml.reset();
            } catch (IOException e1) {
                // ignore if not supported
            }

            // Validation throws a bunch of possible Exceptions on failure.
            validator.validate(new StreamSource(xml));
            return SdkRepository.getSchemaUri(version);

        } catch (SAXParseException e) {
            outError[0] = String.format(
                    "XML verification failed for %1$s.\nLine %2$d:%3$d, Error: %4$s",
                    url,
                    e.getLineNumber(),
                    e.getColumnNumber(),
                    e.toString());

        } catch (Exception e) {
            outError[0] = String.format(
                    "XML verification failed for %1$s.\nError: %4$s",
                    url,
                    e.toString());
        }
        return null;
    }

    /**
     * Manually parses the root element of the XML to extract the schema version
     * at the end of the xmlns:sdk="http://schemas.android.com/sdk/android/repository/$N"
     * declaration.
     *
     * @return 1..{@link SdkRepository#NS_LATEST_VERSION} for a valid schema version
     *         or 0 if no schema could be found.
     */
    private int getXmlSchemaVersion(InputStream xml) {
        if (xml == null) {
            return 0;
        }

        // Reset the stream if it supports that operation.
        // At runtime we use a ByteArrayInputStream which can be reset; however for unit tests
        // we use a FileInputStream that doesn't support resetting and is read-once.
        try {
            xml.reset();
        } catch (IOException e1) {
            // ignore if not supported
        }

        // Get an XML document
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(false);
            factory.setValidating(false);

            // Parse the old document using a non namespace aware builder
            factory.setNamespaceAware(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(xml);

            // Prepare a new document using a namespace aware builder
            factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();

        } catch (Exception e) {
            // Failed to get builder factor
            // Failed to create XML document builder
            // Failed to parse XML document
            // Failed to read XML document
        }

        if (doc == null) {
            return 0;
        }

        // Check the root element is an XML with at least the following properties:
        // <sdk:sdk-repository
        //    xmlns:sdk="http://schemas.android.com/sdk/android/repository/$N">
        //
        // Note that we don't have namespace support enabled, we just do it manually.

        Pattern nsPattern = Pattern.compile(SdkRepository.NS_SDK_REPOSITORY_PATTERN);

        String prefix = null;
        for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                prefix = null;
                String name = child.getNodeName();
                int pos = name.indexOf(':');
                if (pos > 0 && pos < name.length() - 1) {
                    prefix = name.substring(0, pos);
                    name = name.substring(pos + 1);
                }
                if (SdkRepository.NODE_SDK_REPOSITORY.equals(name)) {
                    NamedNodeMap attrs = child.getAttributes();
                    String xmlns = "xmlns";                                         //$NON-NLS-1$
                    if (prefix != null) {
                        xmlns += ":" + prefix;                                      //$NON-NLS-1$
                    }
                    Node attr = attrs.getNamedItem(xmlns);
                    if (attr != null) {
                        String uri = attr.getNodeValue();
                        if (uri != null) {
                            Matcher m = nsPattern.matcher(uri);
                            if (m.matches()) {
                                String version = m.group(1);
                                try {
                                    return Integer.parseInt(version);
                                } catch (NumberFormatException e) {
                                    return 0;
                                }
                            }
                        }
                    }
}
}
}

        return 0;
}

/**
}


        // Check the root element is an XML with at least the following properties:
// <sdk:sdk-repository
//    xmlns:sdk="http://schemas.android.com/sdk/android/repository/$N">
//

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


private static final String NS_SDK_REPOSITORY_BASE =
"http://schemas.android.com/sdk/android/repository/";                   //$NON-NLS-1$

    /** The pattern of our XML namespace. Matcher's group(1) is the schema version (integer). */
public static final String NS_SDK_REPOSITORY_PATTERN =
        NS_SDK_REPOSITORY_BASE + "([1-9][0-9]*)";        //$NON-NLS-1$

/** The latest version of the sdk-repository XML Schema.
*  Valid version numbers are between 1 and this number, included. */
    public static final int NS_LATEST_VERSION = 3;

/** The XML namespace of the latest sdk-repository XML. */
public static final String NS_SDK_REPOSITORY = getSchemaUri(NS_LATEST_VERSION);

//<End of snippet n. 2>










//<Beginning of snippet n. 3>


fail();
}

    /** A document with an unknown license id. */
public void testLicenseIdNotFound() throws Exception {
// we define a license named "lic1" and then reference "lic2" instead
String document = "<?xml version=\"1.0\"?>" +
OPEN_TAG +
"<r:license id=\"lic1\"> some license </r:license> " +
"<r:tool> <r:uses-license ref=\"lic2\" /> <r:revision>1</r:revision> " +
            "<r:min-platform-tools-rev>1</r:min-platform-tools-rev> " +
"<r:archives> <r:archive os=\"any\"> <r:size>1</r:size> <r:checksum>2822ae37115ebf13412bbef91339ee0d9454525e</r:checksum> " +
"<r:url>url</r:url> </r:archive> </r:archives> </r:tool>" +
CLOSE_TAG;

//<End of snippet n. 3>








