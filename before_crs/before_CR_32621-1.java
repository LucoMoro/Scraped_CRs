/*remove the modifier 'final' of this class

This final class cannot be inherited. If we add the newest http-core's jar to the library,
we found SyncBasicHttpParams inherited from this class, which will cause warning:
"Superclass of 'Lorg/apache/http/params/SyncBasicHttpParams;' is final 'Lorg/apache/http/params/BasicHttpParams;', and fails to load SyncBasicHttpParams class. Dalvik load this final class instead of new-version non-final BasicHttpParams.
And in the newest httpcore 4.1.4, there's also no 'final' in this class. (http://hc.apache.org/httpcomponents-core-ga/httpcore/xref/org/apache/http/params/BasicHttpParams.html)

Change-Id:I052d2d99521c36fb12bd6b428d35d42b58e821f3Signed-off-by: Justin Yang <xjanker@gmail.com>*/
//Synthetic comment -- diff --git a/src/org/apache/http/params/BasicHttpParams.java b/src/org/apache/http/params/BasicHttpParams.java
//Synthetic comment -- index 70e6605..06f7db7 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
* 
* @version $Revision: 610464 $
*/
public final class BasicHttpParams extends AbstractHttpParams
implements Serializable, Cloneable {

private static final long serialVersionUID = -7086398485908701455L;







