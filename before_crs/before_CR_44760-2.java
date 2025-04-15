/*SubjectPublicKeyInfo: use algorithm OID as fallback

If the algorithm has a name, but there are no KeyFactory available for
that particular algorithm name, try to use the algorithm OID to find a
KeyFactory before falling back to X509PublicKey.

Change-Id:I2b294f2db3388372479c964f53fdff7fb62f3d8f*/
//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/AlgorithmIdentifier.java b/luni/src/main/java/org/apache/harmony/security/x509/AlgorithmIdentifier.java
//Synthetic comment -- index 1ab12e6..185151c 100644

//Synthetic comment -- @@ -72,6 +72,14 @@
}

/**
* Returns the value of algorithm field of the structure.
*/
public String getAlgorithm() {








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/x509/SubjectPublicKeyInfo.java b/luni/src/main/java/org/apache/harmony/security/x509/SubjectPublicKeyInfo.java
//Synthetic comment -- index 545d489..aef7a65 100644

//Synthetic comment -- @@ -26,13 +26,13 @@
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.apache.harmony.security.asn1.ASN1BitString;
import org.apache.harmony.security.asn1.ASN1Sequence;
import org.apache.harmony.security.asn1.ASN1Type;
import org.apache.harmony.security.asn1.BerInputStream;
import org.apache.harmony.security.asn1.BitString;
import org.apache.harmony.security.utils.AlgNameMapper;

/**
* The class encapsulates the ASN.1 DER encoding/decoding work
//Synthetic comment -- @@ -103,32 +103,54 @@
}

/**
     * Returns The PublicKey corresponding to this SubjectPublicKeyInfo
* instance.
*/
public PublicKey getPublicKey() {
if (publicKey == null) {
            String alg_oid = algorithmID.getAlgorithm();
            try {
                String alg =
                    AlgNameMapper.map2AlgName(alg_oid);

                if (alg == null) {
                    alg = alg_oid;
                }
                publicKey = KeyFactory.getInstance(alg)
                    .generatePublic(new X509EncodedKeySpec(getEncoded()));
            } catch (InvalidKeySpecException ignored) {
            } catch (NoSuchAlgorithmException ignored) {
}
if (publicKey == null) {
                publicKey = new X509PublicKey(alg_oid, getEncoded(),
                        subjectPublicKey);
}
}
return publicKey;
}

public static final ASN1Sequence ASN1 = new ASN1Sequence(new ASN1Type[] {
AlgorithmIdentifier.ASN1, ASN1BitString.getInstance() }) {
@Override protected Object getDecodedObject(BerInputStream in) {








//Synthetic comment -- diff --git a/luni/src/test/java/org/apache/harmony/security/tests/x509/SubjectPublicKeyInfoTest.java b/luni/src/test/java/org/apache/harmony/security/tests/x509/SubjectPublicKeyInfoTest.java
new file mode 100644
//Synthetic comment -- index 0000000..35dbbda

//Synthetic comment -- @@ -0,0 +1,162 @@







