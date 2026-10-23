package com.luc.alertateste

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class AlertaActivity : ComponentActivity() {
    private var player: MediaPlayer? = null
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setShowWhenLocked(true)
        setTurnScreenOn(true)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val titulo = intent.getStringExtra("titulo") ?: "Alerta extremo"
        val texto = intent.getStringExtra("texto") ?: "Mensagem de teste"

        iniciarSomEVibracao()

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFB3261E))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("⚠", fontSize = 72.sp, color = Color.White)
                Spacer(Modifier.height(16.dp))
                Text(
                    titulo.uppercase(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Text(texto, fontSize = 18.sp, color = Color.White, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text("(teste de laboratório)", fontSize = 12.sp, color = Color(0xFFFFCDD2))
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = { finish() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFFB3261E))
                ) { Text("OK") }
            }
        }
    }

    private fun iniciarSomEVibracao() {
        val uri = Uri.parse("android.resource://$packageName/${R.raw.amber_alert}")
        player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            setDataSource(this@AlertaActivity, uri)
            isLooping = true
            prepare()
            start()
        }
        vibrator = getSystemService(Vibrator::class.java)
        vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 800, 400, 800, 400), 0))
    }

    override fun onDestroy() {
        player?.release()
        vibrator?.cancel()
        super.onDestroy()
    }
}
