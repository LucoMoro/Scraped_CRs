/*Make HTTP Header class handle multiple cache-control fields.

The HTTP specification states the following about the fields:
Multiple message-header fields with the same field-name MAY be present
in a message if and only if the entire field-value for that header field
is defined as a comma-separated list [i.e., #(values)]. It MUST be
possible to combine the multiple header fields into one "field-name:
field-value" pair, without changing the semantics of the message, by
appending each subsequent field-value to the first, each separated by a
comma. The order in which header fields with the same field-name are
received is therefore significant to the interpretation of the combined
field value, and thus a proxy MUST NOT change the order of these field
values when a message is forwarded.

Change-Id:I1a6fe5cc8f541f8e80d559641d270d09eac9d85c*/
//Synthetic comment -- diff --git a/core/java/android/net/http/Headers.java b/core/java/android/net/http/Headers.java
//Synthetic comment -- index 09f6f4f..e989903 100644

//Synthetic comment -- @@ -262,7 +262,14 @@
break;
case HASH_CACHE_CONTROL:
if (name.equals(CACHE_CONTROL)) {
                mHeaders[IDX_CACHE_CONTROL] = val;
}
break;
case HASH_LAST_MODIFIED:








//Synthetic comment -- diff --git a/tests/CoreTests/android/core/HttpHeaderTest.java b/tests/CoreTests/android/core/HttpHeaderTest.java
new file mode 100644
//Synthetic comment -- index 0000000..a5d48578

//Synthetic comment -- @@ -0,0 +1,62 @@







