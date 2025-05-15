//<Beginning of snippet n. 0>

if (zx >= 0 && zx < 10 && nc >= 0 && nc < 10 && ys >= 0 && ys < 10) {
    mMat[6] = zx * nc + ys;
}
if (xy >= 0 && xy < 10 && nc >= 0 && nc < 10 && zs >= 0 && zs < 10) {
    mMat[1] = xy * nc + zs;
}
if (y >= 0 && nc >= 0 && c >= 0) {
    mMat[4] = y * y * nc + c;
}
if (yz >= 0 && nc >= 0 && xs >= 0) {
    mMat[9] = yz * nc - xs;
}
if (zx >= 0 && nc >= 0 && ys >= 0) {
    mMat[2] = zx * nc - ys;
}
if (yz >= 0 && nc >= 0 && xs >= 0) {
    mMat[7] = yz * nc + xs; // Changed from mMat[6] to mMat[7] to prevent overwriting
}
if (z >= 0 && nc >= 0 && c >= 0) {
    mMat[8] = z * z * nc + c;
}

//<End of snippet n. 0>