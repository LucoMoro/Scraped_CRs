//<Beginning of snippet n. 0>
public String number;           // often filtered from 800-892-1212 to 8008921212
public String nameAndNumber;    // Fred Flintstone <670-782-1123>
public boolean bcc;

@Override
public String toString() {
    return nameAndNumber + " " + number + (bcc ? " (BCC)" : "");
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

if (TextUtils.isEmpty(r.name) && Mms.isEmailAddress(r.number)) {
    r.name = ContactInfoCache.getInstance().getNameForNumber(r.number);
}

if (!TextUtils.isEmpty(r.name) && !TextUtils.isEmpty(r.number) && !recipientExists(r, rl)) {
    rl.add(r);
    i += 1; // Adjusted dynamic index management
}
//<End of snippet n. 1>

//<Beginning of helper methods>
private boolean recipientExists(Recipient r, List<Recipient> rl) {
    String normalizedName = r.name.replaceAll("\\s+", "").toLowerCase();
    String normalizedNumber = r.number.replaceAll("[^\\d]", "");
    for (Recipient existing : rl) {
        String existingNormalizedName = existing.name.replaceAll("\\s+", "").toLowerCase();
        String existingNormalizedNumber = existing.number.replaceAll("[^\\d]", "");
        if (existingNormalizedNumber.equals(normalizedNumber) && existingNormalizedName.equals(normalizedName)) {
            return true;
        }
    }
    return false;
}
//<End of helper methods>