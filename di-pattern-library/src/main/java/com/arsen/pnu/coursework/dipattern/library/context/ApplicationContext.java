package com.arsen.pnu.coursework.dipattern.library.context;

import com.arsen.pnu.coursework.dipattern.library.annotation.Component;
import com.arsen.pnu.coursework.dipattern.library.context.bean.DequeuBean;
import com.arsen.pnu.coursework.dipattern.library.exception.BeanNotFoundException;
import com.arsen.pnu.coursework.dipattern.library.exception.DuplicateBeanException;
import com.arsen.pnu.coursework.dipattern.library.util.BeanUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
public class ApplicationContext {

    private static ApplicationContext INSTANCE;

    private final Class<?> applicationClass;

    private final Map<String, Object> beansMap = new ConcurrentHashMap<>();
    private final Deque<DequeuBean> beanInitOrder = new ConcurrentLinkedDeque<>();

    public ApplicationContext(Class<?> applicationClass) {
        this.applicationClass = applicationClass;
        INSTANCE = this; //want to cry about it
        scan();
    }

    private void scan() {
        String packageName = applicationClass.getPackageName();
        Reflections reflections = new Reflections(packageName);

        reflections.getTypesAnnotatedWith(Component.class)
                .forEach(this::addBeanToDeque);

        DequeuBean bean;
        while ((bean = beanInitOrder.poll()) != null) {
            registerComponent(bean);
        }
    }

    @SneakyThrows
    private void registerComponent(DequeuBean bean) {
        if (beansMap.containsKey(bean.getName())) {
            throw new DuplicateBeanException(bean.getType(), bean.getName());
        }
        Optional.ofNullable(initBean(bean))
                .ifPresentOrElse(createdBean -> {
                    beansMap.put(bean.getName(), createdBean);
                    log.trace("Registered component {}", bean.getName());
                }, () -> beanInitOrder.push(bean));
    }

    @SneakyThrows
    private Object initBean(DequeuBean bean) {
        Constructor<?> constructor = bean.getConstructor();
        if (constructor.getParameterCount() == 0) {
            return constructor.newInstance();
        }

        List<Class<?>> parameterTypes = BeanUtil.getParameterTypes(constructor);
        List<Object> args = new LinkedList<>();
        for (Class<?> parameterType : parameterTypes) {
            Object instance = getInternalBean(parameterType);
            if (Objects.isNull(instance)) {
                return null;
            }
            args.add(instance);
        }
        return bean.getConstructor().newInstance(args.toArray());
    }

    @SneakyThrows
    private void addBeanToDeque(Class<?> type) {
        Component annotation = type.getAnnotation(Component.class);
        String beanName = StringUtils.firstNonBlank(annotation.value(), type.getSimpleName());
        Constructor<?> constructor = BeanUtil.getConstructor(type);
        DequeuBean bean = DequeuBean.builder().name(beanName).type(type).constructor(constructor).build();

        if (constructor.getParameterCount() == 0) {
            beanInitOrder.addFirst(bean);
            return;
        }
        beanInitOrder.addLast(bean);
    }

    private Object getInternalBean(Class<?> type) {
        if (beansMap.containsKey(type.getSimpleName())) {
            return beansMap.get(type.getSimpleName());
        }
        return beansMap.values().stream()
                .filter(instance -> instance.getClass().equals(type))
                .findFirst()
                .orElse(null);
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
