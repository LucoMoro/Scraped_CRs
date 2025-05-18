//<Beginning of snippet n. 0>
if (mMat.length >= 9) {
    mMat[6] = yz * nc + xs;
    mMat[1] = xy * nc + zs;
    mMat[4] = y * y * nc + c;
    mMat[7] = yz * nc - xs; 
    mMat[2] = zx * nc - ys;
    mMat[8] = z * z * nc + c;
} else {
    throw new IllegalArgumentException("Array size must be at least 9 for a 3x3 matrix.");
}
//<End of snippet n. 0>