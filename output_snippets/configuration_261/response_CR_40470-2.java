/* package */ byte[] getIncomingSmsFingerprint() {
    if (mEnvelope == null || mEnvelope.origAddress == null || mEnvelope.bearerData == null) {
        throw new IllegalArgumentException("Invalid mEnvelope or its fields.");
    }
    
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    
    output.write(mEnvelope.teleService);
    output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
    output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);

    long timestamp = System.currentTimeMillis(); // Using a timestamp to reduce collisions
    output.write((byte) (mEnvelope.serviceCategory & 0xFF)); // Write service category in byte form
    output.write((byte) (timestamp & 0xFF)); // Append timestamp to differentiate messages

    return output.toByteArray();
}