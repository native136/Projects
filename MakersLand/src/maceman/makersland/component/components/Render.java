package maceman.makersland.component.components;

import android.graphics.Bitmap;
import maceman.makersland.component.Component;
import maceman.makersland.entity.Entity;

public class Render extends Component {

	public Bitmap bitmap;
	public String bitmapString;

	public Render(Entity owner, Bitmap bitmap, String bitmapString) {
		super(owner);
		this.bitmap = bitmap;
		this.bitmapString = bitmapString;
	}

}
