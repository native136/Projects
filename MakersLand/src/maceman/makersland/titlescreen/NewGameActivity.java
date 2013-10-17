package maceman.makersland.titlescreen;

import maceman.makersland.R;
import maceman.makersland.world.WorldView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

public class NewGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WorldView wv = new WorldView(this);
		setContentView(wv);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
