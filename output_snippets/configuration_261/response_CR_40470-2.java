/* package */ byte[] getIncomingSmsFingerprint() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    static byte[] lastFingerprint = null;

    if (mEnvelope.teleService == 0x0002 || mEnvelope.teleService == 0x0003) {
        output.write(mEnvelope.teleService);
        output.write(mEnvelope.serviceCategory);
        output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
        output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);
        
        byte[] currentFingerprint = output.toByteArray();
        
        // Ensure uniqueness for fingerprints
        if (lastFingerprint != null && Arrays.equals(currentFingerprint, lastFingerprint)) {
            // Adjust logic here to create a unique fingerprint if a collision is detected
            currentFingerprint[currentFingerprint.length - 1]++; // simple change to avoid collision
        }
        
        lastFingerprint = currentFingerprint;
        return currentFingerprint;
    }
    
    return output.toByteArray();
}