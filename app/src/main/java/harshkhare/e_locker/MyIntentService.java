package harshkhare.e_locker;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Hi ! HARSH on 12-May-17.
 */

public class MyIntentService extends IntentService {
    private boolean intentRedelivert;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(MyIntentService.class.getName());
        setIntentRedelivert(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


    }

    public void setIntentRedelivert(boolean intentRedelivert) {
        this.intentRedelivert = intentRedelivert;
    }
}
