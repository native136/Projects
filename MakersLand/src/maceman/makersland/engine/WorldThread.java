package maceman.makersland.engine;

import android.graphics.Canvas;
import maceman.makersland.system.systems.MovementSystem;
import maceman.makersland.system.systems.RenderingSystem;
import maceman.makersland.world.WorldView;

public class WorldThread extends Thread{
	
	private WorldView mWorldView;
	private RenderingSystem mRenderingSystem;
	private MovementSystem mMovementSystem;
	
	public Boolean mRun = false;
	
	/*
	 * Constructor
	 * @param CellMap PanelView
	 */
	
	public WorldThread(WorldView worldView){
		mWorldView = worldView;
		
	}
	
	/*
	 * Thread loop
	 */
	public void run(){
		Canvas c;
		while(mRun){
			c = null;
			try{
				c = mWorldView.getHolder().lockCanvas(null);
				synchronized (mWorldView.getHolder()) {
					mWorldView.onDraw(c);
					
				}
			} finally{
				// If an exception is thrown, it won't crash the the surface
				if (c!=null){
					mWorldView.getHolder().unlockCanvasAndPost(c);
				}
			}
		}
	}
}
