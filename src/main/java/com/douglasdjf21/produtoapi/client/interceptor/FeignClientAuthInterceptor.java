package com.douglasdjf21.produtoapi.client.interceptor;


import com.douglasdjf21.produtoapi.util.RequestUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION = "Authorization";
    private static final String TRANSACTION_ID = "transactionid";

    @Override
    public void apply(RequestTemplate template) {
        var currentRequest = RequestUtil.getCurrentRequest();
        template
                .header(AUTHORIZATION, currentRequest.getHeader(AUTHORIZATION))
                .header(TRANSACTION_ID, currentRequest.getHeader(TRANSACTION_ID));
    }
}
