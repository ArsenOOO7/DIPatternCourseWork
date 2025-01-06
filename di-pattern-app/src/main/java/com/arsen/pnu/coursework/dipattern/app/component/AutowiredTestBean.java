package com.arsen.pnu.coursework.dipattern.app.component;

import com.arsen.pnu.coursework.dipattern.library.annotation.Autowired;
import com.arsen.pnu.coursework.dipattern.library.annotation.Component;
import lombok.Getter;

@Getter
@Component
public class AutowiredTestBean {

    @Autowired
    private TestComponent testComponent;
    @Autowired
    private TestInjectBean bean;

}
