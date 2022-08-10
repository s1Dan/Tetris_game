package com.s1dan.tetris_game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.s1dan.tetris.R


class MainActivity : AppCompatActivity() {

    var tvHighScore: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val btnNewGame = findViewById<Button>(R.id.btn_new_game)
        btnNewGame.setOnClickListener(this::onBtnNewGameClick)

        val btnResetScore = findViewById<Button>(R.id.btn_reset_game)
        btnResetScore.setOnClickListener(this::OnBtnResetScoreClick)

        val btnExit = findViewById<Button>(R.id.btn_exit)
        btnExit.setOnClickListener(this::onBtnExitClick)

        tvHighScore = findViewById(R.id.tv_high_score)

    }

//    private fun handleExitEvent(view: View) {
//        finish()
//    }

    private fun onBtnNewGameClick(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    private fun OnBtnResetScoreClick(view: View) {
        val preferences = AppPreferences(this)
        preferences.clearHighScore()
        Snackbar.make(view,"Score successfully reset", Snackbar.LENGTH_SHORT).show()

        tvHighScore?.text = "High score: ${preferences.getHighScore()}"
    }

    private fun onBtnExitClick(view: View) {
        System.exit(0)
    }
}

