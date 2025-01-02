package com.arsen.pnu.coursework.dipattern.app.component;

import com.arsen.pnu.coursework.dipattern.library.annotation.Component;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Component
public class TestInjectBean {

    private final TestComponent testComponent;

}
