package com.arsen.pnu.coursework.dipattern.app.component;

import com.arsen.pnu.coursework.dipattern.library.annotation.Component;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TestInjectBeanTwice {

    private final TestInjectBean testInjectBean;
    private final TestComponent testComponent;

}
