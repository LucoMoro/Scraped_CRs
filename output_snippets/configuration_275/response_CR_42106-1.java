//<Beginning of snippet n. 0>

/**
* This class generates cryptographically secure pseudo-random numbers.
*
 * <h3>Supported Algorithms</h3>
 * <ul>
 *   <li><strong>SHA1PRNG</strong>: Based on <a
 *     href="http://en.wikipedia.org/wiki/SHA-1">SHA-1</a>. Not guaranteed to be
 *     compatible with the SHA1PRNG algorithm on every provider implementation.
 *     The effects of {@link #setSeed} are not universally deterministic and 
 *     may vary by provider.</li>
 * </ul>
*
* <p>The default algorithm is defined by the first {@code SecureRandomSpi}
* provider found in the VM's installed security providers. Use {@link
* Security} to install custom {@link SecureRandomSpi} providers.
*
* <a name="insecure_seed"><h3>Seeding {@code SecureRandom} may be
* insecure</h3></a>
* A seed is an array of bytes used to bootstrap random number generation.
* The preferred method is to rely on an internal entropy source, such as 
* {@code /dev/urandom}. This seed is unpredictable and appropriate for secure use.
*
 * <p>You may alternatively specify the initial seed explicitly with the
 * {@link #SecureRandom(byte[]) seeded constructor} or by calling {@link
 * #setSeed} before any random numbers have been generated. Specifying a fixed
 * seed will cause the instance to return a predictable sequence of numbers.
 * This may be useful for testing but it is not appropriate for secure use.
 *
 * <p>It is dangerous to seed {@code SecureRandom} with easily guessable values such
 * as user inputs, fixed values, or patterns, including the current time, as these 
 * can be more predictable to an attacker than the default seed. 
 *
 * <p>Calling {@link #setSeed} on a {@code SecureRandom} <i>after</i> it has
 * been used to generate random numbers (i.e., calling {@link #nextBytes}) will
 * supplement the existing seed. This does not guarantee a predictable sequence of
 * numbers nor does it harm the security of the numbers generated, but caution 
 * should be exercised when seeding this way due to potential predictability risks.
*/
public class SecureRandom extends Random {

//<End of snippet n. 0>