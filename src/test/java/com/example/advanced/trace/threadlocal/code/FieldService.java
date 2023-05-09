package com.example.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

/**
 * 동시성 문제 확인하기 위해 만든 Service 로직을 담은 클래스
 */
@Slf4j
public class FieldService {

    private String nameStore;

    // 필드에 저장 후 1초 후 조회하는 시나리오
    public String logic(String name){
        log.info("저장 name={} -> nameStore={}", name, nameStore);
        nameStore = name;
        sleep(1000);
        log.info("조회 nameStore={}", nameStore);
        return nameStore;
    }

    private void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
