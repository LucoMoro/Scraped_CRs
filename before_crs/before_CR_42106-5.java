/*Update docs on SecureRandom about determinism

The docs for SecureRandom mentioned very specific operational semantics
of the behavior of one provider. This makes it more general while
maintaining the cautioning tone givent to developers about the setSeed
semantics.

Change-Id:I234478dfe05a20357f16a2ae7eb9535889101832*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/security/SecureRandom.java b/luni/src/main/java/java/security/SecureRandom.java
//Synthetic comment -- index 9091f73..4ab7bdb 100644

//Synthetic comment -- @@ -28,20 +28,26 @@
/**
* This class generates cryptographically secure pseudo-random numbers.
*
 * <h3>Supported Algorithms</h3>
 * <ul>
 *   <li><strong>SHA1PRNG</strong>: Based on <a
 *     href="http://en.wikipedia.org/wiki/SHA-1">SHA-1</a>. Not guaranteed to be
 *     compatible with the SHA1PRNG algorithm on the reference
 *     implementation.</li>
 * </ul>
*
* <p>The default algorithm is defined by the first {@code SecureRandomSpi}
 * provider found in the VM's installed security providers. Use {@link
 * Security} to install custom {@link SecureRandomSpi} providers.
*
 * <a name="insecure_seed"><h3>Seeding {@code SecureRandom} may be
 * insecure</h3></a>
* A seed is an array of bytes used to bootstrap random number generation.
* To produce cryptographically secure random numbers, both the seed and the
* algorithm must be secure.
//Synthetic comment -- @@ -50,19 +56,11 @@
* an internal entropy source, such as {@code /dev/urandom}. This seed is
* unpredictable and appropriate for secure use.
*
 * <p>You may alternatively specify the initial seed explicitly with the
 * {@link #SecureRandom(byte[]) seeded constructor} or by calling {@link
 * #setSeed} before any random numbers have been generated. Specifying a fixed
 * seed will cause the instance to return a predictable sequence of numbers.
 * This may be useful for testing but it is not appropriate for secure use.
 *
 * <p>It is dangerous to seed {@code SecureRandom} with the current time because
 * that value is more predictable to an attacker than the default seed.
 *
 * <p>Calling {@link #setSeed} on a {@code SecureRandom} <i>after</i> it has
 * been used to generate random numbers (ie. calling {@link #nextBytes}) will
 * supplement the existing seed. This does not cause the instance to return a
 * predictable numbers, nor does it harm the security of the numbers generated.
*/
public class SecureRandom extends Random {








