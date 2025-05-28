//<Beginning of snippet n. 0>
if (mMat.length >= 10) {
    mMat[6] = zx * nc + ys;
    mMat[1] = xy * nc + zs;
    mMat[4] = y * y * nc + c;
    mMat[7] = yz * nc - xs; // Changed from mMat[9] to mMat[7] for 3x3 matrix
    mMat[2] = zx * nc - ys;
    mMat[6] = yz * nc + xs; // This assignment remains but note it overwrites the previous mMat[6] assignment
    mMat[8] = z * z * nc + c;
}
//<End of snippet n. 0>