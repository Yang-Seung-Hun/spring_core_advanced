package com.example.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 전략 패턴
 *  - 템플릿 메서드 패턴과 달리 변하지 않는 부분을 템플릿이 아닌 Context라는 곳에 둠
 *  - 변하는 부분은 상속이 아닌 인터페이스를 구현하여 문제 해결
 *  -> 상속이 아닌 위임으로 문제 해결
 *
 * 방법1. 필드에 전략을 보관하는 방식으로 해결
 * - 변하지 않는 로직을 가지고 있는 템플릿 역할을 함. 전략 패턴에서는 컨텍스트라고 함
 * - 컨텍스트는 구현체가 아닌 인터페이스에 의존하기 때문에 인터페이스를 변경하거나 새로 만들어도 코드에 영향을 주지 않음
 * - 스프링에서도 의존관계 주입에서 전략 패턴을 사용함
 *
 * 템플릿 메서드 패턴 문제 해결
 * - 템플릿 메서드 패턴에서는 템플릿이(부모)가 바뀌면 자식 클래스에도 영향을 줬음
 * - 전략 패턴에서는 전략부분이 바뀌더라도 인터페이스에만 의존하기 때문에 Strategy 구현체(변하는 부분)에 영향을 주지 않음
 */
@Slf4j
public class ContextV1 {

    private Strategy strategy;

    public ContextV1(Strategy strategy){
        this.strategy = strategy;
    }

    public void execute(){
        long startTime = System.currentTimeMillis();

        strategy.call(); // 비즈니스로직(변하는 부분)

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);

    }

}
