package maceman.makersland.system.systems;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.util.LruCache;
import maceman.makersland.component.components.Render;
import maceman.makersland.entity.Entity;
import maceman.makersland.entity.EntityManager;
import maceman.makersland.system.System;

public class RenderingSystem implements System {

	public ArrayList<Render> components;
	public EntityManager em;
	public LruCache<String, Bitmap> mMemoryCache;
	public Bitmap bitmap;

	public RenderingSystem(EntityManager em,
			LruCache<String, Bitmap> memoryCache) {

		this.em = em;
		this.mMemoryCache = memoryCache;
	}

	@Override
	public void process(int entityID) {
	}

	@Override
	public void addComponentTo(int entityID) {
		if (em.getIndex().size() < entityID) {
			components.add(entityID, new Render(new Entity(entityID, true),
					null, null));
		} else {
			components.set(entityID, new Render(new Entity(entityID, true),
					null, null));
		}
	}

	@Override
	public void updateComponent(int entityID) {
		components.set(entityID, new Render(new Entity(entityID, true), null,
				null));
	}

	@Override
	public void removeComponent(int entityID) {
		components.set(entityID, null);
	}

	public void draw(int entityID, Canvas canvas, int x, int y, int tileSize,
			Paint paint) {
		// check to see if we already have the image loaded; if not, loads it.
		if (mMemoryCache.get(components.get(entityID).bitmapString) == null) {
			bitmap = components.get(entityID).bitmap;
			mMemoryCache.put(components.get(entityID).bitmapString, components.get(entityID).bitmap);
		}else {
			bitmap = mMemoryCache.get(components.get(entityID).bitmapString);
		}
		canvas.drawBitmap(bitmap, null, new Rect(x, y, x + tileSize, y
				+ tileSize), paint);

	}

}
