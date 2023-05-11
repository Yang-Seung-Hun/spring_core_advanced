package com.example.advanced.trace.strategy.code.template;

import lombok.extern.slf4j.Slf4j;

/**
 * ContextV2는 변하지 않는 템플릿 역할을 하고, 변하는 부분은 파라미터로 넘어온 Strategy의 코드를 실행해서 처리함
 * 이렇게 다른 코드의 인수로서 넘겨주는 실행 가능한 코드를 콜백(callback)이라 함
 *
 * 템플릿 콜백 패턴
 * - 스프링에서는 ContextV2와 같은 방식의 전략 패턴을 템플릿 콜백 패턴이라고 함
 * - GOF 패턴은 아니고, 스프링 내부에서 이런 방식을 많이 사용해, 스프링에서 이렇게 부름
 * - JdbcTemplate, RestTemplate 등 XXXTemplate은 템플릿 콜백 패턴으로 만들어져 있다 생각하면 됨
 * - Context : 템플릿,   Strategy : Callback
 */
@Slf4j
public class TimeLogTemplate {

    public void execute(Callback callback){
        long startTime = System.currentTimeMillis();

        callback.call();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}
