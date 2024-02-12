package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        // hello.aop.member 패키지 하위의 모든 메소드
        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember(){}

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{}, arg={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        @Before("allMember() && args(arg,..)")
        public void logArgs3(String arg) {
            log.info("[logArgs3] arg={}", arg);
        }

        // this - 프록시 호출
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this] {}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        // target - 프록시가 호출한 실제 대상을 호출(구현체)
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target] {}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        //@target(annotation)에서 annotation은 메소드 파라미터로 전달된 ClassAop annotation을 가리킵니다.
        //즉, 이 포인트컷은 ClassAop 어노테이션이 붙은 클래스에 속한 메소드를 대상으로 합니다.
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target] {}, obj={}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within] {}, obj={}", joinPoint.getSignature(), annotation);
        }

        // @annotation(annotation)에서 annotation은 메소드 파라미터로 전달된 MethodAop annotation을 가리킵니다.
        // 즉, 이 포인트컷은 MethodAop 어노테이션이 붙은 메소드를 대상으로 합니다.
        // 예를 들어, @Before("allMember() && @annotation(annotation)") 어드바이스는
        // allMember() 포인트컷에 매칭되는 메소드 중에서,
        // 그 메소드에 MethodAop 어노테이션이 붙어 있는 경우에만 실행됩니다.
        // 따라서, @annotation은 특정 어노테이션이 붙은 메소드를 대상으로 하는 어드바이스를 정의할 때 사용됩니다.
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation] {}, annotation={}", joinPoint.getSignature(), annotation.value());
        }

        /**
         * @target과 @annotation은 AspectJ의 포인트컷 디자인에이터로,
         * 어드바이스가 적용될 조인 포인트를 선택하는 데 사용됩니다.
         * 그러나 두 디자인에이터는 서로 다른 유형의 어노테이션을 대상으로 합니다.
         *
         * @target은 어노테이션이 붙은 타입(클래스 또는 인터페이스)에 매칭되는 조인 포인트를 선택합니다.
         * 즉, @target(ClassAop)은 ClassAop 어노테이션이 붙은 클래스에 속한 메소드를 대상으로 합니다.
         *
         * @annotation은 어노테이션이 붙은 메소드에 매칭되는 조인 포인트를 선택합니다.
         * 즉, @annotation(MethodAop)은 MethodAop 어노테이션이 붙은 메소드를 대상으로 합니다.
         *
         * @target은 특정 어노테이션이 붙은 클래스를 대상으로 하는 어드바이스를 정의할 때 사용되며,
         * @annotation은 특정 어노테이션이 붙은 메소드를 대상으로 하는 어드바이스를 정의할 때 사용됩니다.
         */








    }















































}
