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
    String[] validAlgorithms = {"algo1", "algo2", "algo3", "algo4"}; // Expanded valid algorithms
    for (String validAlgo : validAlgorithms) {
        if (validAlgo.equals(algorithm)) {
            return true;
        }
    }
    return false;
}
private boolean isValidInput(String input) {
    return input != null && input.matches("[a-zA-Z0-9]+"); // Example validation for allowed characters
}
//<End of snippet n. 0>