package com.example.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

/**
 * ThreadLocal로 필드 동시성 문제 해결하는 Service 로직을 담은 클래스
 *
 * ThreadLocal :
 * - 해당 쓰레드만 접근할 수 있는 특별한 저장소를 의미
 * - 여러 사람이 같은 물건 보관창구를 사용하더라도 창구 직원은 사용자를 인식해서 사용자별로 확실하게 물건을 구분해주는 것에 비유
 *          -> 사용자A,B 모두 창구 직원을 통해서 물건을 보관하고,꺼내지만 창구 직원이 사용자에 따라 보관한 물건을 구분해줌
 *
 * ThreadLocal 사용법 :
 * - 값 저장 : ThreadLocal.set(value)
 * - 값 조회 : TreadLocal.get()
 * - 값 제거 : ThreadLocal.remove()
 */
@Slf4j
public class ThreadLocalService {

    private ThreadLocal<String> nameStore = new ThreadLocal<>(); // ThreadLocal 선언

    public String logic(String name){
        log.info("저장 name={} -> nameStore={}", name, nameStore.get());
        nameStore.set(name); // ThreadLocal 사용 - 데이터 저장
        sleep(1000);
        log.info("조회 nameStore={}", nameStore.get()); // ThreadLocal 사용 - 데이터 조회
        return nameStore.get();
    }

    private void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
