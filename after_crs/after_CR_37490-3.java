/*Clean and format all imported packages

Change-Id:I2e45f5ba1d56b9b17314a8b0f4c0fc1a5d8fc4d9Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/LogTag.java b/src/com/android/mms/LogTag.java
//Synthetic comment -- index cb1534d..bb1959a 100644

//Synthetic comment -- @@ -16,10 +16,6 @@

package com.android.mms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
//Synthetic comment -- @@ -27,6 +23,10 @@
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import com.android.mms.data.Contact;
import com.android.mms.data.Conversation;
import com.android.mms.data.RecipientIdCache;

public class LogTag {
public static final String TAG = "Mms";









//Synthetic comment -- diff --git a/src/com/android/mms/MmsConfig.java b/src/com/android/mms/MmsConfig.java
//Synthetic comment -- index ee4392b..a2112bd 100755

//Synthetic comment -- @@ -21,12 +21,12 @@
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.android.internal.telephony.TelephonyProperties;

public class MmsConfig {
private static final String TAG = "MmsConfig";
private static final boolean DEBUG = true;








//Synthetic comment -- diff --git a/src/com/android/mms/SuggestionsProvider.java b/src/com/android/mms/SuggestionsProvider.java
//Synthetic comment -- index 375851b..358ac06 100644

//Synthetic comment -- @@ -17,7 +17,6 @@
package com.android.mms;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.ContentResolver;








//Synthetic comment -- diff --git a/src/com/android/mms/TempFileProvider.java b/src/com/android/mms/TempFileProvider.java
//Synthetic comment -- index 2ae0632..6e71df7 100644

//Synthetic comment -- @@ -3,6 +3,9 @@

package com.android.mms;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
//Synthetic comment -- @@ -12,9 +15,6 @@
import android.os.ParcelFileDescriptor;
import android.util.Log;

/**
* The TempFileProvider manages a uri, backed by a file, for passing to the camera app for
* capturing pictures and videos and storing the data in a file in the messaging app.








//Synthetic comment -- diff --git a/src/com/android/mms/data/Contact.java b/src/com/android/mms/data/Contact.java
//Synthetic comment -- index d57f8d5..bd0db89 100644

//Synthetic comment -- @@ -13,6 +13,7 @@
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
//Synthetic comment -- @@ -20,22 +21,21 @@
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Presence;
import android.provider.ContactsContract.Profile;
import android.provider.Telephony.Mms;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.MmsApp;
import com.android.mms.R;
import com.android.mms.ui.MessageUtils;

public class Contact {
public static final int CONTACT_METHOD_TYPE_UNKNOWN = 0;








//Synthetic comment -- diff --git a/src/com/android/mms/data/ContactList.java b/src/com/android/mms/data/ContactList.java
//Synthetic comment -- index 90a793f..01c2598 100644

//Synthetic comment -- @@ -8,7 +8,6 @@
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.ui.MessageUtils;









//Synthetic comment -- diff --git a/src/com/android/mms/data/Conversation.java b/src/com/android/mms/data/Conversation.java
//Synthetic comment -- index 1d37d12..a708d25 100644

//Synthetic comment -- @@ -19,8 +19,8 @@
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.Sms;
import android.provider.Telephony.Sms.Conversations;
import android.provider.Telephony.Threads;
import android.provider.Telephony.ThreadsColumns;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
//Synthetic comment -- @@ -32,7 +32,6 @@
import com.android.mms.transaction.MessagingNotification;
import com.android.mms.ui.MessageUtils;
import com.android.mms.util.DraftCache;

/**
* An interface for finding information about conversations and/or creating new ones.








//Synthetic comment -- diff --git a/src/com/android/mms/data/RecipientIdCache.java b/src/com/android/mms/data/RecipientIdCache.java
//Synthetic comment -- index 6857e32..34cbf8a 100644

//Synthetic comment -- @@ -8,18 +8,18 @@
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.LogTag;

@ThreadSafe
public class RecipientIdCache {








//Synthetic comment -- diff --git a/src/com/android/mms/data/WorkingMessage.java b/src/com/android/mms/data/WorkingMessage.java
//Synthetic comment -- index d6bd3b1..b63f366 100755

//Synthetic comment -- @@ -42,6 +42,7 @@

import com.android.common.contacts.DataUsageStatUpdater;
import com.android.common.userhappiness.UserHappinessSignals;
import com.android.mms.ContentRestrictionException;
import com.android.mms.ExceedMessageSizeException;
import com.android.mms.LogTag;
import com.android.mms.MmsApp;








//Synthetic comment -- diff --git a/src/com/android/mms/dom/smil/SmilLayoutElementImpl.java b/src/com/android/mms/dom/smil/SmilLayoutElementImpl.java
//Synthetic comment -- index 99edbb8..aae4be4 100644

//Synthetic comment -- @@ -17,12 +17,12 @@

package com.android.mms.dom.smil;

import org.w3c.dom.NodeList;
import org.w3c.dom.smil.SMILLayoutElement;
import org.w3c.dom.smil.SMILRootLayoutElement;

import com.android.mms.layout.LayoutManager;

public class SmilLayoutElementImpl extends SmilElementImpl implements
SMILLayoutElement {
SmilLayoutElementImpl(SmilDocumentImpl owner, String tagName) {








//Synthetic comment -- diff --git a/src/com/android/mms/dom/smil/SmilMediaElementImpl.java b/src/com/android/mms/dom/smil/SmilMediaElementImpl.java
//Synthetic comment -- index 10d885b..324f3a7 100644

//Synthetic comment -- @@ -17,7 +17,6 @@

package com.android.mms.dom.smil;

import org.w3c.dom.DOMException;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
//Synthetic comment -- @@ -27,6 +26,8 @@

import android.util.Log;

import com.android.mms.dom.events.EventImpl;

public class SmilMediaElementImpl extends SmilElementImpl implements
SMILMediaElement {
public final static String SMIL_MEDIA_START_EVENT = "SmilMediaStart";








//Synthetic comment -- diff --git a/src/com/android/mms/dom/smil/SmilPlayer.java b/src/com/android/mms/dom/smil/SmilPlayer.java
//Synthetic comment -- index af2c763..a0a9044 100644

//Synthetic comment -- @@ -17,6 +17,11 @@

package com.android.mms.dom.smil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import org.w3c.dom.NodeList;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
//Synthetic comment -- @@ -29,11 +34,6 @@

import android.util.Log;

/**
* The SmilPlayer is responsible for playing, stopping, pausing and resuming a SMIL tree.
* <li>It creates a whole timeline before playing.</li>








//Synthetic comment -- diff --git a/src/com/android/mms/dom/smil/parser/SmilContentHandler.java b/src/com/android/mms/dom/smil/parser/SmilContentHandler.java
//Synthetic comment -- index 12c11e7..1c92f59 100644

//Synthetic comment -- @@ -17,13 +17,13 @@

package com.android.mms.dom.smil.parser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.smil.SMILDocument;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.android.mms.dom.smil.SmilDocumentImpl;









//Synthetic comment -- diff --git a/src/com/android/mms/dom/smil/parser/SmilXmlParser.java b/src/com/android/mms/dom/smil/parser/SmilXmlParser.java
//Synthetic comment -- index 4af20a3..969113a 100644

//Synthetic comment -- @@ -17,18 +17,16 @@

package com.android.mms.dom.smil.parser;

import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.smil.SMILDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.android.mms.MmsException;

public class SmilXmlParser {
private XMLReader mXmlReader;








//Synthetic comment -- diff --git a/src/com/android/mms/drm/DrmUtils.java b/src/com/android/mms/drm/DrmUtils.java
//Synthetic comment -- index 019487b..c1a321c 100644

//Synthetic comment -- @@ -17,16 +17,11 @@

package com.android.mms.drm;

import android.drm.DrmManagerClient;
import android.drm.DrmStore;
import android.net.Uri;
import android.util.Log;

import com.android.mms.MmsApp;

public class DrmUtils {








//Synthetic comment -- diff --git a/src/com/android/mms/layout/LayoutManager.java b/src/com/android/mms/layout/LayoutManager.java
//Synthetic comment -- index eae2479..76cb5ae 100644

//Synthetic comment -- @@ -20,8 +20,6 @@
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

/**
* MMS presentation layout management.








//Synthetic comment -- diff --git a/src/com/android/mms/model/AudioModel.java b/src/com/android/mms/model/AudioModel.java
//Synthetic comment -- index b1fe051..a6f08a4 100644

//Synthetic comment -- @@ -17,26 +17,25 @@

package com.android.mms.model;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.events.Event;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.MediaStore.Audio;
import android.provider.Telephony.Mms.Part;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.ContentRestrictionException;
import com.android.mms.dom.events.EventImpl;
import com.android.mms.dom.smil.SmilMediaElementImpl;
import com.google.android.mms.MmsException;

public class AudioModel extends MediaModel {
private static final String TAG = MediaModel.TAG;








//Synthetic comment -- diff --git a/src/com/android/mms/model/CarrierContentRestriction.java b/src/com/android/mms/model/CarrierContentRestriction.java
//Synthetic comment -- index 949c5b4..2d16745 100644

//Synthetic comment -- @@ -21,13 +21,13 @@
import android.content.ContentResolver;
import android.util.Log;

import com.android.mms.ContentRestrictionException;
import com.android.mms.ExceedMessageSizeException;
import com.android.mms.LogTag;
import com.android.mms.MmsConfig;
import com.android.mms.ResolutionException;
import com.android.mms.UnsupportContentTypeException;
import com.google.android.mms.ContentType;

public class CarrierContentRestriction implements ContentRestriction {
private static final ArrayList<String> sSupportedImageTypes;








//Synthetic comment -- diff --git a/src/com/android/mms/model/ImageModel.java b/src/com/android/mms/model/ImageModel.java
//Synthetic comment -- index 40810aa..1fede11 100644

//Synthetic comment -- @@ -17,20 +17,10 @@

package com.android.mms.model;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.events.Event;
import org.w3c.dom.smil.ElementTime;
//Synthetic comment -- @@ -42,11 +32,19 @@
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.ContentRestrictionException;
import com.android.mms.ExceedMessageSizeException;
import com.android.mms.LogTag;
import com.android.mms.MmsApp;
import com.android.mms.MmsConfig;
import com.android.mms.dom.smil.SmilMediaElementImpl;
import com.android.mms.ui.UriImage;
import com.android.mms.util.ItemLoadedCallback;
import com.android.mms.util.ItemLoadedFuture;
import com.android.mms.util.ThumbnailManager;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.PduPart;
import com.google.android.mms.pdu.PduPersister;


public class ImageModel extends RegionMediaModel {








//Synthetic comment -- diff --git a/src/com/android/mms/model/LayoutModel.java b/src/com/android/mms/model/LayoutModel.java
//Synthetic comment -- index e52b65c..97b1637 100644

//Synthetic comment -- @@ -17,13 +17,13 @@

package com.android.mms.model;

import java.util.ArrayList;

import android.util.Config;
import android.util.Log;

import com.android.mms.layout.LayoutManager;
import com.android.mms.layout.LayoutParameters;

public class LayoutModel extends Model {
private static final String TAG = SlideModel.TAG;








//Synthetic comment -- diff --git a/src/com/android/mms/model/MediaModel.java b/src/com/android/mms/model/MediaModel.java
//Synthetic comment -- index 452d361..f346ce4 100644

//Synthetic comment -- @@ -17,24 +17,24 @@

package com.android.mms.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.w3c.dom.events.EventListener;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import com.android.mms.LogTag;
import com.google.android.mms.MmsException;
// TODO: remove dependency for SDK build

public abstract class MediaModel extends Model implements EventListener {
protected static final String TAG = "Mms/media";








//Synthetic comment -- diff --git a/src/com/android/mms/model/MediaModelFactory.java b/src/com/android/mms/model/MediaModelFactory.java
//Synthetic comment -- index 9928395..6ed7a7b 100644

//Synthetic comment -- @@ -17,14 +17,7 @@

package com.android.mms.model;

import java.io.IOException;

import org.w3c.dom.smil.SMILMediaElement;
import org.w3c.dom.smil.SMILRegionElement;
//Synthetic comment -- @@ -35,7 +28,12 @@
import android.content.Context;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.MmsConfig;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.PduBody;
import com.google.android.mms.pdu.PduPart;

public class MediaModelFactory {
private static final String TAG = "Mms:media";








//Synthetic comment -- diff --git a/src/com/android/mms/model/RegionMediaModel.java b/src/com/android/mms/model/RegionMediaModel.java
//Synthetic comment -- index 8bbb06b..2ea7da3 100644

//Synthetic comment -- @@ -17,12 +17,10 @@

package com.android.mms.model;

import android.content.Context;
import android.net.Uri;

import com.google.android.mms.MmsException;

public abstract class RegionMediaModel extends MediaModel {
protected RegionModel mRegion;








//Synthetic comment -- diff --git a/src/com/android/mms/model/SlideModel.java b/src/com/android/mms/model/SlideModel.java
//Synthetic comment -- index 3c8d70b..98c66f0 100644

//Synthetic comment -- @@ -17,24 +17,24 @@

package com.android.mms.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.smil.ElementTime;

import android.text.TextUtils;
import android.util.Config;
import android.util.Log;

import com.android.mms.ContentRestrictionException;
import com.android.mms.dom.smil.SmilParElementImpl;
import com.google.android.mms.ContentType;

public class SlideModel extends Model implements List<MediaModel>, EventListener {
public static final String TAG = "Mms/slideshow";
private static final boolean DEBUG = false;








//Synthetic comment -- diff --git a/src/com/android/mms/model/SlideshowModel.java b/src/com/android/mms/model/SlideshowModel.java
//Synthetic comment -- index e2ecb0d..fcb41d0 100755

//Synthetic comment -- @@ -18,21 +18,13 @@
package com.android.mms.model;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventTarget;
//Synthetic comment -- @@ -49,15 +41,21 @@
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.ContentRestrictionException;
import com.android.mms.ExceedMessageSizeException;
import com.android.mms.LogTag;
import com.android.mms.MmsConfig;
import com.android.mms.dom.smil.parser.SmilXmlSerializer;
import com.android.mms.layout.LayoutManager;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.GenericPdu;
import com.google.android.mms.pdu.MultimediaMessagePdu;
import com.google.android.mms.pdu.PduBody;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduPart;
import com.google.android.mms.pdu.PduPersister;

public class SlideshowModel extends Model
implements List<SlideModel>, IModelChangedObserver {








//Synthetic comment -- diff --git a/src/com/android/mms/model/SmilHelper.java b/src/com/android/mms/model/SmilHelper.java
//Synthetic comment -- index 577ba5a..755bcab 100644

//Synthetic comment -- @@ -24,15 +24,11 @@
import static com.android.mms.dom.smil.SmilParElementImpl.SMIL_SLIDE_END_EVENT;
import static com.android.mms.dom.smil.SmilParElementImpl.SMIL_SLIDE_START_EVENT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.events.EventTarget;
import org.w3c.dom.smil.SMILDocument;
//Synthetic comment -- @@ -45,22 +41,19 @@
import org.w3c.dom.smil.SMILRootLayoutElement;
import org.xml.sax.SAXException;

import android.drm.DrmManagerClient;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;

import com.android.mms.MmsApp;
import com.android.mms.dom.smil.SmilDocumentImpl;
import com.android.mms.dom.smil.parser.SmilXmlParser;
import com.android.mms.dom.smil.parser.SmilXmlSerializer;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.PduBody;
import com.google.android.mms.pdu.PduPart;

public class SmilHelper {
private static final String TAG = "Mms/smil";








//Synthetic comment -- diff --git a/src/com/android/mms/model/TextModel.java b/src/com/android/mms/model/TextModel.java
//Synthetic comment -- index a63be87..15ff5eb 100644

//Synthetic comment -- @@ -17,8 +17,7 @@

package com.android.mms.model;

import java.io.UnsupportedEncodingException;

import org.w3c.dom.events.Event;
import org.w3c.dom.smil.ElementTime;
//Synthetic comment -- @@ -26,8 +25,8 @@
import android.content.Context;
import android.util.Log;

import com.android.mms.dom.smil.SmilMediaElementImpl;
import com.google.android.mms.pdu.CharacterSets;

public class TextModel extends RegionMediaModel {
private static final String TAG = "Mms/text";








//Synthetic comment -- diff --git a/src/com/android/mms/model/VideoModel.java b/src/com/android/mms/model/VideoModel.java
//Synthetic comment -- index 75d5c5d..67f4be3 100644

//Synthetic comment -- @@ -17,6 +17,19 @@

package com.android.mms.model;

import org.w3c.dom.events.Event;
import org.w3c.dom.smil.ElementTime;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.android.mms.ContentRestrictionException;
import com.android.mms.LogTag;
import com.android.mms.MmsApp;
//Synthetic comment -- @@ -25,23 +38,8 @@
import com.android.mms.util.ItemLoadedCallback;
import com.android.mms.util.ItemLoadedFuture;
import com.android.mms.util.ThumbnailManager;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;

public class VideoModel extends RegionMediaModel {
private static final String TAG = MediaModel.TAG;








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/HttpUtils.java b/src/com/android/mms/transaction/HttpUtils.java
//Synthetic comment -- index 832f306..b6c878c 100644

//Synthetic comment -- @@ -17,6 +17,13 @@

package com.android.mms.transaction;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
//Synthetic comment -- @@ -25,13 +32,9 @@
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.net.http.AndroidHttpClient;
//Synthetic comment -- @@ -40,14 +43,8 @@
import android.util.Config;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.MmsConfig;

public class HttpUtils {
private static final String TAG = LogTag.TRANSACTION;








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessageStatusService.java b/src/com/android/mms/transaction/MessageStatusService.java
//Synthetic comment -- index 0a07c9b..19dd8c3 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.mms.transaction;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
//Synthetic comment -- @@ -31,6 +29,8 @@
import android.telephony.SmsMessage;
import android.util.Log;

import com.android.mms.LogTag;

/**
* Service that gets started by the MessageStatusReceiver when a message status report is
* received.








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MessagingNotification.java b/src/com/android/mms/transaction/MessagingNotification.java
//Synthetic comment -- index 2a886fa..a003ef8 100644

//Synthetic comment -- @@ -20,40 +20,27 @@
import static com.google.android.mms.pdu.PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND;
import static com.google.android.mms.pdu.PduHeaders.MESSAGE_TYPE_RETRIEVE_CONF;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
//Synthetic comment -- @@ -73,13 +60,25 @@
import android.util.Log;
import android.widget.Toast;

import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.data.Contact;
import com.android.mms.data.Conversation;
import com.android.mms.data.WorkingMessage;
import com.android.mms.model.SlideModel;
import com.android.mms.model.SlideshowModel;
import com.android.mms.ui.ComposeMessageActivity;
import com.android.mms.ui.ConversationList;
import com.android.mms.ui.MessagingPreferenceActivity;
import com.android.mms.util.AddressUtils;
import com.android.mms.util.DownloadManager;
import com.android.mms.widget.MmsWidgetProvider;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.GenericPdu;
import com.google.android.mms.pdu.MultimediaMessagePdu;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduPersister;

/**
* This class is used to update the notification indicator. It will check whether








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MmsMessageSender.java b/src/com/android/mms/transaction/MmsMessageSender.java
//Synthetic comment -- index ccd9ef0..531cc02 100644

//Synthetic comment -- @@ -17,6 +17,18 @@

package com.android.mms.transaction;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.MmsSms.PendingMessages;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.ui.MessagingPreferenceActivity;
import com.android.mms.util.SendingProgressTokenManager;
//Synthetic comment -- @@ -30,20 +42,6 @@
import com.google.android.mms.pdu.SendReq;
import com.google.android.mms.util.SqliteWrapper;

public class MmsMessageSender implements MessageSender {
private static final String TAG = "MmsMessageSender";









//Synthetic comment -- diff --git a/src/com/android/mms/transaction/MmsSystemEventReceiver.java b/src/com/android/mms/transaction/MmsSystemEventReceiver.java
//Synthetic comment -- index 2a8fdfc..b8eb917 100644

//Synthetic comment -- @@ -25,12 +25,10 @@
import android.provider.Telephony.Mms;
import android.util.Log;

import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.TelephonyIntents;
import com.android.mms.LogTag;
import com.android.mms.MmsApp;

/**
* MmsSystemEventReceiver receives the








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/NotificationPlayer.java b/src/com/android/mms/transaction/NotificationPlayer.java
//Synthetic comment -- index fe10ac0..1b0fcfc 100644

//Synthetic comment -- @@ -21,6 +21,8 @@

package com.android.mms.transaction;

import java.util.LinkedList;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
//Synthetic comment -- @@ -31,9 +33,6 @@
import android.os.SystemClock;
import android.util.Log;

/**
* @hide
* This class is provides the same interface and functionality as android.media.AsyncPlayer








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/NotificationTransaction.java b/src/com/android/mms/transaction/NotificationTransaction.java
//Synthetic comment -- index 3077a6c..83b0fb1 100644

//Synthetic comment -- @@ -25,6 +25,17 @@
import static com.google.android.mms.pdu.PduHeaders.STATUS_RETRIEVED;
import static com.google.android.mms.pdu.PduHeaders.STATUS_UNRECOGNIZED;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Inbox;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.mms.MmsApp;
import com.android.mms.MmsConfig;
import com.android.mms.util.DownloadManager;
//Synthetic comment -- @@ -38,18 +49,6 @@
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;

/**
* The NotificationTransaction is responsible for handling multimedia








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/PrivilegedSmsReceiver.java b/src/com/android/mms/transaction/PrivilegedSmsReceiver.java
//Synthetic comment -- index c2d0dca..f1e21a8 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package com.android.mms.transaction;

import android.content.Context;
import android.content.Intent;









//Synthetic comment -- diff --git a/src/com/android/mms/transaction/ProgressCallbackEntity.java b/src/com/android/mms/transaction/ProgressCallbackEntity.java
//Synthetic comment -- index 8ff72f9..629dc02 100644

//Synthetic comment -- @@ -17,14 +17,14 @@

package com.android.mms.transaction;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.entity.ByteArrayEntity;

import android.content.Context;
import android.content.Intent;

public class ProgressCallbackEntity extends ByteArrayEntity {
private static final int DEFAULT_PIECE_SIZE = 4096;









//Synthetic comment -- diff --git a/src/com/android/mms/transaction/PushReceiver.java b/src/com/android/mms/transaction/PushReceiver.java
//Synthetic comment -- index 5da16fd..ebed8fb 100644

//Synthetic comment -- @@ -21,6 +21,20 @@
import static com.google.android.mms.pdu.PduHeaders.MESSAGE_TYPE_DELIVERY_IND;
import static com.google.android.mms.pdu.PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND;
import static com.google.android.mms.pdu.PduHeaders.MESSAGE_TYPE_READ_ORIG_IND;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Inbox;
import android.util.Log;

import com.android.mms.MmsConfig;
import com.google.android.mms.ContentType;
//Synthetic comment -- @@ -32,21 +46,6 @@
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.ReadOrigInd;

/**
* Receives Intent.WAP_PUSH_RECEIVED_ACTION intents and starts the








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/ReadRecTransaction.java b/src/com/android/mms/transaction/ReadRecTransaction.java
//Synthetic comment -- index 31993c0..d424860 100644

//Synthetic comment -- @@ -17,19 +17,19 @@

package com.android.mms.transaction;

import java.io.IOException;

import android.content.Context;
import android.net.Uri;
import android.provider.Telephony.Mms.Sent;
import android.util.Log;

import com.android.mms.ui.MessageUtils;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.PduComposer;
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.ReadRecInd;

/**
* The ReadRecTransaction is responsible for sending read report








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/RetrieveTransaction.java b/src/com/android/mms/transaction/RetrieveTransaction.java
//Synthetic comment -- index 6952187..b055841 100644

//Synthetic comment -- @@ -17,6 +17,18 @@

package com.android.mms.transaction;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Inbox;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.MmsConfig;
import com.android.mms.ui.MessageUtils;
import com.android.mms.util.DownloadManager;
//Synthetic comment -- @@ -30,19 +42,6 @@
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.RetrieveConf;

/**
* The RetrieveTransaction is responsible for retrieving multimedia








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/RetryScheduler.java b/src/com/android/mms/transaction/RetryScheduler.java
//Synthetic comment -- index 5c5cc5c..4788a58 100644

//Synthetic comment -- @@ -17,13 +17,6 @@

package com.android.mms.transaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
//Synthetic comment -- @@ -32,16 +25,21 @@
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.MmsSms.PendingMessages;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.util.DownloadManager;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduPersister;

public class RetryScheduler implements Observer {
private static final String TAG = "RetryScheduler";
private static final boolean DEBUG = false;








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SendTransaction.java b/src/com/android/mms/transaction/SendTransaction.java
//Synthetic comment -- index d26589e..a94307f 100644

//Synthetic comment -- @@ -17,31 +17,29 @@

package com.android.mms.transaction;

import java.util.Arrays;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Sent;
import android.text.TextUtils;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.ui.MessageUtils;
import com.android.mms.util.RateController;
import com.android.mms.util.SendingProgressTokenManager;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.PduComposer;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.SendConf;
import com.google.android.mms.pdu.SendReq;

/**
* The SendTransaction is responsible for sending multimedia messages








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SimFullReceiver.java b/src/com/android/mms/transaction/SimFullReceiver.java
//Synthetic comment -- index 6c715b4..e7baecd 100644

//Synthetic comment -- @@ -17,9 +17,6 @@

package com.android.mms.transaction;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
//Synthetic comment -- @@ -29,6 +26,9 @@
import android.provider.Settings;
import android.provider.Telephony;

import com.android.mms.R;
import com.android.mms.ui.ManageSimMessages;

/**
* Receive Intent.SIM_FULL_ACTION.  Handle notification that SIM is full.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsMessageSender.java b/src/com/android/mms/transaction/SmsMessageSender.java
//Synthetic comment -- index 768f615..552b466 100644

//Synthetic comment -- @@ -17,22 +17,22 @@

package com.android.mms.transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Telephony.Sms;
import android.provider.Telephony.Sms.Inbox;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.ui.MessagingPreferenceActivity;
import com.google.android.mms.MmsException;

public class SmsMessageSender implements MessageSender {
protected final Context mContext;
protected final int mNumberOfDests;








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsReceiver.java b/src/com/android/mms/transaction/SmsReceiver.java
//Synthetic comment -- index 8c70dff..22eb4e6 100644

//Synthetic comment -- @@ -19,11 +19,10 @@

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.provider.Telephony.Sms.Intents;

/**
* Handle incoming SMSes.  Just dispatches the work off to a Service.








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsReceiverService.java b/src/com/android/mms/transaction/SmsReceiverService.java
//Synthetic comment -- index 099e41d..702c0cc 100755

//Synthetic comment -- @@ -23,15 +23,6 @@
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
//Synthetic comment -- @@ -41,6 +32,7 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
//Synthetic comment -- @@ -49,7 +41,6 @@
import android.os.Message;
import android.os.Process;
import android.provider.Telephony.Sms;
import android.provider.Telephony.Sms.Inbox;
import android.provider.Telephony.Sms.Intents;
import android.provider.Telephony.Sms.Outbox;
//Synthetic comment -- @@ -61,8 +52,15 @@
import android.widget.Toast;

import com.android.internal.telephony.TelephonyIntents;
import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.data.Contact;
import com.android.mms.data.Conversation;
import com.android.mms.ui.ClassZeroActivity;
import com.android.mms.util.Recycler;
import com.android.mms.util.SendingProgressTokenManager;
import com.android.mms.widget.MmsWidgetProvider;
import com.google.android.mms.MmsException;

/**
* This service essentially plays the role of a "worker thread", allowing us to store








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsRejectedReceiver.java b/src/com/android/mms/transaction/SmsRejectedReceiver.java
//Synthetic comment -- index b433693..e60f5ad 100644

//Synthetic comment -- @@ -16,8 +16,6 @@

package com.android.mms.transaction;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
//Synthetic comment -- @@ -26,6 +24,8 @@
import android.content.Intent;
import android.provider.Settings;
import android.provider.Telephony;

import com.android.mms.R;
import com.android.mms.ui.ConversationList;










//Synthetic comment -- diff --git a/src/com/android/mms/transaction/SmsSingleRecipientSender.java b/src/com/android/mms/transaction/SmsSingleRecipientSender.java
//Synthetic comment -- index 286cffc..df267aa 100644

//Synthetic comment -- @@ -7,17 +7,16 @@
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Sms;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;

import com.android.mms.LogTag;
import com.android.mms.MmsConfig;
import com.android.mms.data.Conversation;
import com.android.mms.ui.MessageUtils;
import com.google.android.mms.MmsException;

public class SmsSingleRecipientSender extends SmsMessageSender {









//Synthetic comment -- diff --git a/src/com/android/mms/transaction/Transaction.java b/src/com/android/mms/transaction/Transaction.java
//Synthetic comment -- index f3225e3..adc1722 100644

//Synthetic comment -- @@ -17,18 +17,17 @@

package com.android.mms.transaction;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.android.mms.util.SendingProgressTokenManager;
import com.google.android.mms.MmsException;

/**
* Transaction is an abstract class for notification transaction, send transaction
* and other transactions described in MMS spec.








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/TransactionService.java b/src/com/android/mms/transaction/TransactionService.java
//Synthetic comment -- index 99a48ee..6867705 100644

//Synthetic comment -- @@ -17,21 +17,8 @@

package com.android.mms.transaction;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Service;
import android.content.BroadcastReceiver;
//Synthetic comment -- @@ -40,7 +27,6 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
//Synthetic comment -- @@ -50,12 +36,23 @@
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Telephony.Mms;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.MmsSms.PendingMessages;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.util.RateController;
import com.google.android.mms.pdu.GenericPdu;
import com.google.android.mms.pdu.NotificationInd;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;

/**
* The TransactionService of the MMS Client is responsible for handling requests








//Synthetic comment -- diff --git a/src/com/android/mms/transaction/TransactionSettings.java b/src/com/android/mms/transaction/TransactionSettings.java
//Synthetic comment -- index 25e2b5c..728f03a 100644

//Synthetic comment -- @@ -17,20 +17,17 @@

package com.android.mms.transaction;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.NetworkUtils;
import android.provider.Telephony;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.PhoneConstants;
import com.android.mms.LogTag;

/**
* Container of transaction settings. Instances of this class are contained
* within Transaction instances to allow overriding of the default APN








//Synthetic comment -- diff --git a/src/com/android/mms/ui/AsyncDialog.java b/src/com/android/mms/ui/AsyncDialog.java
//Synthetic comment -- index e3569a2..d2571d7 100644

//Synthetic comment -- @@ -16,12 +16,8 @@

package com.android.mms.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;









//Synthetic comment -- diff --git a/src/com/android/mms/ui/AttachmentEditor.java b/src/com/android/mms/ui/AttachmentEditor.java
//Synthetic comment -- index 93ef17e..8854c4c 100644

//Synthetic comment -- @@ -17,24 +17,22 @@

package com.android.mms.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.mms.R;
import com.android.mms.data.WorkingMessage;
import com.android.mms.model.SlideModel;
import com.android.mms.model.SlideshowModel;

/**
* This is an embedded editor/view to add photos and sound/video clips
* into a multimedia message.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/AttachmentTypeSelectorAdapter.java b/src/com/android/mms/ui/AttachmentTypeSelectorAdapter.java
//Synthetic comment -- index 14e801b..5831d6f 100644

//Synthetic comment -- @@ -17,13 +17,13 @@

package com.android.mms.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.android.mms.MmsConfig;
import com.android.mms.R;

/**
* An adapter to store icons and strings for attachment type list.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/AudioAttachmentView.java b/src/com/android/mms/ui/AudioAttachmentView.java
//Synthetic comment -- index d859fb8..0fd3d41 100644

//Synthetic comment -- @@ -17,7 +17,7 @@

package com.android.mms.ui;

import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
//Synthetic comment -- @@ -32,7 +32,7 @@
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mms.R;

/**
* This class provides an embedded editor/viewer of audio attachment.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/BasicSlideEditorView.java b/src/com/android/mms/ui/BasicSlideEditorView.java
//Synthetic comment -- index dbcdd72..9b22562 100644

//Synthetic comment -- @@ -17,12 +17,11 @@

package com.android.mms.ui;

import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
//Synthetic comment -- @@ -34,8 +33,7 @@
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mms.R;

/**
* This is a basic view to show and edit a slide.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ChipsRecipientAdapter.java b/src/com/android/mms/ui/ChipsRecipientAdapter.java
//Synthetic comment -- index aa1b53f..fe4cc90 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.mms.ui;

import android.content.Context;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.mms.R;

public class ChipsRecipientAdapter extends BaseRecipientAdapter {
private static final int DEFAULT_PREFERRED_MAX_RESULT_COUNT = 10;









//Synthetic comment -- diff --git a/src/com/android/mms/ui/ClassZeroActivity.java b/src/com/android/mms/ui/ClassZeroActivity.java
//Synthetic comment -- index 891a911..0abcdda 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//Synthetic comment -- @@ -40,8 +41,6 @@
import com.android.mms.R;
import com.android.mms.transaction.MessagingNotification;

/**
* Display a class-zero SMS message to the user. Wait for the user to dismiss
* it.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ComposeMessageActivity.java b/src/com/android/mms/ui/ComposeMessageActivity.java
//Synthetic comment -- index 0bbceec..5918464 100644

//Synthetic comment -- @@ -47,14 +47,15 @@
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
//Synthetic comment -- @@ -73,19 +74,19 @@
import android.os.SystemProperties;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Intents;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.provider.Settings;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Sms;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
//Synthetic comment -- @@ -95,15 +96,15 @@
import android.text.util.Linkify;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnKeyListener;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
//Synthetic comment -- @@ -129,13 +130,6 @@
import com.android.mms.data.WorkingMessage;
import com.android.mms.data.WorkingMessage.MessageStatusListener;
import com.android.mms.drm.DrmUtils;
import com.android.mms.model.SlideModel;
import com.android.mms.model.SlideshowModel;
import com.android.mms.transaction.MessagingNotification;
//Synthetic comment -- @@ -146,8 +140,13 @@
import com.android.mms.util.PhoneNumberFormatter;
import com.android.mms.util.SendingProgressTokenManager;
import com.android.mms.util.SmileyParser;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.PduBody;
import com.google.android.mms.pdu.PduPart;
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.SendReq;

/**
* This is the main UI for:








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConfirmRateLimitActivity.java b/src/com/android/mms/ui/ConfirmRateLimitActivity.java
//Synthetic comment -- index 74b971a..35230d0 100644

//Synthetic comment -- @@ -18,10 +18,6 @@
package com.android.mms.ui;

import static com.android.mms.util.RateController.RATE_LIMIT_CONFIRMED_ACTION;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//Synthetic comment -- @@ -29,10 +25,13 @@
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.android.mms.R;
import com.android.mms.util.RateController;

public class ConfirmRateLimitActivity extends Activity {
private static final String TAG = "ConfirmRateLimitActivity";
private static final boolean DEBUG = false;








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationList.java b/src/com/android/mms/ui/ConversationList.java
//Synthetic comment -- index 345bec3..9d5d5ec 100644

//Synthetic comment -- @@ -21,6 +21,52 @@
import java.util.Collection;
import java.util.HashSet;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.AsyncQueryHandler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SqliteWrapper;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Threads;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.data.Contact;
//Synthetic comment -- @@ -33,53 +79,6 @@
import com.android.mms.util.Recycler;
import com.google.android.mms.pdu.PduHeaders;

/**
* This activity provides a list view of existing conversations.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationListAdapter.java b/src/com/android/mms/ui/ConversationListAdapter.java
//Synthetic comment -- index a6492f8..40d0eaf 100644

//Synthetic comment -- @@ -17,10 +17,6 @@

package com.android.mms.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
//Synthetic comment -- @@ -30,6 +26,9 @@
import android.widget.AbsListView;
import android.widget.CursorAdapter;

import com.android.mms.R;
import com.android.mms.data.Conversation;

/**
* The back-end data adapter for ConversationList.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ConversationListItem.java b/src/com/android/mms/ui/ConversationListItem.java
//Synthetic comment -- index 16c76a9..730a39b 100644

//Synthetic comment -- @@ -17,17 +17,9 @@

package com.android.mms.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
//Synthetic comment -- @@ -42,6 +34,13 @@
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.data.Contact;
import com.android.mms.data.ContactList;
import com.android.mms.data.Conversation;
import com.android.mms.util.SmileyParser;

/**
* This class manages the view for given conversation.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/ui/DeliveryReportActivity.java b/src/com/android/mms/ui/DeliveryReportActivity.java
//Synthetic comment -- index c22042a..e81de83 100644

//Synthetic comment -- @@ -17,13 +17,17 @@

package com.android.mms.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony.Mms;
//Synthetic comment -- @@ -36,12 +40,8 @@
import android.view.Window;
import android.widget.ListView;

import com.android.mms.R;
import com.google.android.mms.pdu.PduHeaders;

/**
* This is the UI for displaying a delivery report:








//Synthetic comment -- diff --git a/src/com/android/mms/ui/DeliveryReportAdapter.java b/src/com/android/mms/ui/DeliveryReportAdapter.java
//Synthetic comment -- index 9463a58..f3a63b9 100644

//Synthetic comment -- @@ -16,7 +16,7 @@
*/
package com.android.mms.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
//Synthetic comment -- @@ -24,7 +24,7 @@
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.mms.R;

/**
* The back-end data adapter for DeliveryReportActivity.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/DeliveryReportListItem.java b/src/com/android/mms/ui/DeliveryReportListItem.java
//Synthetic comment -- index 12361b7..46a9fd9 100644

//Synthetic comment -- @@ -17,9 +17,6 @@

package com.android.mms.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
//Synthetic comment -- @@ -28,6 +25,9 @@
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mms.R;
import com.android.mms.data.Contact;

/**
* This class displays the status for a single recipient of a message.  It is used in
* the ListView of DeliveryReportActivity.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/EditSlideDurationActivity.java b/src/com/android/mms/ui/EditSlideDurationActivity.java
//Synthetic comment -- index ee317ff..222875c 100644

//Synthetic comment -- @@ -20,15 +20,11 @@
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;








//Synthetic comment -- diff --git a/src/com/android/mms/ui/IconListAdapter.java b/src/com/android/mms/ui/IconListAdapter.java
//Synthetic comment -- index f99b956..e52a0d2 100644

//Synthetic comment -- @@ -17,6 +17,8 @@

package com.android.mms.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
//Synthetic comment -- @@ -25,8 +27,6 @@
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mms.R;

/**








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ImageAttachmentView.java b/src/com/android/mms/ui/ImageAttachmentView.java
//Synthetic comment -- index 5ae282b..3ec0c40 100644

//Synthetic comment -- @@ -17,7 +17,7 @@

package com.android.mms.ui;

import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
//Synthetic comment -- @@ -29,7 +29,7 @@
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.mms.R;

/**
* This class provides an embedded editor/viewer of picture attachment.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/LayoutSelectorAdapter.java b/src/com/android/mms/ui/LayoutSelectorAdapter.java
//Synthetic comment -- index 2d8878c..4912e9a 100644

//Synthetic comment -- @@ -17,11 +17,11 @@

package com.android.mms.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.android.mms.R;

/**








//Synthetic comment -- diff --git a/src/com/android/mms/ui/ManageSimMessages.java b/src/com/android/mms/ui/ManageSimMessages.java
//Synthetic comment -- index f495428..68fd01d 100644

//Synthetic comment -- @@ -17,22 +17,18 @@

package com.android.mms.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//Synthetic comment -- @@ -48,6 +44,9 @@
import android.widget.ListView;
import android.widget.TextView;

import com.android.mms.R;
import com.android.mms.transaction.MessagingNotification;

/**
* Displays a list of the SMS messages stored on the ICC.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageListAdapter.java b/src/com/android/mms/ui/MessageListAdapter.java
//Synthetic comment -- index 04f965e..bff070f 100644

//Synthetic comment -- @@ -17,6 +17,8 @@

package com.android.mms.ui;

import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
//Synthetic comment -- @@ -35,11 +37,10 @@
import android.widget.AbsListView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.android.mms.R;
import com.google.android.mms.MmsException;

/**
* The back-end data adapter of a message list.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageListItem.java b/src/com/android/mms/ui/MessageListItem.java
//Synthetic comment -- index c14bdb8..aeb1473 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
//Synthetic comment -- @@ -61,7 +60,6 @@
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mms.MmsApp;
import com.android.mms.R;
import com.android.mms.data.Contact;








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageUtils.java b/src/com/android/mms/ui/MessageUtils.java
//Synthetic comment -- index a8ef605..5d6afb7 100644

//Synthetic comment -- @@ -17,10 +17,44 @@

package com.android.mms.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.media.CamcorderProfile;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Sms;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.style.URLSpan;
import android.util.Log;
import android.widget.Toast;

import com.android.mms.LogTag;
import com.android.mms.MmsApp;
import com.android.mms.MmsConfig;
import com.android.mms.R;
import com.android.mms.TempFileProvider;
import com.android.mms.data.WorkingMessage;
import com.android.mms.model.MediaModel;
//Synthetic comment -- @@ -40,40 +74,6 @@
import com.google.android.mms.pdu.PduPersister;
import com.google.android.mms.pdu.RetrieveConf;
import com.google.android.mms.pdu.SendReq;

/**
* An utility class for managing messages.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessagingPreferenceActivity.java b/src/com/android/mms/ui/MessagingPreferenceActivity.java
//Synthetic comment -- index a2f31b6..383ff82 100755

//Synthetic comment -- @@ -17,10 +17,6 @@

package com.android.mms.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
//Synthetic comment -- @@ -32,15 +28,18 @@
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuItem;

import com.android.mms.MmsApp;
import com.android.mms.MmsConfig;
import com.android.mms.R;
import com.android.mms.util.Recycler;

/**








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MiniPreferenceActivity.java b/src/com/android/mms/ui/MiniPreferenceActivity.java
//Synthetic comment -- index a78c5e4..e3e071c 100644

//Synthetic comment -- @@ -5,15 +5,12 @@

package com.android.mms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.mms.R;

/**
* This activity is used by 3rd party apps to allow the user to turn on/off notifications in








//Synthetic comment -- diff --git a/src/com/android/mms/ui/MmsThumbnailPresenter.java b/src/com/android/mms/ui/MmsThumbnailPresenter.java
//Synthetic comment -- index e283012..abcec2f 100644

//Synthetic comment -- @@ -18,11 +18,7 @@
package com.android.mms.ui;

import android.content.Context;

import com.android.mms.model.AudioModel;
import com.android.mms.model.ImageModel;
import com.android.mms.model.Model;
//Synthetic comment -- @@ -30,7 +26,6 @@
import com.android.mms.model.SlideshowModel;
import com.android.mms.model.VideoModel;
import com.android.mms.util.ItemLoadedCallback;
import com.android.mms.util.ThumbnailManager.ImageLoaded;

public class MmsThumbnailPresenter extends Presenter {








//Synthetic comment -- diff --git a/src/com/android/mms/ui/NumberPickerDialog.java b/src/com/android/mms/ui/NumberPickerDialog.java
//Synthetic comment -- index ed2012d..22118d2 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;








//Synthetic comment -- diff --git a/src/com/android/mms/ui/PresenterFactory.java b/src/com/android/mms/ui/PresenterFactory.java
//Synthetic comment -- index dcd3c5c..3a7832b 100644

//Synthetic comment -- @@ -17,12 +17,12 @@

package com.android.mms.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;
import android.util.Log;

import com.android.mms.model.Model;

/**








//Synthetic comment -- diff --git a/src/com/android/mms/ui/RecipientsAdapter.java b/src/com/android/mms/ui/RecipientsAdapter.java
//Synthetic comment -- index 1950452..bafb410 100644

//Synthetic comment -- @@ -17,18 +17,14 @@

package com.android.mms.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.DataUsageFeedback;
import android.telephony.PhoneNumberUtils;
import android.text.Annotation;
//Synthetic comment -- @@ -39,6 +35,10 @@
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.android.mms.MmsApp;
import com.android.mms.R;
import com.android.mms.data.Contact;

/**
* This adapter is used to filter contacts on both name and number.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/ui/RecipientsEditor.java b/src/com/android/mms/ui/RecipientsEditor.java
//Synthetic comment -- index b1e748b..0baeff8 100644

//Synthetic comment -- @@ -17,10 +17,8 @@

package com.android.mms.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.provider.Telephony.Mms;
//Synthetic comment -- @@ -30,23 +28,23 @@
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.MultiAutoCompleteTextView;

import com.android.ex.chips.RecipientEditTextView;
import com.android.mms.MmsConfig;
import com.android.mms.data.Contact;
import com.android.mms.data.ContactList;

/**
* Provide UI for editing the recipients of multi-media messages.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SearchActivity.java b/src/com/android/mms/ui/SearchActivity.java
//Synthetic comment -- index 3ef7ccd..92e746b 100644

//Synthetic comment -- @@ -20,9 +20,6 @@
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
//Synthetic comment -- @@ -35,7 +32,6 @@
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.provider.Telephony;
import android.text.SpannableString;
import android.text.TextPaint;
//Synthetic comment -- @@ -49,8 +45,9 @@
import android.widget.ListView;
import android.widget.TextView;

import com.android.mms.MmsApp;
import com.android.mms.R;
import com.android.mms.data.Contact;

/***
* Presents a List of search results.  Each item in the list represents a thread which








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideEditorActivity.java b/src/com/android/mms/ui/SlideEditorActivity.java
//Synthetic comment -- index d431fd8..dbb5809 100644

//Synthetic comment -- @@ -17,28 +17,6 @@

package com.android.mms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
//Synthetic comment -- @@ -50,9 +28,9 @@
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
//Synthetic comment -- @@ -61,11 +39,29 @@
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.mms.ExceedMessageSizeException;
import com.android.mms.MmsApp;
import com.android.mms.MmsConfig;
import com.android.mms.R;
import com.android.mms.ResolutionException;
import com.android.mms.TempFileProvider;
import com.android.mms.UnsupportContentTypeException;
import com.android.mms.model.IModelChangedObserver;
import com.android.mms.model.LayoutModel;
import com.android.mms.model.Model;
import com.android.mms.model.SlideModel;
import com.android.mms.model.SlideshowModel;
import com.android.mms.ui.BasicSlideEditorView.OnTextChangedListener;
import com.android.mms.ui.MessageUtils.ResizeImageResultCallback;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.PduBody;
import com.google.android.mms.pdu.PduPart;
import com.google.android.mms.pdu.PduPersister;

/**
* This activity allows user to edit the contents of a slide.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideListItemView.java b/src/com/android/mms/ui/SlideListItemView.java
//Synthetic comment -- index bebb5d7..ae6b921 100644

//Synthetic comment -- @@ -17,7 +17,8 @@

package com.android.mms.ui;

import java.io.IOException;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
//Synthetic comment -- @@ -33,8 +34,7 @@
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mms.R;

/**
* A simplified view of slide in the slides list.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideView.java b/src/com/android/mms/ui/SlideView.java
//Synthetic comment -- index 41c6c9f..ec2a470 100644

//Synthetic comment -- @@ -17,8 +17,10 @@

package com.android.mms.ui;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.Bitmap;
//Synthetic comment -- @@ -40,10 +42,8 @@
import android.widget.TextView;
import android.widget.VideoView;

import com.android.mms.R;
import com.android.mms.layout.LayoutManager;

/**
* A basic view to show the contents of a slide.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideViewInterface.java b/src/com/android/mms/ui/SlideViewInterface.java
//Synthetic comment -- index c8e8df8..8ce9925 100644

//Synthetic comment -- @@ -17,11 +17,11 @@

package com.android.mms.ui;

import java.util.Map;

import android.graphics.Bitmap;
import android.net.Uri;

/**
* Defines the interfaces of the view to show contents of a slide.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowActivity.java b/src/com/android/mms/ui/SlideshowActivity.java
//Synthetic comment -- index c8e5180..c76b178 100644

//Synthetic comment -- @@ -17,16 +17,7 @@

package com.android.mms.ui;

import java.io.ByteArrayOutputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
//Synthetic comment -- @@ -47,12 +38,21 @@
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;

import com.android.mms.R;
import com.android.mms.dom.AttrImpl;
import com.android.mms.dom.smil.SmilDocumentImpl;
import com.android.mms.dom.smil.SmilPlayer;
import com.android.mms.dom.smil.parser.SmilXmlSerializer;
import com.android.mms.model.LayoutModel;
import com.android.mms.model.RegionModel;
import com.android.mms.model.SlideshowModel;
import com.android.mms.model.SmilHelper;
import com.google.android.mms.MmsException;

/**
* Plays the given slideshow in full-screen mode with a common controller.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowAttachmentView.java b/src/com/android/mms/ui/SlideshowAttachmentView.java
//Synthetic comment -- index 3394fd9..22dccef 100644

//Synthetic comment -- @@ -17,7 +17,8 @@

package com.android.mms.ui;

import java.io.IOException;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
//Synthetic comment -- @@ -31,8 +32,7 @@
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mms.R;

/**
* This class provides an embedded editor/viewer of slide-show attachment.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowEditActivity.java b/src/com/android/mms/ui/SlideshowEditActivity.java
//Synthetic comment -- index 77e35b1..187974e 100644

//Synthetic comment -- @@ -17,16 +17,6 @@

package com.android.mms.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
//Synthetic comment -- @@ -44,6 +34,15 @@
import android.widget.TextView;
import android.widget.Toast;

import com.android.mms.R;
import com.android.mms.model.IModelChangedObserver;
import com.android.mms.model.Model;
import com.android.mms.model.SlideModel;
import com.android.mms.model.SlideshowModel;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.PduBody;
import com.google.android.mms.pdu.PduPersister;

/**
* A list of slides which allows user to edit each item in it.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowEditor.java b/src/com/android/mms/ui/SlideshowEditor.java
//Synthetic comment -- index b17da18..3a18129 100644

//Synthetic comment -- @@ -17,8 +17,10 @@

package com.android.mms.ui;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.mms.model.AudioModel;
import com.android.mms.model.ImageModel;
import com.android.mms.model.RegionModel;
//Synthetic comment -- @@ -26,10 +28,8 @@
import com.android.mms.model.SlideshowModel;
import com.android.mms.model.TextModel;
import com.android.mms.model.VideoModel;
import com.google.android.mms.ContentType;
import com.google.android.mms.MmsException;

/**
* An utility to edit contents of a slide.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowPresenter.java b/src/com/android/mms/ui/SlideshowPresenter.java
//Synthetic comment -- index b0a4d56..64af07b 100644

//Synthetic comment -- @@ -17,12 +17,15 @@

package com.android.mms.ui;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.mms.model.AudioModel;
import com.android.mms.model.ImageModel;
import com.android.mms.model.LayoutModel;
import com.android.mms.model.MediaModel;
import com.android.mms.model.MediaModel.MediaAction;
import com.android.mms.model.Model;
import com.android.mms.model.RegionMediaModel;
import com.android.mms.model.RegionModel;
//Synthetic comment -- @@ -30,15 +33,9 @@
import com.android.mms.model.SlideshowModel;
import com.android.mms.model.TextModel;
import com.android.mms.model.VideoModel;
import com.android.mms.ui.AdaptableSlideViewInterface.OnSizeChangedListener;
import com.android.mms.util.ItemLoadedCallback;

/**
* A basic presenter of slides.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/ui/UriImage.java b/src/com/android/mms/ui/UriImage.java
//Synthetic comment -- index d9f158a..aa0c093 100644

//Synthetic comment -- @@ -17,18 +17,17 @@

package com.android.mms.ui;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.Telephony.Mms.Part;
//Synthetic comment -- @@ -36,11 +35,10 @@
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.android.mms.LogTag;
import com.android.mms.model.ImageModel;
import com.google.android.mms.ContentType;
import com.google.android.mms.pdu.PduPart;

public class UriImage {
private static final String TAG = "Mms/image";








//Synthetic comment -- diff --git a/src/com/android/mms/ui/VideoAttachmentView.java b/src/com/android/mms/ui/VideoAttachmentView.java
//Synthetic comment -- index befa1fe..1b16cf1 100644

//Synthetic comment -- @@ -17,20 +17,20 @@

package com.android.mms.ui;

import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.mms.R;
// TODO: remove dependency for SDK build

/**
* This class provides an embedded editor/viewer of video attachment.








//Synthetic comment -- diff --git a/src/com/android/mms/ui/WarnOfStorageLimitsActivity.java b/src/com/android/mms/ui/WarnOfStorageLimitsActivity.java
//Synthetic comment -- index 3e688be..9fd4993 100644

//Synthetic comment -- @@ -17,10 +17,6 @@

package com.android.mms.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
//Synthetic comment -- @@ -28,6 +24,9 @@
import android.os.Bundle;
import android.view.KeyEvent;

import com.android.internal.app.AlertController;
import com.android.mms.R;

/**
* This is the UI for telling the user about the storage limit setting.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/util/AddressUtils.java b/src/com/android/mms/util/AddressUtils.java
//Synthetic comment -- index 8d05467..35836c3 100644

//Synthetic comment -- @@ -16,22 +16,20 @@
*/
package com.android.mms.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Addr;
import android.text.TextUtils;

import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.mms.MmsApp;
import com.android.mms.R;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.PduHeaders;
import com.google.android.mms.pdu.PduPersister;

public class AddressUtils {
private static final String TAG = "AddressUtils";








//Synthetic comment -- diff --git a/src/com/android/mms/util/BlobCache.java b/src/com/android/mms/util/BlobCache.java
//Synthetic comment -- index f37bb37..cd89749 100644

//Synthetic comment -- @@ -65,8 +65,6 @@
//
package com.android.mms.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
//Synthetic comment -- @@ -76,6 +74,8 @@
import java.nio.channels.FileChannel;
import java.util.zip.Adler32;

import android.util.Log;

public class BlobCache implements Closeable {
private static final String TAG = "BlobCache";









//Synthetic comment -- diff --git a/src/com/android/mms/util/CacheManager.java b/src/com/android/mms/util/CacheManager.java
//Synthetic comment -- index 9198505..dd02b66 100644

//Synthetic comment -- @@ -16,15 +16,15 @@

package com.android.mms.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class CacheManager {
private static final String TAG = "CacheManager";
private static final String KEY_CACHE_UP_TO_DATE = "cache-up-to-date";








//Synthetic comment -- diff --git a/src/com/android/mms/util/DownloadManager.java b/src/com/android/mms/util/DownloadManager.java
//Synthetic comment -- index 0811af0..5210597 100644

//Synthetic comment -- @@ -17,17 +17,6 @@

package com.android.mms.util;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
//Synthetic comment -- @@ -36,15 +25,25 @@
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Telephony.Mms;
import android.telephony.ServiceState;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.TelephonyIntents;
import com.android.internal.telephony.TelephonyProperties;
import com.android.mms.R;
import com.android.mms.data.Contact;
import com.android.mms.ui.MessagingPreferenceActivity;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.NotificationInd;
import com.google.android.mms.pdu.PduPersister;

public class DownloadManager {
private static final String TAG = "DownloadManager";








//Synthetic comment -- diff --git a/src/com/android/mms/util/DraftCache.java b/src/com/android/mms/util/DraftCache.java
//Synthetic comment -- index 71d9ae0..bf36cd8 100644

//Synthetic comment -- @@ -16,18 +16,18 @@

package com.android.mms.util;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.provider.Telephony.MmsSms;
import android.provider.Telephony.Sms.Conversations;
import android.util.Log;

import com.android.mms.LogTag;

/**
* Cache for information about draft messages on conversations.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/util/ImageCacheService.java b/src/com/android/mms/util/ImageCacheService.java
//Synthetic comment -- index c589c49..9cd82f9 100644

//Synthetic comment -- @@ -16,11 +16,11 @@

package com.android.mms.util;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.content.Context;

public class ImageCacheService {
@SuppressWarnings("unused")
private static final String TAG = "ImageCacheService";








//Synthetic comment -- diff --git a/src/com/android/mms/util/PhoneNumberFormatter.java b/src/com/android/mms/util/PhoneNumberFormatter.java
//Synthetic comment -- index 3f981c5..ad52528 100644

//Synthetic comment -- @@ -16,13 +16,13 @@

package com.android.mms.util;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.TextView;

import com.android.mms.MmsApp;

public final class PhoneNumberFormatter {
private PhoneNumberFormatter() {}









//Synthetic comment -- diff --git a/src/com/android/mms/util/RateController.java b/src/com/android/mms/util/RateController.java
//Synthetic comment -- index a91ae98..13e90de 100644

//Synthetic comment -- @@ -17,14 +17,13 @@

package com.android.mms.util;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.provider.Telephony.Mms.Rate;
import android.util.Log;









//Synthetic comment -- diff --git a/src/com/android/mms/util/Recycler.java b/src/com/android/mms/util/Recycler.java
//Synthetic comment -- index 3066aab..b44d2eb 100644

//Synthetic comment -- @@ -16,16 +16,12 @@

package com.android.mms.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
//Synthetic comment -- @@ -35,6 +31,10 @@
import android.provider.Telephony.Sms.Conversations;
import android.util.Log;

import com.android.mms.MmsConfig;
import com.android.mms.ui.MessageUtils;
import com.android.mms.ui.MessagingPreferenceActivity;

/**
* The recycler is responsible for deleting old messages.
*/








//Synthetic comment -- diff --git a/src/com/android/mms/util/SendingProgressTokenManager.java b/src/com/android/mms/util/SendingProgressTokenManager.java
//Synthetic comment -- index 1709ae6..a10cf3f 100644

//Synthetic comment -- @@ -17,10 +17,10 @@

package com.android.mms.util;

import java.util.HashMap;

import android.util.Log;

public class SendingProgressTokenManager {
private static final String TAG = "SendingProgressTokenManager";
private static final boolean DEBUG = false;








//Synthetic comment -- diff --git a/src/com/android/mms/util/SmileyParser.java b/src/com/android/mms/util/SmileyParser.java
//Synthetic comment -- index 836fbb5..7a663d1 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.mms.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
//Synthetic comment -- @@ -23,10 +27,6 @@

import com.android.mms.R;

/**
* A class for annotating a CharSequence with spans to convert textual emoticons
* to graphical ones.








//Synthetic comment -- diff --git a/src/com/android/mms/util/ThumbnailManager.java b/src/com/android/mms/util/ThumbnailManager.java
//Synthetic comment -- index 3352296..f0ae7b5 100644

//Synthetic comment -- @@ -24,11 +24,11 @@

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
//Synthetic comment -- @@ -36,7 +36,6 @@
import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.TempFileProvider;
import com.android.mms.ui.UriImage;
import com.android.mms.util.ImageCacheService.ImageData;









//Synthetic comment -- diff --git a/src/com/android/mms/widget/MmsWidgetService.java b/src/com/android/mms/widget/MmsWidgetService.java
//Synthetic comment -- index cd922f8..bb01772 100644

//Synthetic comment -- @@ -17,47 +17,29 @@
package com.android.mms.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.Telephony.Threads;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.mms.LogTag;
import com.android.mms.R;
import com.android.mms.data.Contact;
import com.android.mms.data.Conversation;
import com.android.mms.ui.ConversationList;
import com.android.mms.ui.ConversationListItem;
import com.android.mms.ui.MessageUtils;
import com.android.mms.util.SmileyParser;

public class MmsWidgetService extends RemoteViewsService {
private static final String TAG = "MmsWidgetService";








