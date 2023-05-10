package com.example.advanced.app.v4;

import com.example.advanced.trace.TraceStatus;
import com.example.advanced.trace.logtrace.LogTrace;
import com.example.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TemplateMethod 패턴 적용
 * 해결 문제
 * 1. try catch 문을 제거하여 코드 간결화
 * 2. 핵심 로직과 부가 기능을 분리하여 관리
 * 3. 부가 기능 변경 있을 시 모든 클래스 변경 할 필요 없이 템플릿 부분마 변경하면 됨
 *
 * 좋은 설계란?
 * 변경이 일어날때 자연스럽게 드러남
 * 템플릿 메서드 패턴을 사용하면 로그 로직 부분을 변경해야 할때 추상 메서드 코드만 변경하면 됨
 *
 * 템플릿 메서드 패턴 문제
 * 상속에서 오는 단점을 그래로 갖음
 * - 자식 클래스 입장에서 부모 클래스의 기능을 전혀 사용하지 않지만 모든 기능을 상속받음
 * - 상속 받는 다는 것은 특정 부모 클래스를 강하게 의존한다는 뜻임
 * - 부모 클래스에 변화가 있을 경우 자식 클래스에 영향을 줌
 *
 */


@RestController
@RequiredArgsConstructor
public class OrderControllerV4 {

    private final OrderServiceV4 orderService;
    private final LogTrace trace;

    @GetMapping("/v4/request")
    public String request(String itemId){

        AbstractTemplate<String> template = new AbstractTemplate<>(trace){
            @Override
            protected String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        };
        return template.execute("OrderController.request()");
    }
}
