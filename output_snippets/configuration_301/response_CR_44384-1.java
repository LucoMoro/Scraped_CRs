//<Beginning of snippet n. 0>
String alg;
if (da != null && dea != null) {
    alg = da + "with" + dea;
} else {
    throw new IllegalArgumentException("da and dea must not be null.");
}

try {
    sig = Signature.getInstance(alg);
} catch (NoSuchAlgorithmException e) {
    // Log error message here (assumed logging framework exists)
    System.err.println("Error: " + e.getMessage());
    throw new RuntimeException("Failed to create Signature instance for algorithm: " + alg, e);
}

if (sig == null) {
    throw new RuntimeException("Signature instance could not be created; sig is null.");
}
//<End of snippet n. 0>