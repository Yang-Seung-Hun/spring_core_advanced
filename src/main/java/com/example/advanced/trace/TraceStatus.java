package com.example.advanced.trace;

/**
 * 로그를 시작할 떄의 상태 정보를 가지고 있음
 * 상태 정보는 로그를 종료할 때 사용됨
 */
public class TraceStatus {

    private TraceId traceId;
    private Long startTimeMs; // 로그 시작 시간. 로그 종료 시 이 시작을 기준으로 시작~종료까지의 시간을 계산함
    private String message; // 시작시 사용한 메세지. 로그 종료시에도 해당 메시지 출력

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
