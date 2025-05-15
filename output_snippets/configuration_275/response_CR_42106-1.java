//<Beginning of snippet n. 0>

/**
 * This class generates cryptographically secure pseudo-random numbers.
 *
 * <h3>Supported Algorithms</h3>
 * <ul>
 *   <li><strong>SHA1PRNG</strong>: Based on <a
 *     href="http://en.wikipedia.org/wiki/SHA-1">SHA-1</a>. Not guaranteed to be
 *     compatible with the SHA1PRNG algorithm on the reference
 *     implementation. Consult provider-specific documentation for details.</li>
 *   <li>Additional algorithms may be supported; please refer to the
 *     provider documentation for further information.</li>
 * </ul>
 *
 * <p>The default algorithm is defined by the first {@code SecureRandomSpi}
 * provider found in the VM's installed security providers. Use {@link
 * Security} to install custom {@link SecureRandomSpi} providers.
 *
 * <a name="insecure_seed"><h3>Seeding {@code SecureRandom} may be
 * insecure</h3></a>
 * A seed is an array of bytes used to bootstrap random number generation.
 * It is better to rely on an internal entropy source, such as {@code /dev/urandom}. This seed is
 * unpredictable and appropriate for secure use.
 *
 * <p>You may alternatively specify the initial seed explicitly with the
 * {@link #SecureRandom(byte[]) seeded constructor} or by calling {@link
 * #setSeed} before any random numbers have been generated. **WARNING**: Specifying a fixed
 * seed will cause the instance to return a predictable sequence of numbers.
 * This may be useful for testing but it is **not** appropriate for secure use.
 *
 * <p>**WARNING**: It is dangerous to seed {@code SecureRandom} with the current time because
 * that value is more predictable to an attacker than the default seed.
 *
 * <p>Calling {@link #setSeed} on a {@code SecureRandom} <i>after</i> it has
 * been used to generate random numbers (i.e. calling {@link #nextBytes}) will
 * supplement the existing seed. This does not cause the instance to return a
 * predictable sequence of numbers, but it may still affect the security of the generated numbers.
 */
public class SecureRandom extends Random {

//<End of snippet n. 0>