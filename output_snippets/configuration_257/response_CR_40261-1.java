//<Beginning of snippet n. 0>
private static final ConcurrentHashMap<String, Provider> providersNames = new ConcurrentHashMap<>(20);
private static final CopyOnWriteArraySet<Provider> providers = new CopyOnWriteArraySet<>();
private static final ConcurrentHashMap<String, Provider.Service> services = new ConcurrentHashMap<>();
private static volatile Provider.Service cachedSecureRandomService;
private static volatile boolean needRefresh;
private static int cacheVersion;

static {
    loadProviders();
}

private static void loadProviders() {
    String providerClassName;
    int i = 1;
    ClassLoader cl = ClassLoader.getSystemClassLoader();
    
    while ((providerClassName = Security.getProperty("security.provider." + i++)) != null) {
        if (isValidProviderClass(providerClassName)) {
            try {
                Class<?> providerClass = Class.forName(providerClassName.trim(), true, cl);
                Provider p = (Provider) providerClass.getDeclaredConstructor().newInstance();
                insertProvider(p);
            } catch (Exception e) {
                Logger.getLogger("ProviderLoader").log(Level.SEVERE, "Failed to load provider: " + providerClassName, e);
            }
        }
    }
    Engine.door.renumProviders();
}

private static boolean isValidProviderClass(String providerClassName) {
    String[] validProviders = { "com.example.ProviderA", "com.example.ProviderB" };
    return Arrays.asList(validProviders).contains(providerClassName);
}

private static void insertProvider(Provider provider) {
    if (provider == null || providersNames.containsKey(provider.getName())) {
        return; 
    }
    synchronized (providers) {
        providers.add(provider);
        providersNames.put(provider.getName(), provider);
        initServiceInfo(provider);
        setNeedRefresh();
    }
}

public static Provider[] getProviders() {
    return providers.toArray(new Provider[0]);
}

public static List<Provider> getProvidersList() {
    return new ArrayList<>(providers);
}

public static Provider getProvider(String name) {
    if (name == null) {
        return null;
    }
    return providersNames.get(name);
}

public static int insertProviderAt(Provider provider, int position) {
    if (provider == null || providersNames.containsKey(provider.getName())) {
        return -1; 
    }
    synchronized (providers) {
        int size = providers.size();
        if ((position < 1) || (position > size + 1)) {
            position = size + 1;
        }
        List<Provider> providerList = new ArrayList<>(providers);
        providerList.add(position - 1, provider);
        providers.clear();
        providers.addAll(providerList);
        providersNames.put(provider.getName(), provider);
        setNeedRefresh();
        return position;
    }
}

public static void removeProvider(int providerNumber) {
    synchronized (providers) {
        if (providerNumber < 1 || providerNumber > providers.size()) return;
        Provider p = providers.remove(providerNumber - 1);
        providersNames.remove(p.getName());
        setNeedRefresh();
    }
}

public static void initServiceInfo(Provider p) {
    for (Provider.Service service : p.getServices()) {
        String type = service.getType();
        if (cachedSecureRandomService == null && "SecureRandom".equals(type)) {
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

public static boolean isEmpty() {
    return services.isEmpty();
}

public static Provider.Service getService(String key) {
    return services.get(key);
}

public static Provider.Service getSecureRandomService() {
    getCacheVersion();  
    return cachedSecureRandomService;
}

public static void setNeedRefresh() {
    needRefresh = true;
}

public static int getCacheVersion() {
    if (needRefresh) {
        cacheVersion++;
        services.clear();
        cachedSecureRandomService = null;
        for (Provider p : providers) {
            initServiceInfo(p);
        }
        needRefresh = false;
    }
    return cacheVersion;
}
//<End of snippet n. 0>