package com.arsen.pnu.coursework.dipattern.app.service;

import com.arsen.pnu.coursework.dipattern.library.annotation.Component;
import com.arsen.pnu.coursework.dipattern.library.context.ApplicationContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TestServiceConstructorInjection {

    private final ApplicationContext applicationContext;

}
