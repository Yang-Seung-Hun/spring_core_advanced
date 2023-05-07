package com.example.advanced.trace;

import java.util.UUID;

/**
 * 로그 추적기에 필요한 기반 데이터용 클래스
 * 로그 추적기는 같은 요청을 구별해주는 트랜잭션ID와 깊이를 표현하는  levle이 필요함
 */
public class TraceId {

    private String id; // 트랜잭션 id 표시
    private int level; // 로그 깊이 표시

    public TraceId() {
        this.id = createId();
        this.level = 0; // 기본 생성할 경우 로그 깊이는 0으로
    }

    private  TraceId(String id, int level){
        this.id = id;
        this.level = level;
    }

    /**
     * 트랜잭션 ID 생성해주는 메서드
     */
    private String createId() {
        return UUID.randomUUID().toString().substring(0,8); // 로그 트랜잭션 ID는 랜덤값으로 생성
    }

    /**
     * ID는 그대로고 깊이를 하나 올리는 메서드
     * 같은 요청일 경우 다음 깊이의 로그에 활용됨
     * (ex)
     * [abcde] | orderController()
     * [abcde] | -> orderService()
     * [abcde] | --> orderRequest()
     */
    public TraceId createNextId(){
        return new TraceId(id, level + 1);
    }

    /**
     * ID는 그대로고 깊이를 하나 내리는 메서드
     * 같은 요청일 경우 전 깊이의 로그에 활용됨
     * (ex)
     * [abcde] | <-- orderRequest()
     * [abcde] | <- orderService()
     * [abcde] | orderController()
     */
    public TraceId creatPreviousId(){
        return new TraceId(id, level - 1);
    }

    /**
     * 첫번째 깊이인지 확인하는 편의 메서드
     */
    public boolean isFirstLevel(){
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
