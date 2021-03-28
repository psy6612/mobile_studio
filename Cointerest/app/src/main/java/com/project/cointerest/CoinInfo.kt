package com.project.cointerest


class CoinInfo() {
    var trade_date: String? = null
    var trade_time: String? = null
    var trade_price: String? = null

    override fun toString(): String {
        return "CoinInfo{" +
                "trade_date='" + trade_date + '\'' +
                ", trade_time='" + trade_time + '\'' +
                ", trade_price='" + trade_price + '\'' +
                '}'
    }
}