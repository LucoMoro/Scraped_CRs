//<Beginning of snippet n. 0>
if (da != null && dea != null) {
    String alg = da + "with" + dea;
    Signature sig;
    try {
        sig = Signature.getInstance(alg);
    } catch (NoSuchAlgorithmException e) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No such algorithm: " + alg, e);
        throw new IllegalArgumentException("Signature creation failed for algorithm: " + alg + ". Check if the algorithm is valid.", e);
    }
} else {
    throw new IllegalArgumentException("Input parameters 'da' and 'dea' must not be null.");
}
//<End of snippet n. 0>