package fr.lyon.insa.ot.sims.shareit_client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_home:
			// TODO : home
			return true;
		case R.id.menu_profile:
			openOtherActivity((View) findViewById(R.layout.activity_main), ProfileActivity.class);
			return true;
		case R.id.menu_borrow:
			openOtherActivity((View) findViewById(R.layout.activity_main), BorrowActivity.class);
			return true;
		case R.id.menu_email:
			openOtherActivity((View) findViewById(R.layout.activity_main), EmailActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void openOtherActivity(View view, Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

}