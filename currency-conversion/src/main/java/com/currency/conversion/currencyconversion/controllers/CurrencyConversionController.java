package com.currency.conversion.currencyconversion.controllers;

import com.currency.conversion.currencyconversion.models.CurrencyConversion;
import com.currency.conversion.currencyconversion.proxy.CurrencyExchangeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    public CurrencyExchangeProxy currencyExchangeProxy;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion getCurrencyConversion(@PathVariable String from, @PathVariable String to,
                                                    @PathVariable BigDecimal quantity) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("from", from);
        hashMap.put("to", to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate()
                .getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                        CurrencyConversion.class, hashMap);

        CurrencyConversion currencyConversion = responseEntity.getBody();

        return new CurrencyConversion(currencyConversion.getId(), from, to,
                currencyConversion.getConversionMultiple(), quantity, quantity
                .multiply(currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment());
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion getCurrencyConversionFeign(@PathVariable String from, @PathVariable String to,
                                                    @PathVariable BigDecimal quantity) {

        CurrencyConversion currencyConversion = currencyExchangeProxy.getCurrencyExchange(from, to);

        return new CurrencyConversion(currencyConversion.getId(), from, to,
                currencyConversion.getConversionMultiple(), quantity, quantity
                .multiply(currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment());
    }
}
