package com.example.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 방법2. 전략을 파라미터로 전달
 * - 선 조립 후 실행 방식이 아니라 Context를 실행할 때 마다 전략을 인수로 전달
 * - 클라이언트는 Context 실행하는 시점에 원하는 전략을 전달 할 수 있음
 * - 비교적 유연하게 전략 변경 가능
 *
 */
@Slf4j
public class ContextV2 {

    public void execute(Strategy strategy){
        long startTime = System.currentTimeMillis();

        strategy.call(); // 비즈니스로직(변하는 부분)

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);

    }

}
