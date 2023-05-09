package com.example.advanced.trace.threadlocal;

import com.example.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 동시성 문제 확인하기 위한 테스트
 * 동시성 문제 :
 * - 여러 쓰레드가 동시에 같은 인스턴스의 필드 값을 변경하면서 발생하는 문제
 * - 트래픽이 점점 많아질수록 자주 발생
 * - 스프링 빈 처럼 싱글톤 객체의 필드를 변경하며 사용할 때 주의해야함
 *
 */
@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    @Test
    void field(){
        log.info("main start");
        Runnable userA = () -> {
            fieldService.logic("userA");
        };
        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        sleep(2000); // 동시성 문제 발생 X (thread-A의 실행이 끝나고 thread-B가 실행되도록)
        threadB.start();

        sleep(3000); // 메인 쓰레드 종료 대기
        log.info("main exit");

        /**
         * 결과 : 의도한 대로 문제 없이 결과 나옴
         * [Test worker] main start
         * [thread-A] 저장 name=userA -> nameStore=null
         * [thread-A] 조회 nameStore=userA
         * [thread-B] 저장 name=userB -> nameStore=userA
         * [thread-B] 조회 nameStore=userB
         * [Test worker] main exit
         */
    }

    @Test
    void fieldProblem(){
        log.info("main start");
        Runnable userA = () -> {
            fieldService.logic("userA");
        };
        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        sleep(100); // 동시성 문제 발생함 (thread-A의 실행(1초 걸림)이 끝나기 전 thread-B가 실행)
        threadB.start();

        sleep(3000); // 메인 쓰레드 종료 대기
        log.info("main exit");

        /**
         * 결과 : 동시성 문제 발생해 의도하지 않은 결과가 나옴
         * [Test worker] main start
         * [thread-A] 저장 name=userA -> nameStore=null // userA 가 nameStore에 userA를 저장함
         * [thread-B] 저장 name=userB -> nameStore=userA // userB는 nameStore에 userA를 userB로 덮어씀
         * [thread-A] 조회 nameStore=userB // userA는 userA가 조회되길 기대하지만 userB가 조회됨
         * [thread-B] 조회 nameStore=userB
         * [Test worker] main exit
         */
    }

    private void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
