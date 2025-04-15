/*Fixes*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationList.java b/src/com/android/mms/ui/ConversationList.java
//Synthetic comment -- index 443b648..3eaf588 100644

//Synthetic comment -- @@ -93,7 +93,17 @@
private static final int MENU_VIEW                 = 1;
private static final int MENU_VIEW_CONTACT         = 2;
private static final int MENU_ADD_TO_CONTACTS      = 3;
    private static final int MENU_SEND_IM 			   = 4;

private ThreadListQueryHandler mQueryHandler;
private ConversationListAdapter mListAdapter;
//Synthetic comment -- @@ -348,8 +358,8 @@
}

private Uri constructImToUrl(String host, String data) {
	    // don't encode the url, because the Activity Manager can't find using the encoded url
        StringBuilder buf = new StringBuilder("imto://");
buf.append(host);
buf.append('/');
buf.append(data);
//Synthetic comment -- @@ -357,7 +367,7 @@
}

private void sendIm(String address) {
    	// address must be a single recipient
ContactInfoCache cache = ContactInfoCache.getInstance();
ContactInfoCache.CacheEntry info;
if (Mms.isEmailAddress(address)) {
//Synthetic comment -- @@ -367,20 +377,15 @@
info = cache.getContactInfo(this, address);
}
if (info != null && info.person_id > 0) {
            String[] projection = new String[] {
                    Presence.IM_HANDLE, // 0
                    Presence.IM_PROTOCOL, // 1
                    Presence.PRESENCE_STATUS, // 2
            };
            Cursor presenceCursor = getContentResolver().query(Presence.CONTENT_URI, projection,
Presence.PERSON_ID + "=" + info.person_id, null, null);    

            //Just grab the first presense provider
            if(presenceCursor != null && presenceCursor.moveToNext()) {
            	// Find the display info for the provider
                String data = presenceCursor.getString(0);
Object protocolObj = ContactMethods.decodeImProtocol(
                        presenceCursor.getString(1));
String host;
if (protocolObj instanceof Number) {
int protocol = ((Number) protocolObj).intValue();
//Synthetic comment -- @@ -392,7 +397,7 @@

if (!TextUtils.isEmpty(host)) {
// A valid provider name is required
                	Intent intent = new Intent(Intent.ACTION_SENDTO, constructImToUrl(host, data));
startActivity(intent); 
}
}
//Synthetic comment -- @@ -446,8 +451,8 @@
menu.add(0, MENU_ADD_TO_CONTACTS, 0, R.string.menu_add_to_contacts);
}

                        if(entry.presenceResId != 0) {
                        	menu.add(0, MENU_SEND_IM, 0, R.string.menu_send_im);
}
}
menu.add(0, MENU_DELETE, 0, R.string.menu_delete);
//Synthetic comment -- @@ -482,9 +487,9 @@
break;
}
case MENU_SEND_IM: {
            	String address = getAddress(cursor);
            	sendIm(address);
            	break;
}
default:
break;







