package org.xor.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
/**A class that manage graphic properties such as anti-aliasing, rendering quality, for {@link Graphics2D} objects
 * 
 * @author LordOfBees
 * @version 1.0
 */
public final class GraphicsConfiguration {
	
	private static final File path = new File("./graphics.opt");
	
	private static boolean ANTIALIASING = true;
	private static int RENDERING = 2;
	/**Load the graphics configuration from the property file
	 * 
	 * @throws IOException
	 */
	public static void load() throws IOException{
		if(!path.exists())
			save();
		Properties prop = new Properties();
		prop.load(new FileInputStream(path));
		ANTIALIASING = Boolean.parseBoolean(prop.getProperty("anti-aliasing", "true"));
		RENDERING = Integer.parseInt(prop.getProperty("rendering_level", "2"));
	}
	/**Save the graphics configuration to the property file
	 * 
	 * @throws IOException
	 */
	public static void save() throws IOException {
		if(!path.exists())
			path.createNewFile();
		Properties prop = new Properties();
		prop.put("anti-aliasing", ""+ANTIALIASING);
		prop.put("rendering_level", ""+RENDERING);
		prop.store(new FileOutputStream(path), "Graphics Configuration");
	}
	/**Set all the rendering hints key depending on the graphics configuration for this {@link Graphics2D} object
	 * <p> NOTE : if the {@link #load()} method hasn't been called yet this method will set the rendering hints as the default one ( excepted for anti-aliasing )
	 * @param g is the graphics object you want to set the rendering hints of
	 */
	public static void configure(Graphics2D g){
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  ANTIALIASING ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF );
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, ANTIALIASING ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RENDERING == 3 ? RenderingHints.VALUE_RENDER_QUALITY : RENDERING == 2 
				? RenderingHints.VALUE_RENDER_DEFAULT : RenderingHints.VALUE_RENDER_SPEED);
	}
	/**Get if anti-aliasing has been enabled by the user
	 * <p> NOTE : if the {@link #load()} method hasn't been called yet this method will always return true
	 * @return true if anti-aliasing should be enabled
	 */
	public static boolean isANTIALIASING() {
		return ANTIALIASING;
	}
	/**Set whether or not the anti-aliasing should be enabled and save the configuration
	 * 
	 * @param aNTIALIASING is the new value of ANITALIASING
	 */
	public static void setANTIALIASING(boolean aNTIALIASING) {
		ANTIALIASING = aNTIALIASING;
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**Get the rendering quality level
	 * <p> NOTE : if the {@link #load()} method hasn't been called yet this method will always return 2
	 * @return <ul><li><b>3</b> for {@link RenderingHints#VALUE_RENDER_QUALITY}</li>
	 * <li><b>2</b> for {@link RenderingHints#VALUE_RENDER_DEFAULT}</li>
	 * <li><b>1</b> for {@link RenderingHints#VALUE_RENDER_SPEED}</li></ul>
	 */
	public static int getRENDERING() {
		return RENDERING;
	}
	/**Set the rendering quality level and save the graphics configuration
	 * @param rENDERING must be : <ul><li><b>3</b> for {@link RenderingHints#VALUE_RENDER_QUALITY}</li>
	 * <li><b>2</b> for {@link RenderingHints#VALUE_RENDER_DEFAULT}</li>
	 * <li><b>1</b> for {@link RenderingHints#VALUE_RENDER_SPEED}</li></ul>
	 */
	public static void setRENDERING(int rENDERING) {
		if(rENDERING>3 || rENDERING<1)
			return;
		RENDERING = rENDERING;
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
