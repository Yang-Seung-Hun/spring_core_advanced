package com.example.advanced.app.v2;

import com.example.advanced.trace.TraceStatus;
import com.example.advanced.trace.hellotrace.HelloTraceV1;
import com.example.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloTraceV2 로그 추적기 적용
 *
 * 문제
 * 1. HTTP 요청을 구분하고 깊이를 표현하기 위해서 TraceId 동기화가 필요함 -> TraceId의 동기화를 위해 관련 메서드의 모든 파라미터를 수정해야함
 * 2. 로그를 시작 할때는 begin()을, 처음이 아닐때는 beginSync()를 호출해야함 -> 만약 컨트롤러가 아닌 서비스부터 호출하는 상황이라면 파라미터로 넘길 TraceId가 없음
 * 3. 단순히 trace.begin() 및 trace.end() 코드 두 줄만 적용하면 될 것 같지만, trace.exception()으로 예외도 처리해야 하기 때문에 try catch를 사용해야 해서 지저분해짐
 * 4. 로그를 추적 할 로직이 한 두 곳이 아니라면, 모든 로직에 지저분한 코드를 적용해야 함
 *
 * HelloTraceV1 에서 만족 시키지 못한 요구사항 만족
 * 1. 메서드 호출의 깊이 표현 만족
 * 2. HTTP 요청을 구분 (HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분해야 함) 만족
 *
 */

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;
    private final HelloTraceV2 trace;

    @GetMapping("/v2/request")
    public String request(String itemId){

        TraceStatus status = null;
        try{
            status = trace.begin("OrderController.request()");
            orderService.orderItem(status.getTraceId(), itemId); // beginsync를 사용하기 위해 매번 traceId를 같이 넘겨 주어야 함
            trace.end(status);
            return "ok";
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }
}
