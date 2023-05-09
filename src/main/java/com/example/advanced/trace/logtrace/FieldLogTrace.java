package com.example.advanced.trace.logtrace;

import com.example.advanced.trace.TraceId;
import com.example.advanced.trace.TraceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HelloTraceV2와 거의 비슷 하지만 TraceId 동기화 방법이 다름
 * TraceId 동기화를 파라미터가 아닌 필드를 사용하여 시킴
 */
@Slf4j
public class FieldLogTrace implements LogTrace{

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private TraceId traceIdHolder; //traceId 동기화, 동시성 이슈 발생
    @Override
    public TraceStatus begin(String message) {

        syncTraceId();
        TraceId traceId = traceIdHolder;

        long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    /**
     * 로그 시작 시(begin 호출)TraceId 동기화
     * 시작 할 떄 traceId가 없다는 것은 새로운 트랜잭션을 생성해야 함
     * 시작 할 떄 traceId가 이미 있다는 것은 이미 진행 중인 트랜잭션이 있다는 것이므로 level을 하나 늘려 동기화 시켜야 함
     */
    private void syncTraceId(){
        if(traceIdHolder == null){
            traceIdHolder = new TraceId();
        }else {
            traceIdHolder = traceIdHolder.createNextId();
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();

        TraceId traceId = status.getTraceId();

        if (e == null) {log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);}
        else {log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());}

        releaseTraceId();
    }

    /**
     * 로그 종료 시(complete() 호출)TraceId 동기화
     * 종료 될 떄 level이 first라는 것은 destory 되야한다는 뜻
     * 종료 될 떄 level이 first가 아니라는 것은 level을 하나 줄여 동기화 시켜야 함
     */
    private void releaseTraceId(){
        if(traceIdHolder.isFirstLevel()){
            traceIdHolder = null;
        }else {
            traceIdHolder = traceIdHolder.creatPreviousId();
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }
}
