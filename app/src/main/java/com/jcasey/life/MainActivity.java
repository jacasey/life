package com.jcasey.life;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;




public class MainActivity extends Activity {

	private LifeSurfaceView lifeSurfaceView;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.settings) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),SettingsActivity.class);

			startActivity(intent);
		}
		else if(item.getItemId()==R.id.restart) {
			lifeSurfaceView = new LifeSurfaceView(getApplicationContext());
			setContentView(lifeSurfaceView);
		}
		else if(item.getItemId()==R.id.pause) {
			lifeSurfaceView.pause();
		}
		else if(item.getItemId()==R.id.play) {
			lifeSurfaceView.play();
		}
//		else if(item.getItemId()==R.id.about) {
//			Intent intent = new Intent();
//			intent.setClass(getApplicationContext(),AboutActivity.class);
//
//			startActivity(intent);
//		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lifeSurfaceView = new LifeSurfaceView(getApplicationContext());
		setContentView(lifeSurfaceView);


	}

	@Override
	protected void onResume() {
		super.onResume();

		lifeSurfaceView = new LifeSurfaceView(getApplicationContext());
		setContentView(lifeSurfaceView);
	}
}
