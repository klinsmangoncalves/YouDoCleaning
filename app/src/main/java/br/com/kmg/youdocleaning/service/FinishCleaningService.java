package br.com.kmg.youdocleaning.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import br.com.kmg.youdocleaning.CleaningWidgetProvider;
import br.com.kmg.youdocleaning.R;
import br.com.kmg.youdocleaning.database.FirebaseManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class FinishCleaningService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FINISH_CLEANING = "br.com.kmg.youdocleaning.service.action.finish_cleaning";
    private static final String ACTION_BAZ = "br.com.kmg.youdocleaning.service.action.BAZ";

    private static final String EXTRA_PARAM_USER = "br.com.kmg.youdocleaning.service.extra.user";


    public FinishCleaningService() {
        super("FinishCleaningService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFinishCleaning(Context context, String param1, String param2) {
        Intent intent = new Intent(context, FinishCleaningService.class);
        intent.setAction(ACTION_FINISH_CLEANING);
        intent.putExtra(EXTRA_PARAM_USER, param1);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1) {
        Intent intent = new Intent(context, FinishCleaningService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM_USER, param1);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FINISH_CLEANING.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM_USER);
                handleActionFinishCleaning(param1);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM_USER);
                handleActionBaz(param1);
            }
        }
    }

    private void handleActionFinishCleaning(String user) {
        FirebaseManager.getInstance().finishCleaning();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, CleaningWidgetProvider.class));
        Toast.makeText(this, getString(R.string.cleaning_finished_message), Toast.LENGTH_LONG).show();
        CleaningWidgetProvider.updateCleaningWidgets(this, appWidgetManager, appWidgetIds, null);
    }

    private void handleActionBaz(String param1) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
