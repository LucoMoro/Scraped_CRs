//<Beginning of snippet n. 0>

if (mMat.length > 9) {
    mMat[6] = zx * nc + ys; // First assignment
    mMat[1] = xy * nc + zs;
    mMat[4] = y * y * nc + c;
    mMat[9] = yz * nc - xs;
    mMat[2] = zx * nc - ys;

    // Consolidating the duplicate assignment to mMat[6]
    mMat[6] = yz * nc + xs; // Second assignment
    mMat[8] = z * z * nc + c;
}

//<End of snippet n. 0>