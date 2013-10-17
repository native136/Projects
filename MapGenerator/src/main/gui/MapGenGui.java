package main.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import main.GUIHandler;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import java.awt.Font;

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
	private JPanel pnlImgControls;
	
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
		MainFrame.setResizable(false);
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
		pnlControls.setBounds(10, 11, 314, 281);
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
		
		JButton btnGenerateTerrain = new JButton("Generate Image");
		btnGenerateTerrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handler.GenerateNewMap(txtWidth.getText(),txtHeight.getText(),sliderAmp.getValue(),txtBorderWidth.getText(),txtSeed.getText());
				handler.MapToPanel();
				pnlImgControls.setVisible(true);
				
			}
		});
		btnGenerateTerrain.setBounds(10, 247, 294, 23);
		pnlControls.add(btnGenerateTerrain);
		
		JLabel lblRandomSeed = new JLabel("Random Seed:");
		lblRandomSeed.setBounds(10, 135, 76, 14);
		pnlControls.add(lblRandomSeed);
		
		JLabel lblPx = new JLabel("px");
		lblPx.setBounds(183, 14, 46, 14);
		pnlControls.add(lblPx);
		
		JLabel label = new JLabel("px");
		label.setBounds(183, 45, 46, 14);
		pnlControls.add(label);
		
		txtSeed = new JTextField();
		txtSeed.setBounds(87, 132, 199, 20);
		pnlControls.add(txtSeed);
		txtSeed.setColumns(10);
		
		pnlImgControls = new JPanel();
		pnlImgControls.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		pnlImgControls.setBounds(10, 303, 314, 191);
		MainFrame.getContentPane().add(pnlImgControls);
		pnlImgControls.setLayout(null);
		
		JButton btnSaveAsImage = new JButton("Save Image as .png");
		btnSaveAsImage.setBounds(10, 123, 294, 23);
		pnlImgControls.add(btnSaveAsImage);
		
		JButton btnSaveAsTxt = new JButton("Dump Array to .txt");
		btnSaveAsTxt.setBounds(10, 157, 294, 23);
		pnlImgControls.add(btnSaveAsTxt);
		
		JLabel lblScale = new JLabel("Scale:");
		lblScale.setBounds(10, 15, 54, 14);
		pnlImgControls.add(lblScale);
		
		JButton btnScale2 = new JButton("X 2");
		btnScale2.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnScale2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handler.scaledImage(2);
			}
		});
		btnScale2.setBounds(74, 11, 70, 23);
		pnlImgControls.add(btnScale2);
		
		JButton btnScale4 = new JButton("X 4");
		btnScale4.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnScale4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handler.scaledImage(4);
			}
		});
		btnScale4.setBounds(154, 11, 70, 23);
		pnlImgControls.add(btnScale4);
		
		JButton btnScale8 = new JButton("X 8");
		btnScale8.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnScale8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handler.scaledImage(8);
			}
		});
		btnScale8.setBounds(234, 11, 70, 23);
		pnlImgControls.add(btnScale8);
		
		JButton btnOriginal = new JButton("Original");
		btnOriginal.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnOriginal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handler.MapToPanel();
			}
		});
		btnOriginal.setBounds(154, 45, 70, 23);
		pnlImgControls.add(btnOriginal);
		
		JButton btnScale16 = new JButton("X 16");
		btnScale16.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnScale16.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handler.scaledImage(16);
			}
		});
		btnScale16.setBounds(74, 45, 70, 23);
		pnlImgControls.add(btnScale16);
		
		pnlImgControls.setVisible(false);
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
