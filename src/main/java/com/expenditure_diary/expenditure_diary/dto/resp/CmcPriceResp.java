package com.expenditure_diary.expenditure_diary.dto.resp;

public class CmcPriceResp {
    private Integer id;
    private String name;
    private String fromSymbol;
    private Quote quote;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromSymbol() {
        return fromSymbol;
    }

    public void setFromSymbol(String fromSymbol) {
        this.fromSymbol = fromSymbol;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public static class Quote {
        private String toSymbol;
        private Float price;
        private Float volume24Hour;
        private Float volumeChange24Hour;
        private Float percentChangeIn1Hour;
        private Float percentChangeIn24Hour;
        private Float percentChangeIn7Day;
        private Float percentChangeIn30Day;
        private Float percentChangeIn60Day;
        private Float percentChangeIn90Day;
        private String lastUpdated;

        public String getToSymbol() {
            return toSymbol;
        }

        public void setToSymbol(String tosSymbol) {
            this.toSymbol = tosSymbol;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public Float getVolume24Hour() {
            return volume24Hour;
        }

        public void setVolume24Hour(Float volume24Hour) {
            this.volume24Hour = volume24Hour;
        }

        public Float getVolumeChange24Hour() {
            return volumeChange24Hour;
        }

        public void setVolumeChange24Hour(Float volumeChange24Hour) {
            this.volumeChange24Hour = volumeChange24Hour;
        }

        public Float getPercentChangeIn1Hour() {
            return percentChangeIn1Hour;
        }

        public void setPercentChangeIn1Hour(Float percentChangeIn1Hour) {
            this.percentChangeIn1Hour = percentChangeIn1Hour;
        }

        public Float getPercentChangeIn24Hour() {
            return percentChangeIn24Hour;
        }

        public void setPercentChangeIn24Hour(Float percentChangeIn24Hour) {
            this.percentChangeIn24Hour = percentChangeIn24Hour;
        }

        public Float getPercentChangeIn7Day() {
            return percentChangeIn7Day;
        }

        public void setPercentChangeIn7Day(Float percentChangeIn7Day) {
            this.percentChangeIn7Day = percentChangeIn7Day;
        }

        public Float getPercentChangeIn30Day() {
            return percentChangeIn30Day;
        }

        public void setPercentChangeIn30Day(Float percentChangeIn30Day) {
            this.percentChangeIn30Day = percentChangeIn30Day;
        }

        public Float getPercentChangeIn60Day() {
            return percentChangeIn60Day;
        }

        public void setPercentChangeIn60Day(Float percentChangeIn60Day) {
            this.percentChangeIn60Day = percentChangeIn60Day;
        }

        public Float getPercentChangeIn90Day() {
            return percentChangeIn90Day;
        }

        public void setPercentChangeIn90Day(Float percentChangeIn90Day) {
            this.percentChangeIn90Day = percentChangeIn90Day;
        }

        public String getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }
    }
}
