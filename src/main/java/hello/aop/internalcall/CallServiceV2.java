package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * ObjectProvider<CallServiceV2>는 Spring Framework에서 제공하는 인터페이스로,
 * 특정 타입의 빈을 지연 로딩하는데 사용됩니다. 여기서 CallServiceV2는 요청하는 빈의 타입입니다.
 *
 * private final ObjectProvider<CallServiceV2> callServiceProvider;
 * 이 코드는 CallServiceV2 타입의 빈을 지연 로딩할 수 있는 ObjectProvider를 선언하는 것입니다.
 *
 * 이 ObjectProvider는 CallServiceV2의 인스턴스를 필요할 때까지 로딩을 지연시키고,
 * 필요한 시점에 getObject() 메소드를 통해 인스턴스를 가져옵니다.
 * 이렇게 하면 CallServiceV2의 생성 시점을 제어하고, 빈의 생명주기를 관리하는 데 도움이 됩니다.
 * 또한, 순환 참조와 같은 문제를 해결하는 데에도 사용될 수 있습니다.
 */

@Slf4j
@Component
public class CallServiceV2 {

    private final ObjectProvider<CallServiceV2> callServiceProvider;

    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceProvider) {
        this.callServiceProvider = callServiceProvider;
    }

    public void external() {
        log.info("call external");
        // `callServiceProvider.getObject()`를 호출하는 시점에 스프링 컨테이너에서 빈을 조회한다.
        CallServiceV2 callServiceV2 = callServiceProvider.getObject();
        callServiceV2.internal();
    }

    public void internal() {
        log.info("call internal");
    }

}