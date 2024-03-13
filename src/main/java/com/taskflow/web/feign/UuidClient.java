package com.taskflow.web.feign;

import com.taskflow.domain.dto.response.feign.UuidResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "uuid-centralization")
public interface UuidClient {
    final String URL_PREFIX = "/api/v1";
    @GetMapping(URL_PREFIX + "/uuids/generate")
    UuidResponseDto generateUuid();
}
