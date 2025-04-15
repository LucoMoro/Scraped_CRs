/*Update docs on SecureRandom about determinism

The docs for SecureRandom mentioned very specific operational semantics
of the behavior of one provider. This makes it more general while
maintaining the cautioning tone givent to developers about the setSeed
semantics.

Change-Id:I234478dfe05a20357f16a2ae7eb9535889101832*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/security/SecureRandom.java b/luni/src/main/java/java/security/SecureRandom.java
//Synthetic comment -- index 9091f73..95620d8 100644

//Synthetic comment -- @@ -28,18 +28,23 @@
/**
* This class generates cryptographically secure pseudo-random numbers.
*
 * It is best to invoke {@code SecureRandom} using the default constructor.
 * This will provide an instance of the most cryptographically strong
 * provider available:
 *
 * <pre>SecureRandom sr = new SecureRandom();
 * byte[] output = new byte[16];
 * sr.nextBytes(output);</pre>
*
* <p>The default algorithm is defined by the first {@code SecureRandomSpi}
* provider found in the VM's installed security providers. Use {@link
* Security} to install custom {@link SecureRandomSpi} providers.
*
 * <p>Note that the output of a {@code SecureRandom} instance should never
 * be relied upon to be deterministic. For deterministic output from a given
 * input, see {@link MessageDigest}. For deriving keys from passwords, see
 * {@link javax.crypto.SecretKeyFactory}.
 *
* <a name="insecure_seed"><h3>Seeding {@code SecureRandom} may be
* insecure</h3></a>
* A seed is an array of bytes used to bootstrap random number generation.
//Synthetic comment -- @@ -50,19 +55,11 @@
* an internal entropy source, such as {@code /dev/urandom}. This seed is
* unpredictable and appropriate for secure use.
*
 * <p>Using the {@link #SecureRandom(byte[]) seeded constructor} or calling
 * {@link #setSeed} may completely replace the cryptographically strong
 * default seed causing the instance to return a predictable sequence of
 * numbers unfit for secure use. Due to variations between implementations
 * it is not recommended to use {@code setSeed} at all.
*/
public class SecureRandom extends Random {








