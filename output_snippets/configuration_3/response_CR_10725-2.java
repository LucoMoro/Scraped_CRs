//<Beginning of snippet n. 0>
public String number;           // often filtered from 800-892-1212 to 8008921212
public String nameAndNumber;    // Fred Flintstone <670-782-1123>
public boolean bcc;

@Override
public String toString() {
    return name + " <" + number + ">";
}

@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof Recipient)) return false;
    Recipient other = (Recipient) obj;
    return this.name.equals(other.name) && this.number.equals(other.number);
}

@Override
public int hashCode() {
    return Objects.hash(name, number);
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
String bcc = getAnnotation(a, "bcc");
String number = getAnnotation(a, "number");
Recipient r = new Recipient();

r.name = name;
r.label = label;
r.bcc = bcc.equals("true");
r.number = TextUtils.isEmpty(number) ? TextUtils.substring(sp, start, end) : number;

if (!TextUtils.isEmpty(r.name) && !TextUtils.isEmpty(r.number) && !rl.contains(r) && !Mms.isEmailAddress(r.number)) {
    ContactInfoCache cache = ContactInfoCache.getInstance();
    Recipient existingRecipient = getRecipientAt(sp, start, i, mContext);

    if (existingRecipient != null && !rl.contains(existingRecipient) && !TextUtils.isEmpty(existingRecipient.name) && !TextUtils.isEmpty(existingRecipient.number)) {
        rl.add(existingRecipient);
    }
    rl.add(r);
}

i += 1;
//<End of snippet n. 1>