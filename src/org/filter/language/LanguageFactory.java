package org.filter.language;

import java.io.InputStreamReader;
import java.util.Properties;

public class LanguageFactory {

  private static final String ext = "prop";
  
  public static LabelList create(String langLabel){
    return getLabelsFromFile(langLabel + "." + ext);
  }

  private static LabelList getLabelsFromFile(String fileName) {
    Properties prop = new Properties();
    try {
      prop.load(new InputStreamReader(LanguageFactory.class.getClassLoader().getResourceAsStream("org/filter/language/" + fileName), "utf-8"));
    } catch (Exception e) {
      return getLabels(null);
    }
    
    return getLabels(prop);
  }

  private static LabelList getLabels(Properties prop) {
    LabelList list = new LabelList();
    list.setProgName(prop == null ? "Filter" : prop.getProperty("prog.name"));
    
    list.setMenuFileLabel(prop == null ? "File" : prop.getProperty("menu.file"));
    list.setMenuFileOpenLabel(prop == null ? "Open" : prop.getProperty("menu.file.open"));
    list.setMenuFileExportLabel(prop == null ? "Export" : prop.getProperty("menu.file.export"));
    list.setMenuFileExitLabel(prop == null ? "Exit" : prop.getProperty("menu.file.exit"));
    
    list.setMenuEditLabel(prop == null ? "Edit" : prop.getProperty("menu.edit"));
    list.setMenuEditStartLabel(prop == null ? "Start" : prop.getProperty("menu.edit.start"));
    
    list.setMenuHelpLabel(prop == null ? "Help" : prop.getProperty("menu.help"));
    list.setMenuHelpAboutLabel(prop == null ? "About" : prop.getProperty("menu.help.about"));
    
    list.setLogImportIP(prop == null ? "Import ip rules:" : prop.getProperty("log.import.ip"));
    
    list.setFileOpen(prop == null ? "Open file:" : prop.getProperty("file.open"));
    list.setFileOpenError(prop == null ? "Can't open file:" : prop.getProperty("file.open.error"));
    list.setFileReadError(prop == null ? "Can't read file:" : prop.getProperty("file.read.error"));
    
    list.setPanelInput(prop == null ? "Input File" : prop.getProperty("panel.input"));
    list.setPanelOutput(prop == null ? "Output File:" : prop.getProperty("panel.output"));
    
    return list;
  }
  
}
