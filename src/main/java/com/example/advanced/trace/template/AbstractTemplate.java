package com.example.advanced.trace.template;

import com.example.advanced.trace.TraceStatus;
import com.example.advanced.trace.logtrace.LogTrace;

/**
 * 템플릿 메서드 패턴에서 템플릿 역할 클래스(부모 클래스)
 * 제너릭을 사용해 타입 정의
 * 객체를 생성할 때 내부에서 사용할 LogTrace를 받음
 * 로그에 출력할 메시지를 외부에서 파라미터로 전달받음
 * 템플릿 중간에 call() 메서드를 통해서 변하는 부분을 처리
 */
public abstract class AbstractTemplate<T> {

    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace){
        this.trace = trace;
    }

    public T execute(String message){
        TraceStatus status = null;
        try{
            status = trace.begin(message);

            // 비즈니스 로직 호출
            T result = call();

            trace.end(status);
            return result;
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }

    protected abstract T call();
}
