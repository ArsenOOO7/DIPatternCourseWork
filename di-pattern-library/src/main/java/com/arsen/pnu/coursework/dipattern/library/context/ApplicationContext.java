package com.arsen.pnu.coursework.dipattern.library.context;

import com.arsen.pnu.coursework.dipattern.library.annotation.Component;
import com.arsen.pnu.coursework.dipattern.library.exception.BeanNotFoundException;
import com.arsen.pnu.coursework.dipattern.library.exception.DuplicateBeanException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ApplicationContext {

    private static ApplicationContext INSTANCE;

    private final Class<?> applicationClass;

    private final Map<String, Object> beansMap = new ConcurrentHashMap<>();

    public ApplicationContext(Class<?> applicationClass) {
        this.applicationClass = applicationClass;
        INSTANCE = this; //want to cry about it
        scan();
    }

    private void scan() {
        String packageName = applicationClass.getPackageName();
        Reflections reflections = new Reflections(packageName);

        reflections.getTypesAnnotatedWith(Component.class)
                .forEach(this::registerComponent);
    }

    @SneakyThrows
    private void registerComponent(Class<?> type) {
        Component annotation = type.getAnnotation(Component.class);
        String beanName = StringUtils.firstNonBlank(annotation.value(), type.getSimpleName());
        if (beansMap.containsKey(beanName)) {
            throw new DuplicateBeanException(type, beanName);
        }

        Object bean = type.getConstructor().newInstance();
        beansMap.put(beanName, bean);
        log.trace("Registered component {}", beanName);
    }

    public <T> T getBean(Class<T> type) {
        return (T) Optional.ofNullable(beansMap.get(type.getSimpleName()))
                .orElseGet(() -> beansMap.values()
                        .stream()
                        .filter(instance -> type.equals(instance.getClass()))
                        .findFirst()
                        .orElseThrow(() -> new BeanNotFoundException(type)));
    }

    public static ApplicationContext getInstance() {
        return INSTANCE;
    }
}
