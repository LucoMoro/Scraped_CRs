/*Minimal support for Exchange ActiveSync 12.1

Main goal was to allow support of ActiveSync 12.1 and keep 100% backward
compatible. The two additional tags in "searchGal" can be omitted from this
patch (they will give better results, but do carry a performance penalty)...

This was tested on against my employer's Exchange 2007 server (which is set-
up to only allow the ActiveSync 12.1 protocol)...

Again, I tried to get the functionality in with minimal impact. The one thing
I'm not 100% pleased with myself is that the ActiveSync 12.1 protocol will
currently always be used when it's available.

A better option might be to allow the end-user the choice of the supported
protcol, but that would have required much more code-impact, so I settled
for this compromise...

For more information, or more discussion, you can drop me an email..

Change-Id:Ifb07791e5094f7a1a719b10d33002d4998f7796e*/




//Synthetic comment -- diff --git a/src/com/android/exchange/Eas.java b/src/com/android/exchange/Eas.java
//Synthetic comment -- index 8e561e7..e0649ce 100644

//Synthetic comment -- @@ -45,6 +45,9 @@
public static final double SUPPORTED_PROTOCOL_EX2003_DOUBLE = 2.5;
public static final String SUPPORTED_PROTOCOL_EX2007 = "12.0";
public static final double SUPPORTED_PROTOCOL_EX2007_DOUBLE = 12.0;
    // Define Exchange ActiveSync 12.1 protocol
    public static final String SUPPORTED_PROTOCOL_EX2007A = "12.1";
    public static final double SUPPORTED_PROTOCOL_EX2007A_DOUBLE = 12.1;
public static final String DEFAULT_PROTOCOL_VERSION = SUPPORTED_PROTOCOL_EX2003;

// From EAS spec








//Synthetic comment -- diff --git a/src/com/android/exchange/EasSyncService.java b/src/com/android/exchange/EasSyncService.java
//Synthetic comment -- index 20e1ea8..ceaa5d5 100644

//Synthetic comment -- @@ -367,7 +367,8 @@
// Find the most recent version we support
for (String version: supportedVersionsArray) {
if (version.equals(Eas.SUPPORTED_PROTOCOL_EX2003) ||
                    version.equals(Eas.SUPPORTED_PROTOCOL_EX2007) || 
                    version.equals(Eas.SUPPORTED_PROTOCOL_EX2007A)) {
ourVersion = version;
}
}
//Synthetic comment -- @@ -805,11 +806,20 @@
svc.mTrustSsl = (ha.mFlags & HostAuth.FLAG_TRUST_ALL_CERTIFICATES) != 0;
svc.mDeviceId = SyncManager.getDeviceId();
svc.mAccount = acct;
                // Take protocol, otherwise we are using the default (2.5)
                svc.mProtocolVersion = acct.mProtocolVersion;
                svc.mProtocolVersionDouble = Double.parseDouble(svc.mProtocolVersion);
Serializer s = new Serializer();
s.start(Tags.SEARCH_SEARCH).start(Tags.SEARCH_STORE);
s.data(Tags.SEARCH_NAME, "GAL").data(Tags.SEARCH_QUERY, filter);
s.start(Tags.SEARCH_OPTIONS);
s.data(Tags.SEARCH_RANGE, "0-19");  // Return 0..20 results
                // The following two statements maximize search-results at the cost of performance...
                // I'll only include them for ActiveSync 12.1
                if (svc.mProtocolVersionDouble >= Eas.SUPPORTED_PROTOCOL_EX2007A_DOUBLE) {
                    s.tag(Tags.SEARCH_REBUILD_RESULTS); // Rebuild search results (handles changes in search filter better)
                    s.tag(Tags.SEARCH_DEEP_TRAVERSAL); // Also search sub-folders of the GAL
                }
s.end().end().end().done();
if (DEBUG_GAL_SERVICE) svc.userLog("GAL lookup starting for " + ha.mAddress);
HttpResponse resp = svc.sendHttpClientPost("Search", s.toByteArray());
//Synthetic comment -- @@ -2063,9 +2073,11 @@
userLog("sync, sending ", className, " syncKey: ", syncKey);
s.start(Tags.SYNC_SYNC)
.start(Tags.SYNC_COLLECTIONS)
                .start(Tags.SYNC_COLLECTION);
            // ActiveSync 12.1 and higher don't accept a classname here...
            if (mProtocolVersionDouble < Eas.SUPPORTED_PROTOCOL_EX2007A_DOUBLE)
                s.data(Tags.SYNC_CLASS, className);
            s.data(Tags.SYNC_SYNC_KEY, syncKey)
.data(Tags.SYNC_COLLECTION_ID, mailbox.mServerId);

// Start with the default timeout







