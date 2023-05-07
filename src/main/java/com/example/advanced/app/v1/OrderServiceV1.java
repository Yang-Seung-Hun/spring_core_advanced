package com.example.advanced.app.v1;

import com.example.advanced.trace.TraceStatus;
import com.example.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {

    private final OrderRepositoryV1 orderRepository;
    private final HelloTraceV1 trace;

    public void orderItem(String itemId){

        TraceStatus status = null;
        try{
            status = trace.begin("OrderService.orderItem()"); // 로그 시작
            orderRepository.save(itemId);
            trace.end(status); // 정상 종료 시 로그 종료
        }catch (Exception e){
            trace.exception(status, e); // 예외 종료 시 로그 종료
            throw e; // 예외를 꼭 다시 던져주어야 함.
        }
    }
}
