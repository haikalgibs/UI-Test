package org.d3if1059.mobpro2.helloworld.widget

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class CovidWidgetService : IntentService(SERVICE_NAME) {
    companion object {
        private const val SERVICE_NAME = "CovidWidgetService"
        private const val PACKAGE_NAME = "org.d3if1059.mobpro2.helloworld "
        private const val ACTION_UPDATE_UI = "$PACKAGE_NAME.action.update_ui"
        fun startActionUpdateUI(context: Context) {
            val intent = Intent(context, CovidWidgetService::class.java)
            intent.action = ACTION_UPDATE_UI
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        val action = intent?.action ?: return
        if (action == ACTION_UPDATE_UI) {
            handleActionUpdateUI()
        }
    }

    private fun handleActionUpdateUI() {
        val widgetManager = AppWidgetManager.getInstance(this)
        val widgetIds = widgetManager.getAppWidgetIds(
            ComponentName(this, CovidWidgetProvider::class.java)
        )
        CovidWidgetProvider.updateAllWidget(this, widgetManager, widgetIds)
    }
}
