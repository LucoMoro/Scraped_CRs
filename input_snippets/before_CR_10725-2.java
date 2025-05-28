
//<Beginning of snippet n. 0>


public String number;           // often filtered from 800-892-1212 to 8008921212
public String nameAndNumber;    // Fred Flintstone <670-782-1123>
public boolean bcc;

@Override
public String toString() {

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


String bcc = getAnnotation(a, "bcc");
String number = getAnnotation(a, "number");

Recipient r = new Recipient();

r.name = name;
r.label = label;
r.bcc = bcc.equals("true");
r.number = TextUtils.isEmpty(number) ? TextUtils.substring(sp, start, end) : number;

if (TextUtils.isEmpty(r.name) && Mms.isEmailAddress(r.number)) {
ContactInfoCache cache = ContactInfoCache.getInstance();
Recipient r = getRecipientAt(sp, start, i, mContext);

rl.add(r);
}

i++;

//<End of snippet n. 1>








