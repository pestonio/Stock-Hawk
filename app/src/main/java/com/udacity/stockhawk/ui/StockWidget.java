package com.udacity.stockhawk.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;

/**
 * Implementation of App Widget functionality.
 */
public class StockWidget extends AppWidgetProvider {
    public static final String TOAST_ACTION = "com.udacity.stockhawk.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.udacity.stockhawk.EXTRA_ITEM";

//    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                        int appWidgetId) {
//
//        Intent appIntent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, appIntent, 0);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_listview);
//        views.setOnClickPendingIntent(R.id.widget_symbol, pendingIntent);
//        Intent listIntent = new Intent(context, StockWidgetService.class);
//        views.setRemoteAdapter(R.id.widget_list, listIntent);
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
//    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int i = 0; i < appWidgetIds.length; ++i) {
            Intent intent = new Intent(context, StockWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_listview);
            views.setRemoteAdapter(appWidgetIds[i], R.id.widget_list, intent);

            Intent toastIntent = new Intent(context, MainActivity.class);
            toastIntent.setAction(StockWidget.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}

