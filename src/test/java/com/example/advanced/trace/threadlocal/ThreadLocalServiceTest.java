package com.example.advanced.trace.threadlocal;

import com.example.advanced.trace.threadlocal.code.FieldService;
import com.example.advanced.trace.threadlocal.code.ThreadLocalService;
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
public class ThreadLocalServiceTest {

    private ThreadLocalService service = new ThreadLocalService();

    @Test
    void fieldProblem(){
        log.info("main start");
        Runnable userA = () -> {
            service.logic("userA");
        };
        Runnable userB = () -> {
            service.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        sleep(100); // 일반 필드 사용시 동시성 문제 발생하지만 ThreadLocal 사용해 해결
        threadB.start();

        sleep(3000); // 메인 쓰레드 종료 대기
        log.info("main exit");

        /**
         * 결과 : 동시성 문제 발생하지 않음
         * [Test worker] main start
         * [thread-A] 저장 name=userA -> nameStore=null
         * [thread-B] 저장 name=userB -> nameStore=null
         * [thread-A] 조회 nameStore=userA
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
