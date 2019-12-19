package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageGenerator {
	public String imgCode;
	public BufferedImage image;

	private static Color getColorBg(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc / 2 + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	private static int[] getRandomRgb(Random random) {  
        int[] rgb = new int[3];  
        for (int i = 0; i < 3; i++) {  
            rgb[i] = random.nextInt(255);  
        }  
        return rgb;  
    }  
	
	
	private static void shear(Random random,Graphics g, int w1, int h1, Color color) {  
        shearX(random,g, w1, h1, color);  
        shearY(random,g, w1, h1, color);  
    }  
	
	private static void shearX(Random random,Graphics g, int w1, int h1, Color color) {  
		  
        int period = random.nextInt(2);  
  
        boolean borderGap = true;  
        int frames = 1;  
        int phase = random.nextInt(2);  
  
        for (int i = 0; i < h1; i++) {  
            double d = (double) (period >> 1)  
                    * Math.sin((double) i / (double) period  
                            + (6.2831853071795862D * (double) phase)  
                            / (double) frames);  
            g.copyArea(0, i, w1, 1, (int) d, 0);  
            if (borderGap) {  
                g.setColor(color);  
                g.drawLine((int) d, i, 0, i);  
                g.drawLine((int) d + w1, i, w1, i);  
            }  
        }  
  
    }
	
	private static void shearY(Random random,Graphics g, int w1, int h1, Color color) {  
		  
        int period = random.nextInt(40) + 10; // 50;  
  
        boolean borderGap = true;  
        int frames = 20;  
        int phase = 7;  
        for (int i = 0; i < w1; i++) {  
            double d = (double) (period >> 1)  
                    * Math.sin((double) i / (double) period  
                            + (6.2831853071795862D * (double) phase)  
                            / (double) frames);  
            g.copyArea(i, 0, 1, h1, 0, (int) d);  
            if (borderGap) {  
                g.setColor(color);  
                g.drawLine(i, (int) d, i, 0);  
                g.drawLine(i, (int) d + h1, i, h1);  
            }  
        }  
    }  
	
	private static int getRandomIntColor(Random random) {  
        int[] rgb = getRandomRgb( random);  
        int color = 0;  
        for (int c : rgb) {  
            color = color << 8;  
            color = color | c;  
        }  
        return color;  
    }  

	private static String charsLong = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";

	private static String chars = charsLong;

	public static ImageGenerator make() {
		int charsLength = chars.length();
		int w = 68, h = 21;
		int verifySize = 4;  
		BufferedImage image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g2 = image.createGraphics();
		Random rand = new Random();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);  
        Color[] colors = new Color[5];  
        Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN,  
                Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,  
                Color.PINK, Color.YELLOW };  
        
        float[] fractions = new float[colors.length];  
        for(int i = 0; i < colors.length; i++){  
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];  
            fractions[i] = rand.nextFloat();  
        }  
        Arrays.sort(fractions);  
        g2.setColor( Color.gray );// 设置边框色  
        g2.fillRect(0, 0, w, h);  
		

        Color c = getColorBg(200, 250);  
        g2.setColor(c);// 设置背景色  
        g2.fillRect(0, 2, w, h-4);  
        
        //绘制干扰线  
        Random random = new Random();  
        
        g2.setColor(getRandColor(100, 160));// 设置线条的颜色  
        for (int i = 0; i < 20; i++) {  
            int x = random.nextInt(w - 1);  
            int y = random.nextInt(h - 1);  
            int xl = random.nextInt(6) + 1;  
            int yl = random.nextInt(12) + 1;  
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);  
        }
        
        // 添加噪点  
        shear(rand,g2, w, h, c);// 使图片扭曲
        
        g2.setColor(getRandColor(100, 160));  
        g2.rotate(new Random().nextInt(20));
        int fontSize = h-2;  
       // Font font = new Font("Algerian", Font.ITALIC, fontSize);
        Font font=getRandomFont();
        g2.setFont(font);  
        
        StringBuilder sRand = new StringBuilder();
        for(int i=0;i<verifySize;i++){
        	char rc = chars.charAt(random.nextInt(charsLength));
        	sRand.append(rc);
        }
		
        char[] chars = sRand.toString().toCharArray();  
        //char[] chars ="五乘8=".toCharArray();
        for(int i = 0; i < verifySize; i++){  
        	int fSize = fontSize-rand.nextInt(2);
        	//font = new Font("Algerian", Font.ITALIC, fSize);
        	 font=getRandomFont();
        	//font = new Font("黑体", Font.ITALIC, fSize); 
            g2.setFont(font);  
            
            if(i!=1){
	            AffineTransform affine = new AffineTransform();  
	            affine.setToRotation(Math.PI / 8 * rand.nextDouble() * 
	            		(rand.nextBoolean() ? 1 : -1), 
	            		(w / verifySize) * i + fontSize/2, h/2);  
	            g2.setTransform(affine);  
            }
            /*g2.drawChars(chars, i, 1, ((w-10) / verifySize) * i + 5, h/2 
            		+ fontSize/2 + 10); */ 
            g2.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
            int ww = ((w-10) / verifySize) * i + random.nextInt(5);
            int hh = h - 4 - random.nextInt(2);
            //if( i==2 ) ww -= 5;
            g2.drawChars(chars, i, 1, 
            		ww,hh
            		);
        }  

		g2.dispose();

		// 生成图片完毕
		ImageGenerator result = new ImageGenerator();
		result.imgCode = sRand.toString();
		result.image = image;
		return result;
	}

	// 按字节输出
	public byte[] getImgBytes() {

		ByteArrayOutputStream bot = new ByteArrayOutputStream();

		byte[] b = null;
		try {
			ImageIO.write(image, "png", bot);
			b = bot.toByteArray();
			bot.close();
		} catch (Exception e) {
		}
		return b;
	}
	//随机生成字体、文字大小
	public static Font getRandomFont() { 
	     String[] fonts = {"Georgia", "Verdana", "Arial", "Tahoma", "Time News Roman", "Courier New", "Arial Black", "Quantzite"}; 
	     int fontIndex = (int)Math.round(Math.random() * (fonts.length - 2)); 
	     int fontSize = (int) Math.round(Math.random() * 4 + 18); 
	     return new Font(fonts[fontIndex], Font.PLAIN, fontSize); 
	 } 
}
