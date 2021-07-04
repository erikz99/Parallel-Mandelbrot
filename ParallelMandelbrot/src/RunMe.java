
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.imageio.ImageIO;

public class RunMe {
	public static Color[] generateColors(int n)
	{
		Color[] cols = new Color[n];
		for(int i = 0; i < n; i++)
		{
			cols[i] = Color.getHSBColor(0.7f + (float) (-1.2 * i) / (float) n, 0.82f, 0.4f);
		}

		return cols;
	}

	public long _gts() {
		return System.nanoTime();
	}


	public static void main(String[] args) {

		if (args.length < 8) {
			System.out.println("RunMe <num of threads>");
			System.exit(1);
		}

		int thread_count = Integer.parseInt(args[0]);
		int g = Integer.parseInt(args[1]);
		int width = Integer.parseInt(args[2]);
		int height = Integer.parseInt(args[3]);
		double startX = Double.parseDouble(args[4]);
		double endX = Double.parseDouble(args[5]);
		double startY = Double.parseDouble(args[6]);
		double endY = Double.parseDouble(args[7]);

		/*int thread_count = 7;
		int g = 4;
		int width = 3840;
		int height = 2160;
		double startX = -1.8;
		double endX = 0.45;
		double startY = -1.1;
		double endY = 1.1;*/

		short[][] mat = new short[height][width];

		Thread[] tr = new Thread[thread_count];

		long ts_b = Calendar.getInstance().getTimeInMillis();

		for (int i=0; i<thread_count; i++) {
			MandelbrotRunnable r = new MandelbrotRunnable(mat,g,height,width,startX,endX, startY, endY, i,thread_count);
			tr[i] = new Thread(r);
			tr[i].start();
		}

		for(int i = 0; i < thread_count; i++) {
			try {
				tr[i].join();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		long ts_e = Calendar.getInstance().getTimeInMillis();

		System.out.println("job finished in: " + (ts_e - ts_b) + " millis");

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		Graphics2D g2d = bi.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);

		PrintWriter out = new PrintWriter(System.out);

		Color[] pallete = generateColors(1024);

		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				int r = mat[i][j];

				bi.setRGB(j, i, pallete[r].getRGB());
			}
		}
		
		try {
			ImageIO.write(bi, "PNG", new File("SeeMe-2x2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		out.printf("done.\n");

		out.flush();
		out.close();

	}

}
