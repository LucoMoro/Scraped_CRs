/*Avoid recipient duplication in Messaging

If you try to send an SMS/MMS to a contact which has a comma in
their name field, the Messaging app will add a duplicate recipient
for each of the commas in their name.

The RecipientsEditor tokenizer only scanned directly for commas
and then looked at the spans overlapping with those regions. This
change will take into account the span end each time and adjust the
current index to be at the end of the span if necessary.*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/RecipientList.java b/src/com/android/mms/ui/RecipientList.java
//Synthetic comment -- index 12ee161..9020b65 100644

//Synthetic comment -- @@ -61,6 +61,7 @@
public String number;           // often filtered from 800-892-1212 to 8008921212
public String nameAndNumber;    // Fred Flintstone <670-782-1123>
public boolean bcc;

@Override
public String toString() {








//Synthetic comment -- diff --git a/src/com/android/mms/ui/RecipientsEditor.java b/src/com/android/mms/ui/RecipientsEditor.java
//Synthetic comment -- index ad4e928..9ec991a 100644

//Synthetic comment -- @@ -165,12 +165,18 @@
String bcc = getAnnotation(a, "bcc");
String number = getAnnotation(a, "number");

Recipient r = new Recipient();

r.name = name;
r.label = label;
r.bcc = bcc.equals("true");
r.number = TextUtils.isEmpty(number) ? TextUtils.substring(sp, start, end) : number;

if (TextUtils.isEmpty(r.name) && Mms.isEmailAddress(r.number)) {
ContactInfoCache cache = ContactInfoCache.getInstance();
//Synthetic comment -- @@ -249,6 +255,10 @@
Recipient r = getRecipientAt(sp, start, i, mContext);

rl.add(r);
}

i++;







