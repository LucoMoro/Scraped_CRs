/*Add getSubjectAlternativeNames() to certificate JCE

Change-Id:I4a486878447111fa53d0d78ae9c2bf9365e154ed*/




//Synthetic comment -- diff --git a/bcprov/src/main/java/org/bouncycastle/asn1/DERT61String.java b/bcprov/src/main/java/org/bouncycastle/asn1/DERT61String.java
//Synthetic comment -- index ee2979b..f023e7f 100644

//Synthetic comment -- @@ -70,12 +70,16 @@
public DERT61String(
String   string)
{
        // BEGIN android-changed
        this.string = Strings.toUTF8ByteArray(string);
        // END android-changed
}

public String getString()
{
        // BEGIN android-changed
        return Strings.fromUTF8ByteArray(string);
        // END android-changed
}

public String toString()








//Synthetic comment -- diff --git a/bcprov/src/main/java/org/bouncycastle/jce/provider/X509CertificateObject.java b/bcprov/src/main/java/org/bouncycastle/jce/provider/X509CertificateObject.java
//Synthetic comment -- index e529836..ebd2343 100644

//Synthetic comment -- @@ -20,6 +20,9 @@
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
// BEGIN android-added
import java.util.Collection;
// END android-added
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
//Synthetic comment -- @@ -57,6 +60,9 @@
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
// BEGIN android-added
import org.bouncycastle.x509.extension.X509ExtensionUtil;
// END android-added

public class X509CertificateObject
extends X509Certificate
//Synthetic comment -- @@ -823,4 +829,10 @@

return id1.getParameters().equals(id2.getParameters());
}
    // BEGIN android-added
    public Collection<List<?>> getSubjectAlternativeNames() throws CertificateParsingException
    {
        return X509ExtensionUtil.getSubjectAlternativeNames(this);
    }
    // END android-added
}








//Synthetic comment -- diff --git a/bcprov/src/main/java/org/bouncycastle/x509/extension/X509ExtensionUtil.java b/bcprov/src/main/java/org/bouncycastle/x509/extension/X509ExtensionUtil.java
//Synthetic comment -- index 048f31b..6f2f0ee 100644

//Synthetic comment -- @@ -1,6 +1,9 @@
package org.bouncycastle.x509.extension;

import java.io.IOException;
// BEGIN android-added
import java.net.InetAddress;
// END android-added
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
//Synthetic comment -- @@ -18,6 +21,9 @@
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.X509Extension;
// BEGIN android-added
import org.bouncycastle.asn1.x509.X509Name;
// END android-added


public class X509ExtensionUtil
//Synthetic comment -- @@ -70,10 +76,15 @@
case GeneralName.ediPartyName:
case GeneralName.x400Address:
case GeneralName.otherName:
                    // BEGIN android-changed
                    list.add(genName.getEncoded());
                    // END android-changed
break;
case GeneralName.directoryName:
                    // BEGIN android-changed
                    list.add(X509Name.getInstance(genName.getName()).toString(true,
                            X509Name.DefaultSymbols));
                    // END android-changed
break;
case GeneralName.dNSName:
case GeneralName.rfc822Name:
//Synthetic comment -- @@ -84,7 +95,11 @@
list.add(ASN1ObjectIdentifier.getInstance(genName.getName()).getId());
break;
case GeneralName.iPAddress:
                    // BEGIN android-changed
                    byte[] addrBytes = DEROctetString.getInstance(genName.getName()).getOctets();
                    InetAddress addr = InetAddress.getByAddress(addrBytes);
                    list.add(addr.getHostAddress());
                    // END android-changed
break;
default:
throw new IOException("Bad tag number: " + genName.getTagNo());







