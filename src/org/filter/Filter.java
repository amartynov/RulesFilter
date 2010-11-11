package org.filter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.filter.dto.IPRule;
import org.filter.dto.IPRuleIntersection;
import org.filter.language.LabelList;
import org.filter.language.LanguageFactory;
import org.filter.listeners.AboutMenuItemListener;
import org.filter.listeners.AxisJRBActionListener;
import org.filter.listeners.ExitMenuItemListener;
import org.filter.listeners.ExportFileMenuItemListener;
import org.filter.listeners.StartMenuItemListener;
import org.filter.viewelements.CheckBoxPanel;
import org.filter.viewelements.GraphicPanel;
import org.filter.viewelements.IPRuleJCheckBox;

public class Filter {
  
  private final static LabelList labels = LanguageFactory.create("ru");
	
	private final int WINDOW_WIDTH = 700;
	private final int WINDOW_HEIGHT = 600;
	
	private final int GRAPHIC_WIDTH = 200;
	private final int GRAPHIC_HEIGTH = 200;
	
	private JPanel inputPanel;
	private JPanel outputPanel;
	
	private GraphicPanel inputGraphic = new GraphicPanel(GRAPHIC_WIDTH, GRAPHIC_HEIGTH);
	private GraphicPanel outputGraphic = new GraphicPanel(GRAPHIC_WIDTH, GRAPHIC_HEIGTH);
	
	private ArrayList<IPRuleJCheckBox> inpuntCBList = new ArrayList<IPRuleJCheckBox>();
	private ArrayList<IPRuleJCheckBox> outpuntCBList = new ArrayList<IPRuleJCheckBox>();
	
	private ArrayList<IPRule> inputIpRules;
	private ArrayList<IPRule> outputIpRules = new ArrayList<IPRule>();;
	private File openedFile;
	
	private final JFrame jfrm = new JFrame(labels.getProgName());
	private final JTextArea logTA = new JTextArea();
	
	public Filter() {
//		JFrame jfrm = new JFrame("Filter");
		jfrm.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		jfrm.setMinimumSize(new Dimension(WINDOW_WIDTH + 5, WINDOW_HEIGHT + 45));
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jfrm.setLayout(null);
		
		jfrm.setJMenuBar(getMainMenu());
		
		//main tabbed pane
		JTabbedPane mainTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		inputPanel = getRulesListPanel(inpuntCBList, inputGraphic);
		mainTabbedPane.addTab(labels.getPanelInput(), inputPanel);
		
		outputPanel = getRulesListPanel(outpuntCBList, outputGraphic);
		mainTabbedPane.addTab(labels.getPanelOutput(), outputPanel);
		
		mainTabbedPane.setBounds(0, 0, WINDOW_WIDTH, 400);
		
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
	
	private JPanel getRulesListPanel(ArrayList<IPRuleJCheckBox> list, GraphicPanel grPanel){
		JPanel panel = new JPanel(null);
		
		JPanel axisSet = new JPanel();
		axisSet.setLayout(new BoxLayout(axisSet, BoxLayout.Y_AXIS));

		JPanel XaxisSet = new JPanel();
		XaxisSet.setLayout(new BoxLayout(XaxisSet, BoxLayout.X_AXIS));
		XaxisSet.add(new JLabel("x: "));
		ButtonGroup xbg = new ButtonGroup();
		JPanel xrbp = new JPanel();
		xrbp.setLayout(new BoxLayout(xrbp, BoxLayout.X_AXIS));
		
		ArrayList<JRadioButton> xlist = getAxisRadionButtonList(grPanel, GraphicPanel.X_AXIS);
		for(JRadioButton b : xlist){
			xrbp.add(b);
			xbg.add(b);
		}
		
		JScrollPane XaxisSP = new JScrollPane(xrbp);
		XaxisSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		XaxisSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		XaxisSP.setMaximumSize(new Dimension(WINDOW_WIDTH - 210, 40));
		
		JPanel YaxisSet = new JPanel();
		YaxisSet.setLayout(new BoxLayout(YaxisSet, BoxLayout.X_AXIS));
		YaxisSet.add(new JLabel("y: "));
		ButtonGroup ybg = new ButtonGroup();
		JPanel yrbp = new JPanel();
		yrbp.setLayout(new BoxLayout(yrbp, BoxLayout.X_AXIS));
		
		ArrayList<JRadioButton> ylist = getAxisRadionButtonList(grPanel, GraphicPanel.Y_AXIS);
		for(JRadioButton b : ylist) {
			yrbp.add(b);
			ybg.add(b);
		}
		
		JScrollPane YaxisSP = new JScrollPane(yrbp);
		YaxisSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		YaxisSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		YaxisSP.setMaximumSize(new Dimension(WINDOW_WIDTH - 210, 40));
		
		XaxisSet.add(XaxisSP);
		YaxisSet.add(YaxisSP);
		
		axisSet.add(XaxisSet);
		axisSet.add(YaxisSet);
		
		JPanel grp = new JPanel();
		grp.setLayout(new BoxLayout(grp, BoxLayout.Y_AXIS));
		grp.setBounds(205, 5, WINDOW_WIDTH - 210, 350);
		
		grp.add(axisSet);
		grp.add(grPanel);
		
		panel.add(new CheckBoxPanel(grPanel));
		panel.add(grp);
		
		return panel;
		
	}
	
	private ArrayList<JRadioButton> getAxisRadionButtonList( GraphicPanel gp, int axis) {
		ArrayList<JRadioButton> list = new ArrayList<JRadioButton>();
		for(GraphicAxises ga : GraphicAxises.values()){
			JRadioButton jrb = new JRadioButton(ga.getLabel());
			jrb.addActionListener(new AxisJRBActionListener(gp, ga, axis));
			list.add(jrb);
		}
		list.get(0).doClick();
		
		return list;
	}
	
	private JMenuBar getMainMenu() {
		
		//Menu settings
		JMenu fileMenu = new JMenu(labels.getMenuFileLabel());
		JMenuItem openFileItem = new JMenuItem(labels.getMenuFileOpenLabel());
		JMenuItem exportFileItem = new JMenuItem(labels.getMenuFileExportLabel());
		JMenuItem exitItem = new JMenuItem(labels.getMenuFileExitLabel());
		
		JMenu editMenu = new JMenu(labels.getMenuEditLabel());
		final JMenuItem start =new JMenuItem(labels.getMenuEditStartLabel());
		
		JMenu helpMenu = new JMenu(labels.getMenuHelpLabel());
		JMenuItem about = new JMenuItem(labels.getMenuHelpAboutLabel());
		
		//file menu settings
//		OpenFileMenuItemListener ofListener = new OpenFileMenuItemListener(jfrm, logTA, start);
//		openFileItem.addActionListener(ofListener);
		openFileItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			  JFileChooser jfc = new JFileChooser();
				int retVal = jfc.showOpenDialog(jfrm);
				if(retVal == JFileChooser.APPROVE_OPTION) {
					openedFile = jfc.getSelectedFile();
					inputIpRules = getIPRules(openedFile);
					if(inputIpRules != null) {
						logTA.append(labels.getLogImportIP() + " " + inputIpRules.size() + "\n");
						outputIpRules.clear();
						start.addActionListener(new StartMenuItemListener(inputIpRules, outputIpRules, outputPanel));
						start.setEnabled(true);
						((CheckBoxPanel)inputPanel.getComponent(0)).setRules(inputIpRules);
						ArrayList<IPRuleIntersection> intersection = getRulesIntersectionList(inputIpRules);
					} 
				}
			}
		});
		
		exitItem.addActionListener(new ExitMenuItemListener());
		
		exportFileItem.setEnabled(false);
		exportFileItem.addActionListener(new ExportFileMenuItemListener());
		
		fileMenu.add(openFileItem);
		fileMenu.add(exportFileItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(exitItem);
		
		//edit menu settings
		editMenu.add(start);
		start.setEnabled(false);
		
		//help menu settings
		helpMenu.add(about);
		about.addActionListener(new AboutMenuItemListener());
		
		//main menu bar settings
		JMenuBar mainMenuBar = new JMenuBar();
		mainMenuBar.add(fileMenu);
		mainMenuBar.add(editMenu);
		mainMenuBar.add(helpMenu);
		
		return mainMenuBar;
	}
	
	private ArrayList<IPRule> getIPRules(File file) {
    BufferedReader br;
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      logTA.append(labels.getFileOpen() + " " + file.getPath() + "\n");
    } catch (FileNotFoundException e) {
      logTA.append(labels.getFileOpenError() + " " + file.getAbsolutePath() + "\n");
      return null;
    }
    ArrayList<IPRule> res = new ArrayList<IPRule>();
    try {
      String row;
      while((row = br.readLine()) != null){
        IPRule rule = IPRule.createRule(row);
        if(rule != null){
          res.add(rule);
        } else {
          logTA.append(IPRule.log + "\n");
        }
      }
    } catch (IOException e) {
      logTA.append(labels.getFileReadError() + " " + file.getAbsolutePath() + "\n");
      return null;
    } finally {
      try {
        br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return res;
  }
	
	private ArrayList<IPRuleIntersection> getRulesIntersectionList(ArrayList<IPRule> inputIpRules) {
	  ArrayList<IPRuleIntersection> res = new ArrayList<IPRuleIntersection>();
	  for (int i = 0; i < inputIpRules.size() - 1; i++){
      IPRule irule = inputIpRules.get(i);
      for(int j = i + 1; j < inputIpRules.size(); j++){
        IPRuleIntersection inter = irule.intersection(inputIpRules.get(j));
        res.add(inter);
        //TODO:
        /*if(inter.isEmpty()) {
          
        }*/
      }
    }
    return res;
  }

}
