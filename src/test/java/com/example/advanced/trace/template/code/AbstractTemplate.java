package com.example.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

/**
 * TemplateMethod Pattern
 * - 템플릿 메서드 패턴은 이름 그대로 템플릿을 사용하는 방식(템플릿이라는 틀에 변하지 않는 부분을 몰아두고, 변하는 부분을 별도로 호출)
 * - 변하지 않는 부부은 추상 클래스에 집어넣고, 변하는 부분은 자식 클래스로 해결
 */
@Slf4j
public abstract class AbstractTemplate {

    public void execute(){
        long startTime = System.currentTimeMillis();

        call(); // 변하는 부분(비즈니스 로직)

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    protected abstract void call();

}
