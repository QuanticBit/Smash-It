package org.xor.utils;

import javax.swing.JComponent;
/**A utility thread that will repaint a JCOmponent only after a specified delay
 *  in ms whereas the method {@link JComponent#repaint(long)} will do it as soon as possible ( you can go up to 150 fps for simple swing components, that's useless )
 * 
 * @author LordOfBees
 * @version 1.0
 */
public class SwingDelayedRepainter extends Thread{
	private JComponent comp;
	private long delay;
	/**Create and start a new thread that after the delay will call the {@link JComponent#repaint()} method of the <i>JComponent</i>
	 * 
	 * @param comp is the <i>JComponent</i> to repaint
	 * @param delay is the delay before update in ms
	 */
	public SwingDelayedRepainter(JComponent comp, long delay){
		this.comp=comp;
		this.delay=delay;
		new Thread(this).start();
	}
	
	public void run(){
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		comp.repaint();
	}

}
