package com.example.advanced;

import com.example.advanced.trace.logtrace.FieldLogTrace;
import com.example.advanced.trace.logtrace.LogTrace;
import com.example.advanced.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace(){

//        return new FieldLogTrace();

        /**
         * ThreadLocalTrace를 FieldLogTrace 대신 스프링 빈으로 등록
         * ThreadLocalTrace 로그 추적기 적용
         *
         * 문제
         * 1. 단순히 trace.begin() 및 trace.end() 코드 두 줄만 적용하면 될 것 같지만, trace.exception()으로 예외도 처리해야 하기 때문에 try catch를 사용해야 해서 지저분해짐
         * 2. 로그를 추적 할 로직이 한 두 곳이 아니라면, 모든 로직에 지저분한 코드를 적용해야 함
         *
         * HelloTraceV3 에서 발생한 동시성문제 해결
         * 1. 동시성 문제 해결
         * (예시) 1초에 연속 2번 호출
         * [nio-8080-exec-2] [2b5ecb18] OrderController.request()
         * [nio-8080-exec-1] [946f2806] OrderController.request()
         * [nio-8080-exec-1] [946f2806] |-->OrderService.orderItem()
         * [nio-8080-exec-2] [2b5ecb18] |-->OrderService.orderItem()
         * [nio-8080-exec-1] [946f2806] |   |-->OrderRepository.save()
         * [nio-8080-exec-2] [2b5ecb18] |   |-->OrderRepository.save()
         * [nio-8080-exec-1] [946f2806] |   |<--OrderRepository.save() time=1014ms
         * [nio-8080-exec-2] [2b5ecb18] |   |<--OrderRepository.save() time=1014ms
         * [nio-8080-exec-1] [946f2806] |<--OrderService.orderItem() time=1015ms
         * [nio-8080-exec-1] [946f2806] OrderController.request() time=1019ms
         * [nio-8080-exec-2] [2b5ecb18] |<--OrderService.orderItem() time=1015ms
         * [nio-8080-exec-2] [2b5ecb18] OrderController.request() time=1020ms
         * -> 트랜잭션을 기준으로 모아 보면 정상적인 결과를 확인할 수 있음
         *
         * ThreadLocal 사용시 주의점 :
         * UserA에게 할당된 threadA를 통해 UserA의 정보를 쓰레드 로컬로 저장 한 후 모든 요청이 끝나 threadA를 풀에 반환한 상황을 가정
         * 추후 UserB에게 threadA가 할당되면 UserB에게 UserA의 정보가 노출되는 문제가 발생함 (UserA 정보가 threadA 전용 보관소에 저장되어 있으니)
         * => UserA의 요청이 끝날때 ThreadLocal.remove()를 통해 꼭 제거해야함
         */
        return new ThreadLocalLogTrace();
    }
}
