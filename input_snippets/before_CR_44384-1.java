
//<Beginning of snippet n. 0>


if (da != null && dea != null) {
alg = da + "with" +  dea;
try {
                sig = Signature.getInstance(alg, OpenSSLProvider.PROVIDER_NAME);
} catch (NoSuchAlgorithmException e) {}
}
if (sig == null) {
return null;
}
try {
                sig = Signature.getInstance(alg, OpenSSLProvider.PROVIDER_NAME);
} catch (NoSuchAlgorithmException e) {
return null;
}

//<End of snippet n. 0>








