package com.gitlab.tulliocba.application.cache.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class CacheManager {
    private static final int FULL_CAPACITY = 10;
    private static Pair[] cache = new Pair[FULL_CAPACITY];

    private CacheManager() {
    }

    public static synchronized void put(String concatenatedNumber, int uniqueNumber) {
        if (isCacheFullyFilled(cache)) removeElementFromTail(cache);

        moveElements();

        putElementOnHead(concatenatedNumber, uniqueNumber);
    }

    private static void moveElements() {
        Pair[] newCache = new Pair[cache.length];

        for (int i = 0; i < cache.length; i++) newCache[(i + 1) % cache.length] = cache[i];

        cache = newCache;
    }

    private static void putElementOnHead(String concatenatedNumber, int uniqueNumber) {
        cache[0] = new Pair(concatenatedNumber, uniqueNumber);
    }

    private static void removeElementFromTail(Pair[] cache) {
        cache[FULL_CAPACITY - 1] = null;
    }

    private static boolean isCacheFullyFilled(Pair[] cache) {
        return nonNull(cache[FULL_CAPACITY - 1]);
    }

    public static Optional<Integer> get(String value) {
        final Optional<Pair> cachedElement = Arrays.asList(cache).stream()
                .filter(element -> nonNull(element) && value.equals(element.getKey()))
                .findFirst();

        if (cachedElement.isPresent()) return Optional.of(cachedElement.get().getValue());

        return Optional.empty();

    }

}

@Getter
@AllArgsConstructor
class Pair {
    private String key;
    private int value;
}
