
//<Beginning of snippet n. 0>


*/
private static final Map<String, Provider> providersNames = new HashMap<String, Provider>(20);
static {
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

/**
* Returns a copy of the registered providers as an array.
*/
    public static synchronized Provider[] getProviders() {
        return providers.toArray(new Provider[providers.size()]);
}

/**
* Returns a copy of the registered providers as a list.
*/
    public static synchronized List<Provider> getProvidersList() {
        return new ArrayList<Provider>(providers);
}

/**
* Returns the provider with the specified name.
*/
    public static synchronized Provider getProvider(String name) {
if (name == null) {
return null;
}
        return providersNames.get(name);
}

/**
* Inserts a provider at a specified 1-based position.
*/
    public static synchronized int insertProviderAt(Provider provider, int position) {
        int size = providers.size();
        if ((position < 1) || (position > size)) {
            position = size + 1;
}
        providers.add(position - 1, provider);
        providersNames.put(provider.getName(), provider);
        setNeedRefresh();
        return position;
}

/**
* Removes the provider at the specified 1-based position.
*/
    public static synchronized void removeProvider(int providerNumber) {
        Provider p = providers.remove(providerNumber - 1);
        providersNames.remove(p.getName());
        setNeedRefresh();
}

/**
* Adds information about provider services into HashMap.
*/
    public static synchronized void initServiceInfo(Provider p) {
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
/**
* Returns true if services contain any provider information.
*/
    public static synchronized boolean isEmpty() {
        return services.isEmpty();
}

/**
* caches should be validated against the result of
* Service.getCacheVersion() before use.
*/
    public static synchronized Provider.Service getService(String key) {
        return services.get(key);
}

/**
* Returns the default SecureRandom service description.
*/
    public static synchronized Provider.Service getSecureRandomService() {
getCacheVersion();  // used for side effect of updating cache if needed
return cachedSecureRandomService;
}
* implementation to indicate that a provides list of services has
* changed.
*/
    public static synchronized void setNeedRefresh() {
        needRefresh = true;
}

/**
* Returns the current cache version. This has the possible side
* effect of updating the cache if needed.
*/
    public static synchronized int getCacheVersion() {
        if (needRefresh) {
            cacheVersion++;
            synchronized (services) {
                services.clear();
}
            cachedSecureRandomService = null;
            for (Provider p : providers) {
                initServiceInfo(p);
            }
            needRefresh = false;
}
        return cacheVersion;
}
}

//<End of snippet n. 0>








