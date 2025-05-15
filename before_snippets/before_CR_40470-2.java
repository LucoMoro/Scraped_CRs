
//<Beginning of snippet n. 0>


/* package */ byte[] getIncomingSmsFingerprint() {
ByteArrayOutputStream output = new ByteArrayOutputStream();

output.write(mEnvelope.teleService);
output.write(mEnvelope.origAddress.origBytes, 0, mEnvelope.origAddress.origBytes.length);
output.write(mEnvelope.bearerData, 0, mEnvelope.bearerData.length);

//<End of snippet n. 0>








