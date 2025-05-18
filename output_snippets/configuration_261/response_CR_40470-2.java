/* package */ byte[] getIncomingSmsFingerprint() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    static byte[] lastFingerprint = null;
    static int collisionCounter = 0;

    if (mEnvelope.teleService == 0x0002 || mEnvelope.teleService == 0x0003) {
        output.write(mEnvelope.teleService);
        output.write(mEnvelope.serviceCategory);
        output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
        output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);
        
        byte[] currentFingerprint = output.toByteArray();
        
        // Ensure uniqueness for fingerprints
        if (lastFingerprint != null && Arrays.equals(currentFingerprint, lastFingerprint)) {
            collisionCounter++;
            byte[] uniqueFingerprint = new byte[currentFingerprint.length + 4];
            System.arraycopy(currentFingerprint, 0, uniqueFingerprint, 0, currentFingerprint.length);
            uniqueFingerprint[currentFingerprint.length] = (byte) collisionCounter;
            currentFingerprint = uniqueFingerprint;
        } else {
            collisionCounter = 0;
        }
        
        lastFingerprint = currentFingerprint;
        return currentFingerprint;
    }
    
    return output.toByteArray();
}