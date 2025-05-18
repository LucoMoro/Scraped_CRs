//<Beginning of snippet n. 0>

/**
* This class generates cryptographically secure pseudo-random numbers.
*
* <h3>Supported Algorithms</h3>
* <ul>
*   <li><strong>Variable Algorithms</strong>: Algorithm behavior is not uniform across implementations. 
*       Always check specific implementations for compatibility and performance details.</li>
* </ul>
*
* <p>The default algorithm is defined by the first {@code SecureRandomSpi}
* provider found in the VM's installed security providers. Use {@link
* Security} to install custom {@link SecureRandomSpi} providers.
*
* <a name="insecure_seed"><h3>Seeding {@code SecureRandom} may be
* insecure</h3></a>
* A seed is an array of bytes used to bootstrap random number generation.
* The use of a secure entropy source, such as {@code /dev/urandom}, is 
* recommended. This seed should be unpredictable and appropriate for secure use.
*
* <p>Specifying any form of seed, whether explicit with the
* {@link #SecureRandom(byte[]) seeded constructor} or by calling {@link
* #setSeed}, can lead to vulnerabilities and predictable sequences of random numbers, which poses risks to security.
*
* <p>It is recommended to avoid seeding {@code SecureRandom} with the current time 
* as this value is more predictable to an attacker compared to using a secure entropy source.
*
* <p>Calling {@link #setSeed} on a {@code SecureRandom} <i>after</i> it has
* been used to generate random numbers (i.e., calling {@link #nextBytes}) will
* supplement the existing seed. This can compromise security; avoid fixed or predictable outputs.
*
* <p>Always prioritize avoiding fixed or predictable outputs when utilizing 
* {@code SecureRandom} to maintain high security standards.
*/
public class SecureRandom extends Random {

//<End of snippet n. 0>