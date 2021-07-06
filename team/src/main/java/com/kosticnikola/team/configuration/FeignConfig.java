package com.kosticnikola.team.configuration;

import com.kosticnikola.team.exception.InvalidIDException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    private class CustomErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() >= 400 && response.status() < 500)
                return new InvalidIDException();

            return new RuntimeException();
        }
    }
}
