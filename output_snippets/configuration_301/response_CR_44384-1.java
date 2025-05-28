//<Beginning of snippet n. 0>
if (da != null && dea != null) {
    String alg = da + "with" + dea;
    Signature sig = null;
    try {
        sig = Signature.getInstance(alg.replace(OpenSSLProvider.PROVIDER_NAME, ""));
    } catch (NoSuchAlgorithmException e) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No such algorithm: " + alg, e);
    }
    
    if (sig == null) {
        throw new IllegalStateException("Signature creation failed for algorithm: " + alg + ". Check if the algorithm is valid.");
    }
}
//<End of snippet n. 0>