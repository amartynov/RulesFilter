package org.filter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import org.filter.dto.IPRule;

public class OpenFileMenuItemListener implements ActionListener {
  
  private JTextArea logTA;

  private JFileChooser jfc;
  private ArrayList<IPRule> inputIpRules;
  private JFrame jfrm;
  private JMenuItem start; 
  
  public OpenFileMenuItemListener(JFrame jfrm, JTextArea logTA) {
    this.jfrm = jfrm;
    this.logTA = logTA;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    jfc = new JFileChooser();
    int retVal = jfc.showOpenDialog(jfrm);
    if(retVal == JFileChooser.APPROVE_OPTION) {
      inputIpRules = getIPRules(jfc.getSelectedFile());
      if(inputIpRules != null) {
        logTA.append("Import ip rules: " + inputIpRules.size() + "\n");
        start.setEnabled(true);
//        ((CheckBoxPanel)inputPanel.getComponent(0)).setRules(inputIpRules);
      } 
    }
    
  }
  
  private ArrayList<IPRule> getIPRules(File file) {
    BufferedReader br;
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      logTA.append("Open file: " + file.getPath() + "\n");
    } catch (FileNotFoundException e) {
      logTA.append("Can't open file " + file.getAbsolutePath() + "\n");
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
      logTA.append("Can't read file " + file.getAbsolutePath() + "\n");
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

}
