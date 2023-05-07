package com.example.advanced.trace.hellotrace;

import com.example.advanced.trace.TraceId;
import com.example.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 실제 로그를 시작 및 종료하고, 시간 측정
 */
@Slf4j
@Component
public class HelloTraceV1 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    /**
     * 로그 시작
     * (ex)
     * [abcde] orderController.request()
     */
    public TraceStatus begin(String message){
        TraceId traceId = new TraceId(); // 로그 시작 이니까 TraceId의 기본 생성자 (깊이 0으로 시작)
        long startTimeMs = System.currentTimeMillis();

        /*
        로그 찍는 부분
        트랜잭션 아이디, 깊이별 화살표 표시, 메시지 출력
        (ex)
        [transactionId] OrderController.request()
        [transactionId] |-->OrderService.orderItem() // 트랜잭션 아이디 유지, 깊이 하나 증가
         */
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTimeMs, message); // end() or exception() 호출 시 필요함
    }

    /**
     * 로그 종료(정상 종료)
     * (ex)
     * [abcde] orderController.request() time=1016ms
     */
    public void end(TraceStatus status){
        complete(status, null);
    }

    /**
     * 로그 종료(예외 발생 시)
     * (ex)
     */
    public void exception(TraceStatus status, Exception e){
        complete(status, e);
    }


    /**
     * 로그 정상 종료, 예외 종료 공통 로직 메서드 추출
     */
    private void complete(TraceStatus status, Exception e) {

        // 시간 계산
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();

        TraceId traceId = status.getTraceId();

        /*
        정상 종료 시 로그 출력
        트랜잭션 아이디, 깊이별 화살표 표시, 메시지, 소요 시간 출력
        (ex)
        [transactionId] |<--OrderService.orderItem()
        [transactionId] OrderController.request()
         */
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        }

        /*
        예외 발생 시 로그 출력
        트랜잭션 아이디, 깊이별 화살표 표시, 메시지, 소요 시간, 에러 메시지 출력
        (ex)
        [transactionId] |<X-OrderService.orderItem()
        [transactionId] OrderController.request()
         */
        else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }
    }

    /**
     * 깊이 및 로그 종료 타입 별 출력 모양 생성
     * level=0 : 없음
     * level=1 : |-->
     * level=2 : |   |-->
     */
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }


}
