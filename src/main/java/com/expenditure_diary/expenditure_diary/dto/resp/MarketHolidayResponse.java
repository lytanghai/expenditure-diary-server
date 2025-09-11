package com.expenditure_diary.expenditure_diary.dto.resp;

import java.util.List;

public class MarketHolidayResponse {
    private String exchange;
    private String timeZone;
    private List<Data> data;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        private String eventName;
        private String atDate;
        private String tradingHour;

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getAtDate() {
            return atDate;
        }

        public void setAtDate(String atDate) {
            this.atDate = atDate;
        }

        public String getTradingHour() {
            return tradingHour;
        }

        public void setTradingHour(String tradingHour) {
            this.tradingHour = tradingHour;
        }
    }
}