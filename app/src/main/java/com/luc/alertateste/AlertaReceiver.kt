package com.luc.alertateste

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings

class AlertaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("titulo") ?: "Alerta extremo"
        val texto = intent.getStringExtra("texto") ?: "Mensagem de teste"

        if (Settings.canDrawOverlays(context)) {
            // Permissão liberada: abre a tela vermelha direto
            context.startActivity(
                Intent(context, AlertaActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("titulo", titulo)
                    .putExtra("texto", texto)
            )
        } else {
            // Sem permissão: cai na notificação com tela cheia
            Notificacoes.mostrar(context, titulo, texto)
        }
    }
}
