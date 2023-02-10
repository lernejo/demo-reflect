package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class DataGenerator {

    private final Random random = new Random();

    public <T> T generateRandom(Class<T> clazz) {
        if (clazz.equals(String.class)) {
            return (T) UUID.randomUUID().toString();
        } else if (clazz.equals(Integer.class)) {
            return (T) Integer.valueOf(random.nextInt());
        } else if (canUseOneConstructor(clazz)) {
            return instantiate(getFirstUsableConstructor(clazz));
        } else {
            throw new IllegalArgumentException("Type not handled: " + clazz);
        }
    }

    private <T> T instantiate(Constructor<T> constructor) {
        Object[] parameters = Arrays.stream(constructor.getParameterTypes())
                .map(t -> generateRandom(t))
                .toArray();
        try {
            return constructor.newInstance(parameters);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> boolean canUseOneConstructor(Class<T> clazz) {
        return getFirstUsableConstructor(clazz) != null;
    }

    private <T> Constructor<T> getFirstUsableConstructor(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (isConstructorUsable(constructor)) {
                return (Constructor<T>) constructor;
            }
        }
        return null;
    }

    private boolean isConstructorUsable(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .allMatch(p -> isTypeSupported(p));
    }

    private boolean isTypeSupported(Class<?> clazz) {
        return clazz == String.class
                || clazz == Integer.class
                || canUseOneConstructor(clazz);
    }
}
