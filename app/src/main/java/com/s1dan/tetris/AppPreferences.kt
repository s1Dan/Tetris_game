package com.s1dan.tetris_game

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(ctx: Context) {
    var data: SharedPreferences = ctx.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

    // Принимаем наибольшее количество очков (целое число)
    fun saveHighScore(highScore: Int) {
        data.edit().putInt("HIGH_SCORE", highScore).apply()
    }

    // Возращаем наибольшее количество очков при вызове данных
    fun getHighScore() : Int {
        return data.getInt("HIGH_SCORE",0)
    }

    // Сбрасываем значение наибольшего количества очков до 0, просто переписываем нулем значение
    fun clearHighScore() {
        data.edit().putInt("HIGH_SCORE",0).apply()
    }
}