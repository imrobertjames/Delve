package com.waffleflopper.delve.graphics;

import java.util.Random;

public class Screen {

	private int width, height;
	public int[] pixels;
	public int[] tiles = new int[64*64];
	private Random random = new Random();

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		
		for (int i = 0; i < 64*64; i++) {
			tiles[i] = random.nextInt(0xffffff);
		}
		
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0; //sets all the pixels to black
		}
	}

	public void render() {
		for (int y = 0; y < height; y++) { // makes sure we go line by line
			int yy = y; //we don't want to change they variable itself, so yy
			//if (yy < 0 || yy >= height) break;
			for (int x = 0; x < width; x++) {
				int xx = x - 50; //we don't want to change they variable itself, so yy
				//if (xx < 0 || xx >= width) break;
				int tileIndex = ((xx >> 4) & 63) + ((yy >> 4) & 63) * 64; //16 is tile size
				//the & means when xx >> 4 becomes bigger than 63, it returns to 0
				// so when you get to the 64th 'out of bounds tile' just wrap around to the 0th again
				// means 2 to the power of x. so x >> 4 same as x / 16, because 2 to the power of 4 is 16.
				pixels[x + y * width] = tiles[tileIndex]; // make sure we are getting
													// the right index
													// 0x indicates we are doing hex
				
				
			}
		}
	}

}
