package com.arsen.pnu.coursework.dipattern.library.context;

import com.arsen.pnu.coursework.dipattern.library.annotation.Component;
import com.arsen.pnu.coursework.dipattern.library.context.bean.InternalBean;
import com.arsen.pnu.coursework.dipattern.library.exception.BeanNotFoundException;
import com.arsen.pnu.coursework.dipattern.library.exception.DuplicateBeanException;
import com.arsen.pnu.coursework.dipattern.library.util.BeanUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

    private final Class<?> applicationClass;

    private final Map<String, Object> beansMap = new ConcurrentHashMap<>();
    private final Deque<InternalBean> beanInitOrder = new ConcurrentLinkedDeque<>();
    private final Deque<InternalBean> autowiredBeansInitOrder = new ConcurrentLinkedDeque<>();

    public ApplicationContext(Class<?> applicationClass) {
        this.applicationClass = applicationClass;
        scan();
    }

    public <T> T getBean(Class<T> type) {
        return (T) Optional.ofNullable(beansMap.get(type.getSimpleName()))
                .orElseGet(() -> beansMap.values()
                        .stream()
                        .filter(instance -> type.equals(instance.getClass()))
                        .findFirst()
                        .orElseThrow(() -> new BeanNotFoundException(type)));
    }

    public <T> T getBean(String beanName) {
        return (T) Optional.ofNullable(beanName)
                .orElseThrow(() -> new BeanNotFoundException(beanName));
    }

    private void scan() {
        String packageName = applicationClass.getPackageName();
        Reflections reflections = new Reflections(packageName);
        reflections.getTypesAnnotatedWith(Component.class)
                .forEach(this::addBeanToDeque);
        registerBeans();
    }

    private void registerBeans() {
        registerBean(getClass().getSimpleName(), this);
        InternalBean bean;
        while ((bean = beanInitOrder.poll()) != null) {
            registerBean(bean);
        }
        while((bean = autowiredBeansInitOrder.poll()) != null) {
            populateAutowiredFields(bean);
        }
    }

    @SneakyThrows
    private void registerBean(InternalBean bean) {
        if (beansMap.containsKey(bean.getName())) {
            throw new DuplicateBeanException(bean.getType(), bean.getName());
        }
        registerBean(bean.getName(), initBean(bean));
    }

    private void registerBean(String beanName, Object bean) {
        beansMap.put(beanName, bean);
        log.trace("Registered component {}", bean);
    }

    @SneakyThrows
    private Object initBean(InternalBean bean) {
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
        InternalBean bean = InternalBean.builder()
                .name(beanName)
                .type(type)
                .constructor(constructor)
                .autowiredTypesMap(BeanUtil.getAutowiredFieldTypesMap(type))
                .build();

        if (constructor.getParameterCount() == 0) {
            beanInitOrder.addFirst(bean);
            if(!bean.getAutowiredTypesMap().isEmpty()){
                autowiredBeansInitOrder.addLast(bean);
            }
            return;
        }
        beanInitOrder.addLast(bean);
    }

    private void populateAutowiredFields(InternalBean bean) {
        if (bean.getAutowiredTypesMap().isEmpty()) {
            return;
        }
        Object instance = beansMap.get(bean.getName());
        Map<Field, Class<?>> fieldMap = bean.getAutowiredTypesMap();
        fieldMap.entrySet().removeIf(entry -> {
            Object dependency = getInternalBean(entry.getValue());
            if (Objects.isNull(dependency)) {
                return false;
            }
            try {
                entry.getKey().set(instance, dependency);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return true;
        });
        if(!fieldMap.isEmpty()) {
            autowiredBeansInitOrder.addLast(bean);
        }
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
}
