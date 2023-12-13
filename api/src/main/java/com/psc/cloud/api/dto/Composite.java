package com.psc.cloud.api.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Composite {
    private int productId;
    private String productName;
    private List<Recommend> recommendList;
    private List<Review> reviewList;
}
