package org.besus.meice.oramtt;

import java.awt.EventQueue;

import org.besus.meice.oramtt.ui.DatabaseFrame;


public class Main {

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DatabaseFrame frame = new DatabaseFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

}
