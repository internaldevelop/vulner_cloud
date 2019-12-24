//package com.vulner.unify_auth.aspect;
//
//import com.vulner.common.response.ResponseBean;
//import com.vulner.common.response.ResponseHelper;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.stereotype.Component;
//
//@Component
//@Aspect
//public class AuthTokenAspect {
//    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
//    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
//        // 放行
////        Response response = new Response();
//        ResponseBean responseBean;
//        Object proceed = pjp.proceed();
//        if (proceed != null) {
//            ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>)proceed;
//            OAuth2AccessToken body = responseEntity.getBody();
//            if (responseEntity.getStatusCode().is2xxSuccessful()) {
//                responseBean = ResponseHelper.success(body);
//            }
//        }
//        return ResponseEntity
//                .status(200)
//                .body(responseBean);
//    }
//
//}
