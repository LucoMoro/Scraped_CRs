/*Avoid ConcurrentModificationException on providers

Previously concurrently adding/removing providers in one thread and
doing algorithm lookups on another thread would cause
ConcurrentModificationExceptions such as:

java.util.ConcurrentModificationException
        at java.util.ArrayList$ArrayListIterator.next(ArrayList.java:569)
        at org.apache.harmony.security.fortress.Services.updateServiceInfo(Services.java:186)
        at org.apache.harmony.security.fortress.Services.refresh(Services.java:234)
        at org.apache.harmony.security.fortress.Engine.getInstance(Engine.java:137)
        at java.security.KeyFactory.getInstance(KeyFactory.java:81)

Also do some general cleanup and add similar protection on services.

Change-Id:I706c45655924dfccc3383fca57653d0c5b461721*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/security/SecureRandom.java b/luni/src/main/java/java/security/SecureRandom.java
//Synthetic comment -- index 68a2917..9091f73 100644

//Synthetic comment -- @@ -88,7 +88,6 @@
*/
public SecureRandom() {
super(0);
Provider.Service service = Services.getSecureRandomService();
if (service == null) {
this.provider = null;








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/fortress/Engine.java b/luni/src/main/java/org/apache/harmony/security/fortress/Engine.java
//Synthetic comment -- index 8a67ac2..b576d13 100644

//Synthetic comment -- @@ -134,7 +134,7 @@
if (algorithm == null) {
throw new NoSuchAlgorithmException("Null algorithm name");
}
        Services.flushCachesAndRefreshServicesIfNeeded();
Provider.Service service;
ServiceCacheEntry cacheEntry = this.serviceCache;
if (cacheEntry != null








//Synthetic comment -- diff --git a/luni/src/main/java/org/apache/harmony/security/fortress/Services.java b/luni/src/main/java/org/apache/harmony/security/fortress/Services.java
//Synthetic comment -- index d97e0f4..40dfe3b 100644

//Synthetic comment -- @@ -15,11 +15,6 @@
*  limitations under the License.
*/

package org.apache.harmony.security.fortress;

import java.security.Provider;
//Synthetic comment -- @@ -34,204 +29,210 @@
/**
* This class contains information about all registered providers and preferred
* implementations for all "serviceName.algName".
*/
public class Services {

    /**
     * The HashMap that contains information about preferred implementations for
     * all serviceName.algName in the registered providers.
     * Set the initial size to 600 so we don't grow to 1024 by default because
     * initialization adds a few entries more than the growth threshold.
     */
private static final Map<String, Provider.Service> services
= new HashMap<String, Provider.Service>(600);

/**
     * Save default SecureRandom service as well.
     * Avoids similar provider/services iteration in SecureRandom constructor.
*/
    private static Provider.Service cachedSecureRandomService;

    /**
     * Need refresh flag. Protected by synchronizing on providers.
     */
    private static boolean needRefresh;

    /**
     * The refreshNumber is changed on every update of service
     * information. It is used by external callers to validate their
     * own caches of Service information.
     */
    static volatile int refreshNumber = 1;

    /**
     * Registered providers.
     */
private static final List<Provider> providers = new ArrayList<Provider>(20);

    /**
     * Hash for quick provider access by name. Protected by synchronizing on providers.
     */
private static final Map<String, Provider> providersNames = new HashMap<String, Provider>(20);
static {
loadProviders();
}

private static void loadProviders() {
        synchronized (providers) {
            String providerClassName = null;
            int i = 1;
            ClassLoader cl = ClassLoader.getSystemClassLoader();

            while ((providerClassName = Security.getProperty("security.provider." + i++)) != null) {
                try {
                    Class providerClass = Class.forName(providerClassName.trim(), true, cl);
                    Provider p = (Provider) providerClass.newInstance();
                    providers.add(p);
                    providersNames.put(p.getName(), p);
                    initServiceInfo(p);
                } catch (ClassNotFoundException ignored) {
                } catch (IllegalAccessException ignored) {
                } catch (InstantiationException ignored) {
                }
}
            Engine.door.renumProviders();
}
}

/**
     * Returns a copy of the registered providers as an array.
*/
public static Provider[] getProviders() {
        synchronized (providers) {
            return providers.toArray(new Provider[providers.size()]);
        }
}

/**
     * Returns a copy of the registered providers as a list.
*/
public static List<Provider> getProvidersList() {
        synchronized (providers) {
            return new ArrayList<Provider>(providers);
        }
}

/**
     * Returns the provider with the specified name.
*/
public static Provider getProvider(String name) {
if (name == null) {
return null;
}
        synchronized (providers) {
            return providersNames.get(name);
        }
}

/**
     * Inserts a provider at a specified 1-based position.
*/
public static int insertProviderAt(Provider provider, int position) {
        synchronized (providers) {
            int size = providers.size();
            if ((position < 1) || (position > size)) {
                position = size + 1;
            }
            providers.add(position - 1, provider);
            providersNames.put(provider.getName(), provider);
            setNeedRefresh();
            return position;
}
}

/**
     * Removes the provider at the specified 1-based position.
*/
public static void removeProvider(int providerNumber) {
        synchronized (providers) {
            Provider p = providers.remove(providerNumber - 1);
            providersNames.remove(p.getName());
            setNeedRefresh();
        }
}

/**
* Adds information about provider services into HashMap.
*/
public static void initServiceInfo(Provider p) {
        synchronized (services) {
            for (Provider.Service service : p.getServices()) {
                String type = service.getType();
                if (cachedSecureRandomService == null && type.equals("SecureRandom")) {
                    cachedSecureRandomService = service;
                }
                String key = type + "." + service.getAlgorithm().toUpperCase(Locale.US);
if (!services.containsKey(key)) {
                    services.put(key, service);
                }
                for (String alias : Engine.door.getAliases(service)) {
                    key = type + "." + alias.toUpperCase(Locale.US);
                    if (!services.containsKey(key)) {
                        services.put(key, service);
                    }
}
}
}
}

/**
     * Returns true if services contain any provider information.
*/
public static boolean isEmpty() {
        synchronized (services) {
            return services.isEmpty();
        }
}

/**
     * Looks up the requested service. The service is described by a
     * key specified in the same TYPE.ALGORITHM format used when
     * registering a service with a provider.
*
     * Typically the caller should call
     * flushCachesAndRefreshServicesIfNeeded() and invalidate any of
     * their own caches against the refreshNumber before resorting to
     * calling this method.
*/
public static Provider.Service getService(String key) {
        synchronized (services) {
            return services.get(key);
        }
}

/**
* Returns the default SecureRandom service description.
*/
public static Provider.Service getSecureRandomService() {
        flushCachesAndRefreshServicesIfNeeded();
        return cachedSecureRandomService;
}

/**
     * In addition to being used here when the list of providers
     * changes, this method is also used by the Provider
     * implementation to indicate that a provides list of services has
     * changed.
*/
public static void setNeedRefresh() {
        synchronized (providers) {
            needRefresh = true;
        }
}

    public static void flushCachesAndRefreshServicesIfNeeded() {
        synchronized (providers) {
            if (needRefresh) {
                refreshNumber++;
                synchronized (services) {
                    services.clear();
                }
                cachedSecureRandomService = null;
                for (Provider p : providers) {
                    initServiceInfo(p);
                }
                needRefresh = false;
            }
}
}
}







