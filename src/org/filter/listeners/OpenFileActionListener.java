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

import org.filter.Filter;
import org.filter.dto.IPRule;
import org.filter.viewelements.CheckBoxPanel;
import org.filter.viewelements.MainMenu;

public class OpenFileActionListener implements ActionListener {

  @Override
  public void actionPerformed(ActionEvent e) {
    JFileChooser jfc = new JFileChooser();
    int retVal = jfc.showOpenDialog(null);
    if(retVal == JFileChooser.APPROVE_OPTION) {
      File openedFile = jfc.getSelectedFile();
      Filter.clearInput();
      Filter.clearOutput();
      Filter.inputIpRules = getIPRules(openedFile);
      if(Filter.inputIpRules != null) {
        IPRule.list1.clear();
        IPRule.list2.clear();
        Filter.logTA.append(Filter.labels.getLogImportIP() + " " + Filter.inputIpRules.size() + "\n");
        ((CheckBoxPanel)Filter.inputPanel.getComponent(0)).setRules(Filter.inputIpRules);
        Filter.inputGraphic.repaint();
        Filter.outputGraphic.repaint();
        MainMenu.activateAfterFile();
      } 
    }
  }
  
  private ArrayList<IPRule> getIPRules(File file) {
    BufferedReader br;
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      Filter.logTA.append(Filter.labels.getFileOpen() + " " + file.getPath() + "\n");
    } catch (FileNotFoundException e) {
      Filter.logTA.append(Filter.labels.getFileOpenError() + " " + file.getAbsolutePath() + "\n");
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
          Filter.logTA.append(IPRule.log + "\n");
        }
      }
    } catch (IOException e) {
      Filter.logTA.append(Filter.labels.getFileReadError() + " " + file.getAbsolutePath() + "\n");
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
