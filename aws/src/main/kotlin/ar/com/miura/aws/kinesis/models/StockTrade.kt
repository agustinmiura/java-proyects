package ar.com.miura.aws.kinesis.models

data class StockTrade(
    val tickerSymbol:String,
    val tradeType:String,
    val price:Double,
    val quantity:Double,
    val id:String
) {

    /**
     * TODO : Complete
     */
    public fun asString():String {
        return "string";
    }
}
