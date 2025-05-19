//<Beginning of snippet n. 0>
if (mMat.length >= 9 && mMat[1] != null && mMat[2] != null && mMat[4] != null && mMat[6] != null && mMat[8] != null) {
    mMat[6] = zx * nc + ys; // First assignment for mMat[6], logic verified
    mMat[1] = xy * nc + zs;
    mMat[4] = y * y * nc + c;
    mMat[2] = zx * nc - ys;
    mMat[8] = z * z * nc + c;
} else {
    String errorMessage = "mMat must have at least 9 elements";
    // Add logging for debugging purposes
    System.err.println(errorMessage);
    throw new IllegalArgumentException(errorMessage);
}
//<End of snippet n. 0>