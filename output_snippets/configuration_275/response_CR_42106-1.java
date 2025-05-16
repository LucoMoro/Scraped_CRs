//<Beginning of snippet n. 0>

/**
* This class generates cryptographically secure pseudo-random numbers.
*
 * <h3>Supported Algorithms</h3>
 * <ul>
 *   <li><strong>Variable Providers</strong>: The security and behavior of the random number generator may vary depending on the implementation of the {@code SecureRandomSpi} provider used.</li>
 * </ul>
*
* <p>The default algorithm is defined by the first {@code SecureRandomSpi}
* provider found in the VM's installed security providers. Use {@link
* Security} to install custom {@link SecureRandomSpi} providers.
*
* <a name="insecure_seed"><h3>Seeding {@code SecureRandom} may be
* insecure</h3></a>
* A seed is an array of bytes used to bootstrap random number generation.
* A secure seed should be obtained from an unpredictable source, such as
* {@code /dev/urandom}. Specifying any predictable seed, including fixed seeds
* or current time, poses significant security risks and should be avoided in
* security-sensitive applications.
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
 * been used to generate random numbers (i.e., calling {@link #nextBytes}) will
 * supplement the existing seed. This can lead to potential vulnerabilities, as
 * altering the seed may result in predictable outputs after number generation.
 * Altering the seed should be carefully considered, as it may compromise the
 * security of the generated numbers.
*/
public class SecureRandom extends Random {

//<End of snippet n. 0>