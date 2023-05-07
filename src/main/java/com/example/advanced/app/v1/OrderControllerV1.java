package com.example.advanced.app.v1;

import com.example.advanced.trace.TraceStatus;
import com.example.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloTraceV1 로그 추적기 적용
 *
 * 문제
 * 1. 단순히 trace.begin() 및 trace.end() 코드 두 줄만 적용하면 될 것 같지만, trace.exception()으로 예외도 처리해야 하기 때문에 try catch를 사용해야 해서 지저분해짐
 * 2. 로그를 추적 할 로직이 한 두 곳이 아니라면, 모든 로직에 지저분한 코드를 적용해야 함
 *
 * 만족 시키지 못한 요구 사항
 * 1. 메서드 호출의 깊이 표현
 * 2. HTTP 요청을 구분 (HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분해야 함)
 * (ex) 현재 상황
 * [6547d02b] OrderController.request()
 * [8b3737d4] OrderService.orderItem()
 * [57adf778] OrderRepository.save()
 * [57adf778] OrderRepository.save() time=1009ms
 * [8b3737d4] OrderService.orderItem() time=1009ms
 * [6547d02b] OrderController.request() time=1014ms
 * [19f41dfd] OrderController.request()
 * [b5741601] OrderService.orderItem()
 * [25287f37] OrderRepository.save()
 * [25287f37] OrderRepository.save() time=0ms ex=java.lang.IllegalStateException: 예외 발생!
 * [b5741601] OrderService.orderItem() time=1ms ex=java.lang.IllegalStateException: 예외 발생!
 * [19f41dfd] OrderController.request() time=2ms ex=java.lang.IllegalStateException: 예외 발생!
 *
 */

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderServiceV1 orderService;
    private final HelloTraceV1 trace;

    @GetMapping("/v1/request")
    public String request(String itemId){

        TraceStatus status = null;
        try{
            status = trace.begin("OrderController.request()"); // 로그 시작
            orderService.orderItem(itemId);
            trace.end(status); // 정상 종료 시 로그 종료
            return "ok";
        }catch (Exception e){
            trace.exception(status, e); // 예외 종료 시 로그 종료
            throw e; // 예외를 꼭 다시 던져주어야 함.
        }
    }
}
