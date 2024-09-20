package com.company.service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "REVIEW-SERVICE",  url = "${review-service.url}")
@FeignClient(name = "REVIEW-SERVICE")
public interface ReviewClients {

    @GetMapping("reviews/averageRating")
    Double getAverageRatingForCompany(@RequestParam("companyId") Long companyId);
}
