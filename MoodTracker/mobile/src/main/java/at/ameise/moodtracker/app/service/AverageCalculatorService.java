package at.ameise.moodtracker.app.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import at.ameise.moodtracker.app.TagConstant;
import at.ameise.moodtracker.app.domain.AverageCalculatorHelper;
import at.ameise.moodtracker.app.domain.MoodTableHelper;
import at.ameise.moodtracker.app.util.IntentUtil;
import at.ameise.moodtracker.app.util.Logger;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * Calculates the mood values for 4-value-per-day, daily, weekly and monthly averages.
 * <p/>
 * Created by Mario Gastegger <mario DOT gastegger AT gmail DOT com> on 04.04.15.
 */
public class AverageCalculatorService extends IntentService {

    private static final String ACTION_CALCULATE_AVERAGE = "at.ameise.moodtracker.app.service.action.CALCULATE_AVERAGE";

    /**
     * Contains an array of {@link at.ameise.moodtracker.app.domain.MoodTableHelper.EMoodScope#name()}s.
     */
    private static final String EXTRA_SCOPES = "at.ameise.moodtracker.app.service.extra.SCOPES";


    /**
     * Starts the service to calculate the average in the specified scopes
     * @param context the context
     * @param scopes the scopes in which the average is to be calculated
     */
    public static void startActionCalculateAverage(Context context, MoodTableHelper.EMoodScope[] scopes) {

        final Intent intent = new Intent(context, AverageCalculatorService.class);
        final String[] scopesStrings = new String[scopes.length];

        for(int i = 0; i < scopes.length; i++) {
            scopesStrings[i] = scopes[i].name();
        }

        intent.setAction(ACTION_CALCULATE_AVERAGE);

        intent.putExtra(EXTRA_SCOPES, scopesStrings);

        context.startService(intent);
    }


    public AverageCalculatorService() {
        super("AverageCalculatorService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (IntentUtil.hasAction(intent, ACTION_CALCULATE_AVERAGE)) {

            handleActionCalculateAverage(intent);
        }
    }

    /**
     * Handles the {@link at.ameise.moodtracker.app.service.AverageCalculatorService#ACTION_CALCULATE_AVERAGE}.
     *
     * @param intent the intent
     */
    private void handleActionCalculateAverage(Intent intent) {

        final String[] scopes = intent.getStringArrayExtra(EXTRA_SCOPES);

        /*
         * Calculate all requested averages in the given order.
         */
        for (String scope : scopes) {

            if(MoodTableHelper.EMoodScope.QUARTER_DAY.name().equals(scope)) {

                Logger.debug(TagConstant.AVERAGE_CALCULATOR, "Calculating quarter daily average value.");

                AverageCalculatorHelper.calculateQuarterDailyAverage(this);

            } else if(MoodTableHelper.EMoodScope.DAY.name().equals(scope)) {

                Logger.debug(TagConstant.AVERAGE_CALCULATOR, "Calculating daily average value.");

                AverageCalculatorHelper.calculateDailyAverage(this);

            } else if(MoodTableHelper.EMoodScope.WEEK.name().equals(scope)) {

                Logger.debug(TagConstant.AVERAGE_CALCULATOR, "Calculating weekly average value.");

                AverageCalculatorHelper.calculateWeeklyAverage(this);

            } else if(MoodTableHelper.EMoodScope.MONTH.name().equals(scope)) {

                Logger.debug(TagConstant.AVERAGE_CALCULATOR, "Calculating monthly average value.");

                AverageCalculatorHelper.calculateMonthlyAverage(this);
            }
        }
    }

}
