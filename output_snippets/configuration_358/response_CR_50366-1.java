//<Beginning of snippet n. 0>
if (zx * nc + ys >= 0 && zx * nc + ys <= 8) {
    mMat[6] = zx * nc + ys;
}
if (xy * nc + zs >= 0 && xy * nc + zs <= 8) {
    mMat[1] = xy * nc + zs;
}
if (y * y * nc + c >= 0 && y * y * nc + c <= 8) {
    mMat[4] = y * y * nc + c;
}
if (yz * nc - xs >= 0 && yz * nc - xs <= 8) {
    mMat[3] = yz * nc - xs; 
}
if (zx * nc - ys >= 0 && zx * nc - ys <= 8) {
    mMat[2] = zx * nc - ys;
}
if (yz * nc + xs >= 0 && yz * nc + xs <= 8) {
    // Remove duplicate assignment to mMat[6], retain the first assignment.
}
if (z * z * nc + c >= 0 && z * z * nc + c <= 8) {
    mMat[8] = z * z * nc + c;
}
//<End of snippet n. 0>