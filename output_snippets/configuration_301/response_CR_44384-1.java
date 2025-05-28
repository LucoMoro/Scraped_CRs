//<Beginning of snippet n. 0>
if (isValidAlgorithm(da) && isValidAlgorithm(dea)) {
    alg = da + dea;
    try {
        sig = Signature.getInstance(alg);
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalArgumentException("Algorithm not found for: " + alg, e);
    }
}

private boolean isValidAlgorithm(String algorithm) {
    return algorithm != null && (algorithm.equals("algo1") || algorithm.equals("algo2")); // Example valid algorithms
}
//<End of snippet n. 0>