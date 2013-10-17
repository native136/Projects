package maceman.makersland.titlescreen;

import maceman.makersland.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.ApplicationErrorReport.CrashInfo;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// title screen buttons and listeners
		View btnNewWorld = findViewById(R.id.btnNewWorld);
		btnNewWorld.setOnClickListener(this);
		
		View btnLoadWorld = findViewById(R.id.btnLoadWorld);
		btnLoadWorld.setOnClickListener(this);
		
		View btnOptions = findViewById(R.id.btnOptions);
		btnOptions.setOnClickListener(this);
		
		View btnExit = findViewById(R.id.btnExit);
		btnExit.setOnClickListener(this);
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClick(){
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.btnNewWorld:
			startNewGame();
			break;
		case R.id.btnLoadWorld:
			loadGame();
			break;
		case R.id.btnOptions:
			openOptions();
			break;
		case R.id.btnExit:
			exit();
			break;
		
		}
	}


	// 
	private void startNewGame(){
		Intent intent = new Intent(MainActivity.this,NewGameActivity.class);
		startActivity(intent);
	}
	
	private void loadGame() {
		
	}

	private void openOptions() {
				
	}
	
	private void exit() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
