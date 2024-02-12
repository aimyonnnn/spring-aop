package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(AtAnnotationTest.AtAnnotationAspect.class) // Aspect를 빈으로 등록
@SpringBootTest
public class AtAnnotationTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class AtAnnotationAspect {
        // hello.aop.member.annotation.MethodAop 어노테이션이
        // 붙은 모든 메소드에 대해 이 Aspect를 적용하겠다는 의미입니다.
        @Around("@annotation(hello.aop.member.annotation.MethodAop)")
        public Object doAtAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@annotation] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

/**
 * @MethodAop("test value") 어노테이션이 붙은 hello 메소드가 호출됩니다.
 * AspectJ의 @Around("@annotation(hello.aop.member.annotation.MethodAop") 어드바이스가 이 메소드를 가로챕니다.
 *
 * 이 어드바이스는 hello.aop.member.annotation.MethodAop 어노테이션이 붙은 모든 메소드에 적용됩니다.
 * doAtAnnotation(ProceedingJoinPoint joinPoint) 메소드가 실행됩니다.
 * 이 메소드는 @Around 어드바이스의 동작을 정의합니다.
 *
 * log.info("[@annotation] {}", joinPoint.getSignature()); 코드가 실행되어
 * 현재 실행 중인 메소드의 시그니처 정보를 로그로 출력합니다.
 *
 * joinPoint.proceed(); 코드가 실행되어 원래의 hello 메소드를 실행하고 그 결과를 반환합니다.
 * 이 반환값은 doAtAnnotation 메소드의 반환값으로 사용됩니다.
 *
 * 따라서, hello 메소드는 @Around 어드바이스에 의해 가로채져서 실행되며,
 * 이 과정에서 메소드의 시그니처 정보가 로그로 출력되고, 원래의 메소드가 실행됩니다.
 *
 * [ joinPoint.proceed() ]
 * joinPoint.proceed();는 AspectJ에서 제공하는 메소드로, 현재 실행 중인 메소드를 계속 실행하라는 명령입니다.
 * 이 메소드는 @Around 어드바이스에서 주로 사용되며, 이 메소드가 호출되면
 * 원래의 메소드(즉, 어드바이스가 적용된 메소드)가 실행됩니다.
 */






}
