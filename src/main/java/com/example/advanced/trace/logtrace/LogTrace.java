package com.example.advanced.trace.logtrace;

import com.example.advanced.trace.TraceStatus;

/**
 * 로그 추적기를 위한 최소한의 기능 정의
 * 이를 이용해서 파라미터를 넘기지 않고 TraceId를 동기화 할 수 있는 로그추적기 구현
 */
public interface LogTrace {

    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);
}
