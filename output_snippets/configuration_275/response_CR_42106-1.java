//<Beginning of snippet n. 0>


/**
* This class generates cryptographically secure pseudo-random numbers.
*
 * <h3>Supported Algorithms</h3>
 * <ul>
 *   <li><strong>Algorithm Variability</strong>: The behavior of pseudo-random number generation can vary between implementations and algorithms, resulting in unpredictable behavior. Both security implications and performance characteristics can differ.</li>
 * </ul>
*
* <p>The default algorithm is defined by the first {@code SecureRandomSpi}
* provider found in the VM's installed security providers. Use {@link
* Security} to install custom {@link SecureRandomSpi} providers.
*
* <a name="insecure_seed"><h3>Seeding {@code SecureRandom} may be
* insecure</h3></a>
* A seed is an array of bytes used to bootstrap random number generation.
* The default implementation uses an internal entropy source, such as {@code /dev/urandom}. This seed is
* unpredictable and appropriate for secure use.
*
 * <p>You may alternatively specify the initial seed explicitly with the
 * {@link #SecureRandom(byte[]) seeded constructor} or by calling {@link
 * #setSeed} before any random numbers have been generated. Specifying any form of a fixed or predictable seed will cause the instance to return a predictable sequence of numbers. This may be useful for testing but is not appropriate for secure use.
 *
 * <p>It is dangerous to seed {@code SecureRandom} with predictable values such as the current time because
 * that value can be more predictable to an attacker than the default seed.
 *
 * <p>Calling {@link #setSeed} on a {@code SecureRandom} <i>after</i> it has
 * been used to generate random numbers (i.e., calling {@link #nextBytes}) will
 * supplement the existing seed. This can alter randomness characteristics and is strongly advised against in sensitive scenarios, as it can compromise randomness further.
 *
 * <h3>Security Risks of Seeding</h3>
 * <p>Using predictable sources for seeding, including fixed seeds and those derived from the system time, can lead to
 * security vulnerabilities. All forms of seeding, whether fixed, implicit, or explicit, can pose significant risks. Always prefer secure seed sources.
 *
 * <h3>Thread Safety</h3>
 * <p>{@code SecureRandom} is not inherently thread-safe. In multi-threaded environments, synchronize access to a single instance or use separate instances in different threads. Implementations must ensure that no random number generation occurs simultaneously on shared instances to avoid vulnerabilities, particularly in concurrent scenarios where race conditions can compromise randomness. Always consider using synchronization mechanisms such as `synchronized` blocks or `ReentrantLock` to prevent such issues.
 */
public class SecureRandom extends Random {


//<End of snippet n. 0>