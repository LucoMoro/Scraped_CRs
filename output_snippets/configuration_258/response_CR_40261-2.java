//<Beginning of snippet n. 0>


private static Provider.Service cachedSecureRandomService;

/**
     * Need refresh flag. Protected by synchronizing on providers.
*/
private static boolean needRefresh;

private static final List<Provider> providers = new CopyOnWriteArrayList<Provider>();

/**
     * Hash for quick provider access by name. Protected by synchronizing on providers.
*/
private static final Map<String, Provider> providersNames = new HashMap<String, Provider>(20);
private static final Set<String> safeClassNames = Set.of(
    "com.example.Provider1", // add safe class names here
    "com.example.Provider2"  // add safe class names here
);
private static final Map<String, Provider.Service> services = new HashMap<>();
private static int cacheVersion = 0;

static {
        loadProviders();
    }

private static void loadProviders() {
    synchronized (providers) {
        String providerClassName = null;
        int i = 1;
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        while ((providerClassName = Security.getProperty("security.provider." + i++)) != null) {
            if (!safeClassNames.contains(providerClassName.trim())) {
                continue;
            }
            try {
                Class<?> providerClass = Class.forName(providerClassName.trim(), true, cl);
                Provider p = (Provider) providerClass.getDeclaredConstructor().newInstance();
                providers.add(p);
                providersNames.put(p.getName(), p);
                initServiceInfo(p);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ignored) {
            }
        }
        Engine.door.renumProviders();
    }
}

/**
* Returns a copy of the registered providers as an array.
*/
public static Provider[] getProviders() {
    return providers.toArray(new Provider[0]);
}

/**
* Returns a copy of the registered providers as a list.
*/
public static List<Provider> getProvidersList() {
    return new ArrayList<>(providers);
}

/**
* Returns the provider with the specified name.
*/
public static Provider getProvider(String name) {
    if (name == null) {
        return null;
    }
    return providersNames.get(name);
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
    for (Provider.Service service : p.getServices()) {
        String type = service.getType();
        if (cachedSecureRandomService == null && type.equals("SecureRandom")) {
            cachedSecureRandomService = service;
        }
        String key = type + "." + service.getAlgorithm().toUpperCase(Locale.US);
        services.putIfAbsent(key, service);
        for (String alias : Engine.door.getAliases(service)) {
            key = type + "." + alias.toUpperCase(Locale.US);
            services.putIfAbsent(key, service);
        }
    }
}

/**
* Returns true if services contain any provider information.
*/
public static boolean isEmpty() {
    return services.isEmpty();
}

/**
* caches should be validated against the result of
* Service.getCacheVersion() before use.
*/
public static Provider.Service getService(String key) {
    return services.get(key);
}

/**
* Returns the default SecureRandom service description.
*/
public static Provider.Service getSecureRandomService() {
    getCacheVersion();  // used for side effect of updating cache if needed
    return cachedSecureRandomService;
}

/**
* implementation to indicate that a provides list of services has
* changed.
*/
public static void setNeedRefresh() {
    synchronized (providers) {
        needRefresh = true;
    }
}

/**
* Returns the current cache version. This has the possible side
* effect of updating the cache if needed.
*/
public static int getCacheVersion() {
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

//<End of snippet n. 0>