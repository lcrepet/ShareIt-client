package fr.lyon.insa.ot.sims.shareit_client;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
 
public class ProfileActivity extends Activity {
 
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }
 
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu items for use in the action bar
    MenuInflater inflater = getMenuInflater();
 
    inflater.inflate(R.menu.activity_main, menu);
    return super.onCreateOptionsMenu(menu);
  }
 
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
 
    return super.onOptionsItemSelected(item);
  }
}