package com.kitkaga.wht

enum class BoardSize(val numCards: Int) {
    EASY( 12);
    fun getWidth(): Int{
        return when(this){
            EASY -> 3
        }
    }
    fun getHeight(): Int {
        return numCards / getWidth()
    }
    fun getNumPairs(): Int{
        return numCards / 2;
    }
}