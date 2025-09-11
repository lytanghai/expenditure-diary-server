package com.expenditure_diary.expenditure_diary.dto.resp;

import java.time.LocalDateTime;

public class ForexMarketPriceResponse {
    private String assetName;
    private Float price;
    private String symbol;
    private LocalDateTime updatedAt;
    private String updatedText;

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedText() {
        return updatedText;
    }

    public void setUpdatedText(String updatedText) {
        this.updatedText = updatedText;
    }
}
