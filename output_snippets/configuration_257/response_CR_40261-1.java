//<Beginning of snippet n. 0>
private static final Set<String> WHITELIST = Set.of("AllowedProvider1", "AllowedProvider2");
private static final Map<String, Provider> providersNames = new ConcurrentHashMap<>();
private static final List<Provider> providers = Collections.synchronizedList(new ArrayList<>());
private static final Map<String, Provider.Service> services = new ConcurrentHashMap<>();
private static Provider.Service cachedSecureRandomService;
private static int cacheVersion;
private static boolean needRefresh;

static {
    loadProviders();
}

private static void loadProviders() {
    String providerClassName;
    int i = 1;
    ClassLoader cl = ClassLoader.getSystemClassLoader();

    while ((providerClassName = Security.getProperty("security.provider." + i++)) != null) {
        if (!WHITELIST.contains(providerClassName.trim())) {
            continue;
        }
        try {
            Class<?> providerClass = Class.forName(providerClassName.trim(), true, cl);
            Provider p = (Provider) providerClass.getDeclaredConstructor().newInstance();
            providers.add(p);
            providersNames.put(p.getName(), p);
            initServiceInfo(p);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            System.err.println("Failed to load provider: " + providerClassName + ", Error: " + e.getMessage());
        }
    }
    Engine.door.renumProviders();
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

public static synchronized int insertProviderAt(Provider provider, int position) {
    int size = providers.size();
    if ((position < 1) || (position > size + 1)) {
        position = size + 1;
    }
    providers.add(position - 1, provider);
    providersNames.put(provider.getName(), provider);
    setNeedRefresh();
    return position;
}

public static synchronized void removeProvider(int providerNumber) {
    Provider p = providers.remove(providerNumber - 1);
    if (p != null) {
        providersNames.remove(p.getName());
        setNeedRefresh();
    }
}

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

public static boolean isEmpty() {
    return services.isEmpty();
}

public static Provider.Service getService(String key) {
    return services.get(key);
}

public static Provider.Service getSecureRandomService() {
    if (cachedSecureRandomService == null) {
        synchronized (getSecureRandomService.class) {
            if (cachedSecureRandomService == null) {
                getCacheVersion();
            }
        }
    }
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