package maceman.makersland.world;

import java.util.Random;

import maceman.makersland.engine.WorldThread;
import maceman.makersland.world.tile.Tile;
import maceman.makersland.worldgen.WorldGenerator;
import maceman.makersland.worldgen.noise.NoiseGenerator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class WorldView extends SurfaceView implements SurfaceHolder.Callback {

	private Context mContext;
	private LruCache<String, Bitmap> mMemoryCache;

	// World

	private WorldThread mWorldThread;
	private WorldGenerator mapGenerator;
	private NoiseGenerator noiseGenerator;
	private Tile centerTile;

	private Tile[][] mTileMap;
	private Tile[] mRow;

	private Random r;

	Paint paint;
	private static int mTileSize = 40;
	private static int mMinTileSize = 10;
	private static int mMaxTileSize = 150;
	private static int mWorldSize = 300;
	private static int mBorderWidth = 0;
	private static int mOctaveCount = 6;

	// Upper left offset; "World Camera", starts centered
	private int mXOffSet = ((mTileSize * mWorldSize) / 2) - (getWidth() / 2);
	private int mYOffSet = ((mTileSize * mWorldSize) / 2) - (getHeight() / 2);

	
	// allows for smooth movement between the tiles. the map will not "Lock" onto the next tile and will show part of the next one.
	private int mXScrollOffSet = 0;
	private int mYScrollOffSet = 0;

	// Last Touch point
	private int mXTouch = 0;
	private int mYTouch = 0;

	// pinch value
	private float oldPinch = 0f;
	private float newPinch = 0f;

	// Edges
	private int mStartRow = 0;
	private int mMaxRow = 0;
	private int mStartColumn = 0;
	private int mMaxColumn = 0;

	// movement Checks
	private Boolean mIsMoving = false;
	private Boolean mIsZooming = false;

	/*
	 * reusable vars to be used when going through the rows and columns mX and
	 * mY designate the coordinates where to draw on screen mI and mJ are needed
	 * to draw the tiles as the draw methods for loops go through the cells that
	 * are on screen.
	 */
	private int mX = 0;
	private int mY = 0;
	private int mI = 0;
	private int mJ = 0;

	public WorldView(Context context) {
		super(context);

		int width = mWorldSize;
		int height = mWorldSize;
		mContext = context;

		noiseGenerator = new NoiseGenerator();
		mapGenerator = new WorldGenerator(noiseGenerator);
		r = new Random();

		int seed = r.nextInt();

		paint = new Paint();
		// Get max available VM memory, exceeding this amount will throw an
		// OutOfMemory exception. Stored in kilobytes as LruCache takes an
		// int in its constructor.
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getByteCount() / 1024;
			}
		};
		

		mTileMap = mapGenerator.GenerateNewWorld(width, height, mOctaveCount,
				mBorderWidth, seed);

		int id = 0;
		for (int y = 0; y < mWorldSize; y++) { // rows
			mRow = mTileMap[y];
			for (int x = 0; x < mWorldSize; x++) {// columns
				mTileMap[x][y].setTileID(id);
				id++;
			}
		}
		paint = new Paint(Color.WHITE);
		
		/*
		 *  Create necessary systems
		 */
//		entityManager = new EntityManager();
//		renderingSystem = new RenderingSystem(entityManager, mMemoryCache);
//		movementSystem = new MovementSystem(entityManager);
		
		
		
		// Register the view to Surfaceholder
		getHolder().addCallback(this);

		mWorldThread = new WorldThread(this);

		setFocusable(true);
		
	

	}

	public void onDraw(Canvas canvas) {
		// canvas.drawColor(Color.BLACK);
		// i = tile row; j = tile column;
		mI = 0;
		for (int i = mStartRow; i <= mMaxRow + 1; i++) {
			mJ = 0;
			// get row
			mY = mI * mTileSize;// - mYScrollOffSet; 
			for (int j = mStartColumn; j <= mMaxColumn + 1; j++) {
				mX = mJ * mTileSize;// - mXScrollOffSet;
				// don't go out of bounds
				if ((i >= 0) && (i < mWorldSize) && (j >= 0)
						&& (j < mWorldSize)) {
					mTileMap[i][j].draw(canvas, mX, mY, mTileSize, paint);

				}
				mJ++;
			}
			mI++;
		}

	}

	public void drawActors(Canvas canvas) {


	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!mWorldThread.isAlive()) {
			mWorldThread = new WorldThread(this);
		}

		mWorldThread.mRun = true;
		mWorldThread.start();
		findMapEnds();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		mWorldThread.mRun = false;

		while (retry) {
			try {
				mWorldThread.join();
				retry = false;
			} catch (InterruptedException e) {

			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!mIsZooming){
		centerTile = getCenterTile();
		}
		if (event.getPointerCount() == 1) {
			// touch down
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// new event
				mIsMoving = false;
				mIsZooming = false;
				

				// store coordinates
				mXTouch = (int) event.getX();
				mYTouch = (int) event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				mIsMoving = true;
				// new offset
				mXOffSet += mXTouch - (int) event.getX();
				mYOffSet += mYTouch - (int) event.getY();

				// make sure it never goes out of bounds
				if (mXOffSet < 0) {
					mXOffSet = 0;
				} else if (mXOffSet > mWorldSize * mTileSize - getWidth()) {
					mXOffSet = (mWorldSize * mTileSize) - getWidth();
				}
				if (mYOffSet < 0) {
					mYOffSet = 0;
				} else if (mYOffSet > mWorldSize * mTileSize - getHeight()) {
					mYOffSet = (mWorldSize * mTileSize) - getHeight();
				}

				mXTouch = (int) event.getX();
				mYTouch = (int) event.getY();

				findMapEnds();
				
				break;
			case MotionEvent.ACTION_UP:
				// touch released
				if (!mIsMoving) {
					int column = (int) Math.ceil((mXOffSet + event.getX())
							/ mTileSize) - 1;
					int row = (int) Math.ceil((mYOffSet + event.getY())
							/ mTileSize) - 1;
					Tile tile = mTileMap[row][column];

					Toast.makeText(getContext(),
							"Tile id #" + tile.getTileID(), Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				break;
			}
		} else if (event.getPointerCount() == 2) {
			mIsZooming = true;
			newPinch = (float) (Math
					.floor((Math.sqrt((Math.pow(event.getX(0), 2) - Math.pow(
							event.getX(1), 2))
							+ (Math.pow(event.getY(0), 2) - Math.pow(
									event.getY(1), 2))))));

			if (newPinch > oldPinch) {
				if (mTileSize < mMaxTileSize) {
					mTileSize++;
					oldPinch = newPinch;
				}

			} else if (newPinch < oldPinch) {
				if (mTileSize > mMinTileSize) {
					mTileSize--;
					oldPinch = newPinch;
				}
			}
			if (centerTile != null) {
				mXOffSet = (int) Math
						.floor(((centerTile.getX() * mTileSize) - (getWidth() / 2))
								+ (mTileSize / 2));
				mYOffSet = (int) Math
						.floor(((centerTile.getY() * mTileSize) - (getHeight() / 2))
								+ (mTileSize / 2));
				mStartRow = (int) Math.floor((mYOffSet / mTileSize));
				mStartColumn = (int) Math.floor(mXOffSet / mTileSize);
				mMaxRow = (int) Math.min(mWorldSize,
						mStartRow + Math.floor(getHeight()) / mTileSize);

				mMaxColumn = (int) Math.min(mWorldSize,
						mStartColumn + Math.floor(getWidth()) / mTileSize);

				findMapEnds();
			}

		}

		return true;
	}

	private void findMapEnds() {
		// finds the map ends.
/* 
 *  commented out to make movement more smooth will try to figure this out later. TODO
 */
//		mXScrollOffSet = (int) Math
//				.floor((((mXOffSet / (float) mTileSize) - (mXOffSet / mTileSize)) * mTileSize));
//		mYScrollOffSet = (int) Math
//				.floor((((mYOffSet / (float) mTileSize) - (mYOffSet / mTileSize)) * mTileSize));
		mStartRow = (int) Math.floor((mYOffSet / mTileSize));
		mMaxRow = (int) Math.min(mWorldSize,
				mStartRow + Math.floor(getHeight()) / mTileSize);

		mStartColumn = (int) Math.floor(mXOffSet / mTileSize);
		mMaxColumn = (int) Math.min(mWorldSize,
				mStartColumn + Math.floor(getWidth()) / mTileSize);

	}

	private Tile getCenterTile() {
		Tile ctile = null;

		Integer x = mStartColumn + ((getWidth() / 2) / mTileSize);
		Integer y = mStartRow + ((getHeight() / 2) / mTileSize);

		String s = (x.toString() + ", " + y.toString());
		Log.d("Ctile", s);
		if (x < mTileMap.length && x > 0 && y < mTileMap.length && y > 0) {
			ctile = mTileMap[x][y];
			return ctile;
		}
		return null;
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public WorldThread getmWorldThread() {
		return mWorldThread;
	}

	public void setmWorldThread(WorldThread mWorldThread) {
		this.mWorldThread = mWorldThread;
	}

	public WorldGenerator getMapGenerator() {
		return mapGenerator;
	}

	public void setMapGenerator(WorldGenerator mapGenerator) {
		this.mapGenerator = mapGenerator;
	}

	public NoiseGenerator getNoiseGenerator() {
		return noiseGenerator;
	}

	public void setNoiseGenerator(NoiseGenerator noiseGenerator) {
		this.noiseGenerator = noiseGenerator;
	}

	public Random getR() {
		return r;
	}

	public void setR(Random r) {
		this.r = r;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public static int getmTileSize() {
		return mTileSize;
	}

	public static void setmTileSize(int mTileSize) {
		WorldView.mTileSize = mTileSize;
	}

	public static int getmWorldSize() {
		return mWorldSize;
	}

	public static void setmWorldSize(int mWorldSize) {
		WorldView.mWorldSize = mWorldSize;
	}

	public static int getmBorderWidth() {
		return mBorderWidth;
	}

	public static void setmBorderWidth(int mBorderWidth) {
		WorldView.mBorderWidth = mBorderWidth;
	}

	public static int getmOctaveCount() {
		return mOctaveCount;
	}

	public static void setmOctaveCount(int mOctaveCount) {
		WorldView.mOctaveCount = mOctaveCount;
	}

	public int getmXOffSet() {
		return mXOffSet;
	}

	public void setmXOffSet(int mXOffSet) {
		this.mXOffSet = mXOffSet;
	}

	public int getmYOffSet() {
		return mYOffSet;
	}

	public void setmYOffSet(int mYOffSet) {
		this.mYOffSet = mYOffSet;
	}

	public int getmXScrollOffSet() {
		return mXScrollOffSet;
	}

	public void setmXScrollOffSet(int mXScrollOffSet) {
		this.mXScrollOffSet = mXScrollOffSet;
	}

	public int getmYScrollOffSet() {
		return mYScrollOffSet;
	}

	public void setmYScrollOffSet(int mYScrollOffSet) {
		this.mYScrollOffSet = mYScrollOffSet;
	}

	public int getmXTouch() {
		return mXTouch;
	}

	public void setmXTouch(int mXTouch) {
		this.mXTouch = mXTouch;
	}

	public int getmYTouch() {
		return mYTouch;
	}

	public void setmYTouch(int mYTouch) {
		this.mYTouch = mYTouch;
	}

	public int getmStartRow() {
		return mStartRow;
	}

	public void setmStartRow(int mStartRow) {
		this.mStartRow = mStartRow;
	}

	public int getmMaxRow() {
		return mMaxRow;
	}

	public void setmMaxRow(int mMaxRow) {
		this.mMaxRow = mMaxRow;
	}

	public int getmStartColumn() {
		return mStartColumn;
	}

	public void setmStartColumn(int mStartColumn) {
		this.mStartColumn = mStartColumn;
	}

	public int getmMaxColumn() {
		return mMaxColumn;
	}

	public void setmMaxColumn(int mMaxColumn) {
		this.mMaxColumn = mMaxColumn;
	}

	public Boolean getmIsMoving() {
		return mIsMoving;
	}

	public void setmIsMoving(Boolean mIsMoving) {
		this.mIsMoving = mIsMoving;
	}

	public int getmX() {
		return mX;
	}

	public void setmX(int mX) {
		this.mX = mX;
	}

	public int getmY() {
		return mY;
	}

	public void setmY(int mY) {
		this.mY = mY;
	}

	public int getmI() {
		return mI;
	}

	public void setmI(int mI) {
		this.mI = mI;
	}

	public int getmJ() {
		return mJ;
	}

	public void setmJ(int mJ) {
		this.mJ = mJ;
	}

}
