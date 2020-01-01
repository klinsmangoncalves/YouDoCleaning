package br.com.kmg.youdocleaning.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import br.com.kmg.youdocleaning.R;
import br.com.kmg.youdocleaning.service.FinishCleaningService;

/**
 * Implementation of App Widget functionality.
 */
public class CleaningWidgetProvider extends AppWidgetProvider {

    public static final String WIDGET_INFO_EXTRA = "from_widget";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Long elapsedTime) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cleaning_widget_provider);

        if(elapsedTime != null){
            views.setViewVisibility(R.id.widget_cleaning_progress, View.VISIBLE);
            views.setChronometer(R.id.widget_chronometer, elapsedTime, "%s",  true );
        } else {
            views.setViewVisibility(R.id.widget_cleaning_progress, View.GONE);
        }

        Intent readQrCodeintent = new Intent(context, MainActivity.class);
        readQrCodeintent.putExtra(WIDGET_INFO_EXTRA, true);
        PendingIntent readQRCodePendingIntent = PendingIntent.getActivity(context, 0, readQrCodeintent, 0);

        Intent intentFinishCleaning = new Intent(context, FinishCleaningService.class);
        intentFinishCleaning.setAction(FinishCleaningService.ACTION_FINISH_CLEANING);
        PendingIntent finishCleaningPendingIntent = PendingIntent.getService(context, 0,
                intentFinishCleaning, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.iv_widget_qr_code, readQRCodePendingIntent);
        views.setOnClickPendingIntent(R.id.bt_widget_finish_cleaning, finishCleaningPendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        //no action required, the changes will triggered by app actions
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null);
        }
    }

    public static void updateCleaningWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Long elapsedTime){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, elapsedTime);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

