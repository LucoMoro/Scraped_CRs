//<Beginning of snippet n. 0>


/**
 * This class generates cryptographically secure pseudo-random numbers.
 *
 * <h3>Supported Algorithms</h3>
 * <p>This class may support various algorithms, including but not limited to
 * SHA1PRNG. The specific behavior is determined by the implementation of
 * {@code SecureRandomSpi} in use, and deterministic behaviors may arise based
 * on particular implementations.</p>
 *
 * <p>The default algorithm is defined by the first {@code SecureRandomSpi}
 * provider found in the VM's installed security providers. Use {@link
 * Security} to install custom {@link SecureRandomSpi} providers.</p>
 *
 * <a name="insecure_seed"><h3>Seeding {@code SecureRandom} may be
 * insecure</h3></a>
 * <p>A seed is an array of bytes used to bootstrap random number generation
 * and derives from an internal entropy source, such as {@code /dev/urandom}. This
 * seed is unpredictable and appropriate for secure use.</p>
 *
 * <p>You may alternatively specify the initial seed explicitly with the
 * {@link #SecureRandom(byte[]) seeded constructor} or by calling {@link
 * #setSeed} before any random numbers have been generated. Specifying a fixed
 * seed will cause the instance to return a predictable sequence of numbers.
 * This is acceptable for testing but poses significant security risks in
 * production environments.</p>
 *
 * <p>It is dangerous to seed {@code SecureRandom} with the current time or any
 * value that may be predictable to an attacker because it reduces the security
 * of the generated output.</p>
 *
 * <p>Calling {@link #setSeed} on a {@code SecureRandom} <i>after</i> it has
 * been used to generate random numbers (i.e., calling {@link #nextBytes}) will
 * supplement the existing seed. While this does not cause the instance to return
 * a predictable sequence of numbers nor does it harm the security of the
 * previously generated numbers, caution is still advised.</p>
 *
 * <p><strong>Warning:</strong> The deterministic behavior of different
 * implementations can vary, and risks extend beyond just the
 * {@code SHA1PRNG} algorithm. Always consider the implications of using any
 * seeding mechanism in security-sensitive applications.</p>
 *
 * <p><strong>Best Practices:</strong> For security-sensitive applications,
 * avoid using fixed seeds entirely. Use unpredictable entropy sources,
 * preferably provided by the underlying operating system, for such
 * scenarios. Fixed seeds may be appropriate for non-sensitive testing purposes,
 * but assess the context of use thoroughly.</p>
 */
public class SecureRandom extends Random {


//<End of snippet n. 0>