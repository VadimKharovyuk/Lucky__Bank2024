package com.example.web.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CurrencyRate {
    private String ccy;

    @JsonProperty("base_ccy")
    private String baseCcy;

    @JsonProperty("buy")
    private BigDecimal buyRate;

    @JsonProperty("sale")
    private BigDecimal saleRate;

}
