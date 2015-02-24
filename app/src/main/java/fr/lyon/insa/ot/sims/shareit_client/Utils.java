package fr.lyon.insa.ot.sims.shareit_client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by Louise on 23/02/2015.
 */
public class Utils {

    public static void openOtherActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }
}
