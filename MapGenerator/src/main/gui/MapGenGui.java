package main.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;

import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ImageIcon;

import main.GUIHandler;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MapGenGui {

	private GUIHandler handler;
	
	private JFrame MainFrame;
	private JTextField txtWidth;
	private JTextField txtHeight;
	private JTextField txtBorderWidth;
	private JTextField txtSeed;
	private JLabel lblImageHolder;
	private JScrollPane scrollPane;
	private JPanel pnlImg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MapGenGui window = new MapGenGui();
					window.MainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MapGenGui() {
		initialize();
		handler = new GUIHandler(this);
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		MainFrame = new JFrame();
		MainFrame.setTitle("Terrain Generator");
		MainFrame.setBounds(100, 100, 1015, 715);
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.getContentPane().setLayout(null);
		
		pnlImg = new JPanel();
		pnlImg.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlImg.setBounds(334, 11, 1000, 1000);
		
		scrollPane = new JScrollPane(pnlImg);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnlImg.setLayout(null);
		
		lblImageHolder = new JLabel("");
		pnlImg.add(lblImageHolder);
		lblImageHolder.setBounds(0, 0, 421, 421);
		scrollPane.setBounds(334, 11, 655, 655);
		scrollPane.setViewportView(pnlImg);
		MainFrame.getContentPane().add(scrollPane);
		
		JPanel pnlControls = new JPanel();
		pnlControls.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlControls.setBounds(10, 11, 314, 655);
		MainFrame.getContentPane().add(pnlControls);
		pnlControls.setLayout(null);
		
		JLabel lblWidth = new JLabel("Width:");
		lblWidth.setBounds(10, 11, 85, 14);
		pnlControls.add(lblWidth);
		
		txtWidth = new JTextField();
		txtWidth.setBounds(87, 8, 86, 20);
		pnlControls.add(txtWidth);
		txtWidth.setColumns(10);
		
		txtHeight = new JTextField();
		txtHeight.setBounds(87, 39, 86, 20);
		pnlControls.add(txtHeight);
		txtHeight.setColumns(10);
		
		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setBounds(10, 42, 85, 14);
		pnlControls.add(lblHeight);
		
		final JSlider sliderAmp = new JSlider();
		sliderAmp.setPaintTicks(true);
		sliderAmp.setPaintLabels(true);
		sliderAmp.setMinorTickSpacing(1);
		sliderAmp.setValue(7);
		sliderAmp.setMinimum(5);
		sliderAmp.setMaximum(10);
		sliderAmp.setBounds(76, 70, 228, 23);
		pnlControls.add(sliderAmp);
		
		JLabel lblDetail = new JLabel("Amplitude:");
		lblDetail.setBounds(10, 67, 85, 14);
		pnlControls.add(lblDetail);
		
		JLabel lblBorderWidth = new JLabel("Border Width:");
		lblBorderWidth.setBounds(10, 107, 67, 14);
		pnlControls.add(lblBorderWidth);
		
		txtBorderWidth = new JTextField();
		txtBorderWidth.setBounds(87, 104, 86, 20);
		pnlControls.add(txtBorderWidth);
		txtBorderWidth.setColumns(10);
		
		JButton btnGenerateImage = new JButton("Generate Image");
		btnGenerateImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handler.GenerateNewMap(txtWidth.getText(),txtHeight.getText(),sliderAmp.getValue(),txtBorderWidth.getText(),txtSeed.getText());
				handler.MapToPanel();
			}
		});
		btnGenerateImage.setBounds(10, 553, 294, 23);
		pnlControls.add(btnGenerateImage);
		
		JButton btnDumpArray = new JButton("Dump Array to .txt");
		btnDumpArray.setBounds(10, 621, 294, 23);
		pnlControls.add(btnDumpArray);
		
		JButton btnSaveImage = new JButton("Save Image as .png");
		btnSaveImage.setEnabled(false);
		btnSaveImage.setBounds(10, 587, 294, 23);
		pnlControls.add(btnSaveImage);
		
		JLabel lblRandomSeed = new JLabel("Random Seed:");
		lblRandomSeed.setBounds(10, 170, 76, 14);
		pnlControls.add(lblRandomSeed);
		
		JLabel lblPx = new JLabel("px");
		lblPx.setBounds(183, 14, 46, 14);
		pnlControls.add(lblPx);
		
		JLabel label = new JLabel("px");
		label.setBounds(183, 45, 46, 14);
		pnlControls.add(label);
		
		txtSeed = new JTextField();
		txtSeed.setBounds(87, 167, 199, 20);
		pnlControls.add(txtSeed);
		txtSeed.setColumns(10);
	}

	public GUIHandler getHandler() {
		return handler;
	}

	public void setHandler(GUIHandler handler) {
		this.handler = handler;
	}

	public JFrame getMainFrame() {
		return MainFrame;
	}

	public void setMainFrame(JFrame mainFrame) {
		MainFrame = mainFrame;
	}

	public JTextField getTxtWidth() {
		return txtWidth;
	}

	public void setTxtWidth(JTextField txtWidth) {
		this.txtWidth = txtWidth;
	}

	public JTextField getTxtHeight() {
		return txtHeight;
	}

	public void setTxtHeight(JTextField txtHeight) {
		this.txtHeight = txtHeight;
	}

	public JTextField getTxtBorderWidth() {
		return txtBorderWidth;
	}

	public void setTxtBorderWidth(JTextField txtBorderWidth) {
		this.txtBorderWidth = txtBorderWidth;
	}

	public JTextField getTxtSeed() {
		return txtSeed;
	}

	public void setTxtSeed(JTextField txtSeed) {
		this.txtSeed = txtSeed;
	}

	public JLabel getLblImageHolder() {
		return lblImageHolder;
	}

	public void setLblImageHolder(JLabel lblImageHolder) {
		this.lblImageHolder = lblImageHolder;
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public JPanel getPnlImg() {
		return pnlImg;
	}

	public void setPnlImg(JPanel pnlImg) {
		this.pnlImg = pnlImg;
	}
}
