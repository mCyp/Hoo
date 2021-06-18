package com.joe.jetpackdemo.ui.provider

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.widget.RemoteViews
import androidx.navigation.NavDeepLinkBuilder
import com.joe.jetpackdemo.R

class DeepLinkAppWidgetProvider: AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val remoteView = RemoteViews(context.packageName,R.layout.deep_link_appwidget)

        val args = Bundle()
        args.putString("myarg","from widget")

        /*val pendIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.deeplink_dest)
            .setArguments(args)
            .createPendingIntent()*/

       /* remoteView.setOnClickPendingIntent(R.id.deep_link_button,pendIntent)
        appWidgetManager.updateAppWidget(appWidgetIds,remoteView)*/
    }
}