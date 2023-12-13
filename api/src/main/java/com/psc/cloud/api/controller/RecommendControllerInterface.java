package com.psc.cloud.api.controller;

import com.psc.cloud.api.dto.Recommend;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RecommendControllerInterface {

    @PostMapping(
            value    = "/recommend",
            consumes = "application/json",
            produces = "application/json"
    )
    Recommend createRecommend(@RequestBody Recommend body);

    @GetMapping(
            value = "/recommend",
            produces = "application/json"
    )
    List<Recommend> getRecommends(@RequestParam(value = "productId", required = true) int productId);

    @DeleteMapping(value = "/recommend")
    void deleteRecommends(@RequestParam(value = "productId", required = true)  int productId);
}
