package org.smash.res;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.smash.Map;
import org.xor.utils.FileTools;
import org.xor.utils.GraphicsConfiguration;
import org.xor.utils.GraphicsTools;

public class Skin {
	
	private BufferedImage standR, standL;
	private BufferedImage walkR, walkL;
	private BufferedImage jumpR, jumpL;
	
	private BufferedImage plate = null;
	
	private File path;
	
	public void generateLeftPart(){
		boolean before = GraphicsConfiguration.isANTIALIASING();
		int before2 = GraphicsConfiguration.getRENDERING();
		GraphicsConfiguration.setANTIALIASING(false);
		GraphicsConfiguration.setRENDERING(2);
		setStandL(GraphicsTools.horizontalFlip(standR));
		setWalkL(GraphicsTools.horizontalFlip(walkR));
		setJumpL(GraphicsTools.horizontalFlip(jumpR));
		GraphicsConfiguration.setANTIALIASING(before);
		GraphicsConfiguration.setRENDERING(before2);
	}
	
	public BufferedImage getStandR() {
		return standR;
	}
	public void setStandR(BufferedImage standR) {
		this.standR = standR;
	}
	public BufferedImage getStandL() {
		return standL;
	}
	public void setStandL(BufferedImage standL) {
		this.standL = standL;
	}
	public BufferedImage getWalkR() {
		return walkR;
	}
	public void setWalkR(BufferedImage walkR) {
		this.walkR = walkR;
	}
	public BufferedImage getWalkL() {
		return walkL;
	}
	public void setWalkL(BufferedImage walkL) {
		this.walkL = walkL;
	}
	public BufferedImage getJumpR() {
		return jumpR;
	}
	public void setJumpR(BufferedImage jumpR) {
		this.jumpR = jumpR;
	}
	public BufferedImage getJumpL() {
		return jumpL;
	}
	public void setJumpL(BufferedImage jumpL) {
		this.jumpL = jumpL;
	}
	
	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
	}
	
	public void load(){
		if(!path.exists())
			return;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(path);
			Enumeration<? extends ZipEntry> files = zipFile.entries();

			while (files.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) files.nextElement();
				// if the entry is not directory and matches relative file then extract it
				if (!entry.isDirectory() && entry.getName().endsWith("standR.png")) {
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
					// Read the file
					standR = GraphicsTools.resizeImage(ImageIO.read(bis), Map.size, Map.size, false);
				}
				else if (!entry.isDirectory() && entry.getName().endsWith("walkR.png")) {
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
					// Read the file
					walkR = GraphicsTools.resizeImage(ImageIO.read(bis), Map.size, Map.size, false);
				}
				else if (!entry.isDirectory() && entry.getName().endsWith("jumpR.png")) {
					BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
					// Read the file
					jumpR = GraphicsTools.resizeImage(ImageIO.read(bis), Map.size, Map.size, false);
				} else {
					continue;
				}
			}
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		generateLeftPart();
	}

	public void save(){
		if(!path.exists())
			try {
				path.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		File pic1 = new File(path.getParent()+"/standR.png");
		if(!pic1.exists())
			try {
				pic1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		File pic2 = new File(path.getParent()+"/walkR.png");
		if(!pic2.exists())
			try {
				pic2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		File pic3 = new File(path.getParent()+"/jumpR.png");
		if(!pic3.exists())
			try {
				pic3.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {
			ImageIO.write(standR, "png", pic1);
			ImageIO.write(walkR, "png", pic2);
			ImageIO.write(jumpR, "png", pic3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			ZipOutputStream zos = new ZipOutputStream(fos);
			zos.setLevel(ZipOutputStream.STORED);
			FileTools.addToZipFile(pic1, zos);
			FileTools.addToZipFile(pic2, zos);
			FileTools.addToZipFile(pic3, zos);
			zos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		pic1.delete();
		pic2.delete();
		pic3.delete();
	}

	public BufferedImage getPlate() {
		if(plate == null)
			genPlate();
		return plate;
	}

	private void genPlate() {
		plate = GraphicsTools.loadImageFromResource("name_plate.png");
		Graphics2D g = plate.createGraphics();
		g.drawImage(standL, 50, 10, null);
		g.dispose();
	}
}
