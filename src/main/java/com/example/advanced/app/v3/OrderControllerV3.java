package com.example.advanced.app.v3;

import com.example.advanced.trace.TraceStatus;
import com.example.advanced.trace.hellotrace.HelloTraceV2;
import com.example.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FieldLogTrace 로그 추적기 적용
 * 문제
 * 1. 동시성 문제 발생
 * (예시) 1초에 연속 2번 호출
 * [nio-8080-exec-1][de6d545c] OrderController.request()
 * [nio-8080-exec-1][de6d545c] |-->OrderService.orderItem()
 * [nio-8080-exec-1][de6d545c] |   |-->OrderRepository.save()
 * [nio-8080-exec-2][de6d545c] |   |   |-->OrderController.request()
 * [nio-8080-exec-2][de6d545c] |   |   |   |-->OrderService.orderItem()
 * [nio-8080-exec-2][de6d545c] |   |   |   |   |-->OrderRepository.save()
 * [nio-8080-exec-1][de6d545c] |   |<--OrderRepository.save() time=1000ms
 * [nio-8080-exec-1][de6d545c] |<--OrderService.orderItem() time=1001ms
 * [nio-8080-exec-1][de6d545c] OrderController.request() time=1003ms
 * [nio-8080-exec-2][de6d545c] |   |   |   |   |<--OrderRepository.save() time=1001ms
 * [nio-8080-exec-2][de6d545c] |   |   |   |<--OrderService.orderItem() time=1001ms
 * [nio-8080-exec-2][de6d545c] |   |   |<--OrderController.request() time=1001ms
 * -> 정상 결과는 트랜잭션 아이디 별로 모았을 때 정상적인 모양이 나와야함
 * -> 하지만 위 결과는 모든 트랜잭션 아이디가 같고, 스레드 풀을 기준으로 모아도 level이 맞지 않는 문제 발생함
 * 2. 단순히 trace.begin() 및 trace.end() 코드 두 줄만 적용하면 될 것 같지만, trace.exception()으로 예외도 처리해야 하기 때문에 try catch를 사용해야 해서 지저분해짐
 * 3. 로그를 추적 할 로직이 한 두 곳이 아니라면, 모든 로직에 지저분한 코드를 적용해야 함
 *
 * HelloTraceV2 에서 만족 시키지 못한 요구사항 만족
 * 1. 동기화를 위해 매번 파라미터로 TraceId를 넘기 지 않아도 됨
 */

@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {

    private final OrderServiceV3 orderService;
    private final LogTrace trace;

    @GetMapping("/v3/request")
    public String request(String itemId){

        TraceStatus status = null;
        try{
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status);
            return "ok";
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }
}
