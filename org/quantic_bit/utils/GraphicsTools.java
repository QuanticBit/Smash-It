package org.xor.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
/**An utility class, that provides useful graphics methods
 * <p>All methods use {@link GraphicsConfiguration} to configure Graphics2D object
 * 
 * @author LordOfBees
 * @version 1.01
 */
public final class GraphicsTools {
	/**Get the screen width
	 * 
	 * @return the screen width in pixels
	 */
	public static double getScreenWidth(){
		return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}
	/**Get the screen height
	 * 
	 * @return the screen height in pixels
	 */
	public static double getScreenHeight(){
		return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	/**Get the number of screens
	 * @return the number of screens
	 */
	public static int getScreenNumber(){
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;
	}
	/**Get the screen n°<b>screenIndex</b>'s width
	 * @param screenIndex is the index of the screen you want to get informations on
	 * @return the screen width in pixels
	 */
	public static double getScreenWidth(int screenIndex){
		if(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length >= screenIndex)
			return -1;
		return 	GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenIndex].
				getDisplayMode().getWidth();
	}
	/**Get the screen n°<b>screenIndex</b>'s height
	 * @param screenIndex is the index of the screen you want to get informations on
	 * @return the screen height in pixels
	 */
	public static double getScreenHeight(int screenIndex){
		if(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length >= screenIndex)
			return -1;
		return 	GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenIndex].
				getDisplayMode().getHeight();
	}
	
	
	/**Load a an image from the jar resource folder
	 * 
	 * @param imgName is the image name ( with the extension )
	 * @return an instance of the loaded image
	 */
	public static BufferedImage loadImageFromResource(String imgName){
		BufferedImage img1 = null;
		try {
			img1 = ImageIO.read(GraphicsTools.class.getClassLoader().getResource("res/"+imgName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img1;
	}
	
	/**Scale an image to this scale factor
	 * 
	 * @param image is the image to resize
	 * @param coeff is the scale factor
	 * @return a scaled instance of this image
	 */
	public static BufferedImage scaleImage(BufferedImage image, double coeff){
		return resizeImage(image, (int)(image.getWidth()*coeff), (int) (image.getHeight()*coeff));
	}
	
	/**Scale an image to these exact dimensions with bilinear interpolation
	 * <p>To know if Anti-aliasing is enabled see {@link GraphicsConfiguration#isANTIALIASING()} or
	 *  to choose use instead {@link #resizeImage(BufferedImage, int, int, boolean)}
	 * @param img is the image to resize
	 * @param width is the new width of the image
	 * @param height is the new height of the image
	 * @return a scaled instance of this image
	 */
	public static BufferedImage resizeImage(BufferedImage img, int width, int height) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(width, height, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        GraphicsConfiguration.configure(g);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, width, height, 0, 0, w, h, null);  
        g.dispose();  
        return dimg;  
    } 
	/**Scale an image to these exact dimensions with bilinear interpolation
	 * 
	 * @param img is the image to resize
	 * @param width is the new width of the image
	 * @param height is the new height of the image
	 * @param antiAliasing whether or not enable anti-aliasing
	 * @return a scaled instance of this image
	 */
	public static BufferedImage resizeImage(BufferedImage img, int width, int height, boolean antiAliasing) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(width, height, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        if(antiAliasing)
        	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, width, height, 0, 0, w, h, null);  
        g.dispose();  
        return dimg;  
    }
	/**Get the horizontal flip of this image
	 * 
	 * @param img is the image to apply on the horizontal flip
	 * @return the horizontal flip of this image
	 */
	public static BufferedImage horizontalFlip(BufferedImage img){
		 int w = img.getWidth();  
	     int h = img.getHeight();  
	     BufferedImage dimg = new BufferedImage(w, h, img.getType());  
	     Graphics2D g = dimg.createGraphics();  
	     GraphicsConfiguration.configure(g);
	     g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);  
	     g.dispose();  
	     return dimg;  
	}
	/**Get the vertical flip of this image
	 * 
	 * @param img is the image to apply on the vertical flip
	 * @return the vertical flip of this image
	 */
	public static BufferedImage verticalFlip(BufferedImage img) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(w, h, img.getColorModel().getTransparency());  
        Graphics2D g = dimg.createGraphics();  
        GraphicsConfiguration.configure(g);
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);  
        g.dispose();  
        return dimg;  
    }
	/**Get the image with the inverted colors
	 * 
	 * @param img is the image to apply on the inversion
	 * @return the new image
	 */
	public static BufferedImage invert(BufferedImage source){
		short[] invert = new short[256];
		for (int i = 0; i < 256; i++)
			invert[i] = (short)(255 - i);
		BufferedImageOp invertOp = new LookupOp(
		    new ShortLookupTable(0, invert), null);
		BufferedImage destination = scaleImage(source, 1);
		destination = invertOp.filter(source, destination);
		return destination;
	}
	/**Rotate an image
	 * 
	 * @param img is the image to apply on the rotation
	 * @param angle is the angle of rotation in degrees
	 * @return the rotated image
	 */
	public static BufferedImage rotate(BufferedImage img, double angle) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(w, h, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        GraphicsConfiguration.configure(g);
        g.rotate(Math.toRadians(angle), w/2, h/2);  
        g.drawImage(img, null, 0, 0);  
        return dimg;  
    }
	/**Replace every pixel of the old color with a pixel of the replacer color
	 * 
	 * @param img is the image to apply on the operation
	 * @param old is the color to be replaced
	 * @param replacer is the color to replace with
	 * @return the new image
	 */
	public static BufferedImage replaceColor(BufferedImage img, Color old, Color replacer){
		BufferedImage copy = deepCopy(img);
		for (int x = 0; x< img.getWidth(); x++){
			for(int y = 0; y < img.getHeight() ; y++){
				int rgb = img.getRGB(x, y);
				if(rgb == old.getRGB())
					copy.setRGB(x, y, replacer.getRGB());
			}
		}
		return copy;	
	}
	/**Get the average color of this image
	 * <p>In fact we resize down the image.
	 * <p>Then we make alpha-pondered average values for red, green, blue, and just an average for alpha
	 * @param img is the image to get the colors of
	 * @return the average color
	 */
	public static Color getAverageColor(BufferedImage img){
		int alpha = 0;
		double red = 0;
		double green = 0;
		double blue = 0;
		double divider = 0;
		int width = Math.min(100, img.getWidth());
		int height = (width*width/img.getWidth());
		BufferedImage image = resizeImage(img, width, height, false);
		for (int x = 0; x< width ; x++){
			for(int y = 0; y < height ; y++){
				Color color = new Color(image.getRGB(x, y), true);
				if(color.getAlpha()==0)
						continue;
				alpha += color.getAlpha();
				double factor = (double)(color.getAlpha())/255.0;
				divider += factor;
				red += color.getRed()*factor;
				green += color.getGreen()*factor;
				blue += color.getBlue()*factor;
			}
		}
		return new Color((int) (red/divider), (int) (green/divider), (int) (blue/divider), alpha/(100*height));
	}
	/**Get the dominant color of this image.
	 * <p>In fact we resize down the image and we do not take in count black pixels
	 * <p>Then we calculate the color that appears the most of the time.
	 * @param img is the image to get the colors of
	 * @return the dominant color
	 */
	public static Color getDominantColor(BufferedImage img){
		HashMap<Integer, Integer> colors = new HashMap<Integer, Integer>();
		int max = 0;  
	    int width = Math.min(100, img.getWidth());
		int height = (width*width/img.getWidth());
	    BufferedImage image = resizeImage(img, width, height);
		for (int x = 0; x< width; x++){
			for(int y = 0; y < height ; y++){
				Color color = new Color(image.getRGB(x, y));
				if(color.getRGB() == Color.BLACK.getRGB())
					continue;
				if(colors.containsKey(color.getRGB())){
					int quantity = colors.get(color.getRGB())+1;
					max = Math.max(max, quantity);
					colors.put(color.getRGB(), quantity);
				}else{
					max = Math.max(max, 1);
					colors.put(color.getRGB(), 1);
				}
			}
		}
		int index =0;
		for ( Integer value : colors.values()){
			if(value == max)
				break;
			index++;
		}
		return new Color(colors.keySet().toArray(new Integer[0])[index]);
	}
	/**Get the color representing the dominant color canal of this image.
	 * <p>In fact we resize down the image.
	 * <p>Then we make alpha-pondered values for red, green, blue, and just choose the biggest
	 * @param img is the image to get the colors of
	 * @return <ul><li>{@link Color#RED} for red canal<li>{@link Color#GREEN} for green canal<li>{@link Color#BLUE} for blue canal</ul>
	 */
	public static Color getDominantCanal(BufferedImage img){
		double red = 0;
		double green = 0;
		double blue = 0;
		int width = Math.min(100, img.getWidth());
		int height = (width*width/img.getWidth());
		BufferedImage image = resizeImage(img, width, height, false);
		for (int x = 0; x< width ; x++){
			for(int y = 0; y < height ; y++){
				Color color = new Color(image.getRGB(x, y), true);
				if(color.getAlpha()==0)
						continue;
				double factor = (double)(color.getAlpha())/255.0;
				red += color.getRed()*factor;
				green += color.getGreen()*factor;
				blue += color.getBlue()*factor;
			}
		}
		double max = Math.max(Math.max(blue, green), red);
		return max == red ? Color.RED : max == green ? Color.GREEN : Color.BLUE;
	}
	/**
	 * Copy a buffered image without re-drawing it
	 * @param bi is the buffered image to copy
	 * @return a copy of this buffered image
	 */
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}

}
