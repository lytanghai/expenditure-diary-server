package com.expenditure_diary.expenditure_diary.dto.resp;

import java.math.BigDecimal;

public interface CurrencyTotalProjection {
    String getCurrency();
    BigDecimal getTotal();
}