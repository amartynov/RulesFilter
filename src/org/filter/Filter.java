package org.filter;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.filter.dto.IPRule;
import org.filter.language.LabelList;
import org.filter.language.LanguageFactory;
import org.filter.viewelements.CheckBoxPanel;
import org.filter.viewelements.GraphicPanel;
import org.filter.viewelements.IPRuleJCheckBox;
import org.filter.viewelements.MainMenu;
import org.filter.viewelements.RulesListPanel;

public class Filter {
  
  public final static LabelList labels = LanguageFactory.create("ru");
	
	private final int WINDOW_WIDTH = 700;
	private final int WINDOW_HEIGHT = 600;
	
	public static int panelNumber = 0;
	
	private static final int GRAPHIC_WIDTH = 200;
	private static final int GRAPHIC_HEIGTH = 200;
	
	public static JPanel inputPanel;
	public static JPanel outputPanel;
	
	public static GraphicPanel inputGraphic = new GraphicPanel(GRAPHIC_WIDTH, GRAPHIC_HEIGTH);
	public static GraphicPanel outputGraphic = new GraphicPanel(GRAPHIC_WIDTH, GRAPHIC_HEIGTH);
	
	public static ArrayList<IPRuleJCheckBox> inputCBList = new ArrayList<IPRuleJCheckBox>();
	public static ArrayList<IPRuleJCheckBox> outputCBList = new ArrayList<IPRuleJCheckBox>();
	
	public static ArrayList<IPRule> inputIpRules = new ArrayList<IPRule>();
	public static ArrayList<IPRule> outputIpRules = new ArrayList<IPRule>();
	
	private final JFrame jfrm = new JFrame(labels.getProgName());
	public final static JTextArea logTA = new JTextArea();
	
//	private ArrayList<IPRuleIntersection> intersection;
	
	public Filter() {
//		JFrame jfrm = new JFrame("Filter");
		jfrm.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		jfrm.setMinimumSize(new Dimension(WINDOW_WIDTH + 5, WINDOW_HEIGHT + 45));
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrm.setResizable(false);
		
		jfrm.setLayout(null);
		
		//jfrm.setJMenuBar(getMainMenu());
		jfrm.setJMenuBar(new MainMenu());
		
		//main tabbed pane
		JTabbedPane mainTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		inputPanel = new RulesListPanel(inputCBList, inputGraphic, WINDOW_WIDTH - 210, WINDOW_HEIGHT);
		mainTabbedPane.addTab(labels.getPanelInput(), inputPanel);
		
		outputPanel = new RulesListPanel(outputCBList, outputGraphic, WINDOW_WIDTH - 210, WINDOW_HEIGHT);
		mainTabbedPane.addTab(labels.getPanelOutput(), outputPanel);
		
		mainTabbedPane.setBounds(0, 0, WINDOW_WIDTH, 400);
		mainTabbedPane.addChangeListener(new ChangeListener() {
      
      @Override
      public void stateChanged(ChangeEvent e) {
        //TODO add curent panel
        panelNumber = ((JTabbedPane)e.getSource()).getSelectedIndex();
        
      }
    });
		
		//log text area settings
		logTA.setEditable(false);
		JScrollPane jsbLog = new JScrollPane(logTA);
		jsbLog.setBounds(0, mainTabbedPane.getHeight(), WINDOW_WIDTH, WINDOW_HEIGHT - mainTabbedPane.getHeight());
		jsbLog.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsbLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		jfrm.getContentPane().add(mainTabbedPane);
		jfrm.getContentPane().add(jsbLog);
		
		jfrm.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new Filter();
			}
		});
	}
	
	public static void clearOutput() {
	  Filter.outputIpRules.clear();
    Filter.outputCBList.clear();
    ((CheckBoxPanel)Filter.outputPanel.getComponent(0)).clear();
    ((JRadioButton)((JPanel)((JPanel)((JPanel)outputPanel.getComponent(1)).getComponent(1)).getComponent(1)).getComponent(Protocol.values().length + 1)).doClick();
    Filter.outputGraphic.clear();
	}
	
	public static void clearInput() {
	  Filter.inputIpRules.clear();
    Filter.inputCBList.clear();
    ((CheckBoxPanel)Filter.inputPanel.getComponent(0)).clear();
    ((JRadioButton)((JPanel)((JPanel)((JPanel)inputPanel.getComponent(1)).getComponent(1)).getComponent(1)).getComponent(Protocol.values().length + 1)).doClick();
    Filter.inputGraphic.clear();
  }
	
}