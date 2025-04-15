/*Fix SMTP RFC violation for better interoperability

Space after colon violates RFC 5321 (and RFC 821): "Since it has been a common
source of errors, it is worth noting that spaces are not permitted on either
side of the colon following FROM in the MAIL command or TO in the RCPT command"

Change-Id:Ie5330bf2bd01cd8f734134dadd742cf16df70d7aSigned-off-by: Jack Bates <jack@nottheoilrig.com>*/




//Synthetic comment -- diff --git a/src/com/android/email/mail/transport/SmtpSender.java b/src/com/android/email/mail/transport/SmtpSender.java
//Synthetic comment -- index 71a8630..8bc7396 100644

//Synthetic comment -- @@ -202,15 +202,15 @@
Address[] bcc = Address.unpack(message.mBcc);

try {
            executeSimpleCommand("MAIL FROM:" + "<" + from.getAddress() + ">");
for (Address address : to) {
                executeSimpleCommand("RCPT TO:" + "<" + address.getAddress() + ">");
}
for (Address address : cc) {
                executeSimpleCommand("RCPT TO:" + "<" + address.getAddress() + ">");
}
for (Address address : bcc) {
                executeSimpleCommand("RCPT TO:" + "<" + address.getAddress() + ">");
}
executeSimpleCommand("DATA");
// TODO byte stuffing








//Synthetic comment -- diff --git a/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java b/tests/src/com/android/email/mail/transport/SmtpSenderUnitTests.java
//Synthetic comment -- index 0f320b6..8e9ab75 100644

//Synthetic comment -- @@ -174,9 +174,9 @@
* Prepare to receive a simple message (see setupSimpleMessage)
*/
private void expectSimpleMessage(MockTransport mockTransport) {
        mockTransport.expect("MAIL FROM:<Jones@Registry.Org>",
"250 2.1.0 <Jones@Registry.Org> sender ok");
        mockTransport.expect("RCPT TO:<Smith@Registry.Org>",
"250 2.1.5 <Smith@Registry.Org> recipient ok");
mockTransport.expect("DATA", "354 enter mail, end with . on a line by itself");
mockTransport.expect("Date: .*");







