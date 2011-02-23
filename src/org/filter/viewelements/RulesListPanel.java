package org.filter.viewelements;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.filter.Filter;
import org.filter.GraphicAxises;
import org.filter.Protocol;
import org.filter.listeners.AxisJRBActionListener;
import org.filter.listeners.ProtocolJRBActionListener;

public class RulesListPanel extends JPanel {

  private static final long serialVersionUID = -5143318608294058977L;
  
  private Protocol tecProtocol = null;
  private ArrayList<IPRuleJCheckBox> list;
  private GraphicPanel grPanel;
  
  public RulesListPanel(ArrayList<IPRuleJCheckBox> list, GraphicPanel grPanel, int width, int height) {
    this.list = list;
    this.grPanel = grPanel;
    
    this.setLayout(null);
    
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
    
    ButtonGroup protocolBG = new ButtonGroup();
    JPanel protocolP = new JPanel();
    protocolP.add(new JLabel(Filter.labels.getProtocolLabel() + ":    "));
    protocolP.setLayout(new BoxLayout(protocolP, BoxLayout.Y_AXIS));
    
    for(Protocol prot : Protocol.values()) {
      JRadioButton b = new JRadioButton(prot.getLabel());
      b.addActionListener(new ProtocolJRBActionListener(this, prot));
      protocolP.add(b);
      protocolBG.add(b);
    }
    
    JRadioButton any = new JRadioButton(Filter.labels.getAnyProtocol());
    any.addActionListener(new ProtocolJRBActionListener(this, null));
    protocolP.add(any);
    protocolBG.add(any);
    any.doClick();
    
    JScrollPane XaxisSP = new JScrollPane(xrbp);
    XaxisSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    XaxisSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    XaxisSP.setMaximumSize(new Dimension(width, 40));
    
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
    YaxisSP.setMaximumSize(new Dimension(width, 40));
    
    XaxisSet.add(XaxisSP);
    YaxisSet.add(YaxisSP);
    
    axisSet.add(XaxisSet);
    axisSet.add(YaxisSet);
    
    JPanel grp = new JPanel();
    grp.setLayout(new BoxLayout(grp, BoxLayout.Y_AXIS));
    grp.setBounds(205, 5, width, 350);
    
    JPanel grpX = new JPanel();
    grpX.setLayout(new BoxLayout(grpX, BoxLayout.X_AXIS));
    grpX.add(grPanel);
    grpX.add(protocolP);
    
    grp.add(axisSet);
    grp.add(grpX);
    
    this.add(new CheckBoxPanel(grPanel, list));
    this.add(grp);
    
  }
  
  private ArrayList<JRadioButton> getAxisRadionButtonList(GraphicPanel gp, int axis) {
    ArrayList<JRadioButton> list = new ArrayList<JRadioButton>();
    for(GraphicAxises ga : GraphicAxises.values()){
      JRadioButton jrb = new JRadioButton(ga.getLabel());
      jrb.addActionListener(new AxisJRBActionListener(gp, ga, axis));
      list.add(jrb);
    }
    list.get(0).doClick();
    
    return list;
  }

  public Protocol getTecProtocol() {
    return tecProtocol;
  }

  public void setTecProtocol(Protocol tecProtocol) {
    this.tecProtocol = tecProtocol;
    boolean flag = tecProtocol == null;
    
    for(IPRuleJCheckBox rule : list) {
      rule.setEnabled(tecProtocol == rule.getRule().getProtocol() | flag);
    }
    grPanel.clearRects();
    grPanel.repaint();
  }
  
}
