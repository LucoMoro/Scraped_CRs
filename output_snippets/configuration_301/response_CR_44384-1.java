//<Beginning of snippet n. 0>
if (da != null && dea != null) {
    alg = da + "with" +  dea;
    try {
        sig = Signature.getInstance(alg);
    } catch (NoSuchAlgorithmException e) {
        // Handle exception, for example, log the error
    }
}
if (sig == null) {
    return null;
}
try {
    sig = Signature.getInstance(alg);
} catch (NoSuchAlgorithmException e) {
    return null;
}
//<End of snippet n. 0>