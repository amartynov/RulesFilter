package org.filter.viewelements;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.filter.Filter;
import org.filter.listeners.AboutMenuItemListener;
import org.filter.listeners.AnalizMenuItemListener;
import org.filter.listeners.ExitMenuItemListener;
import org.filter.listeners.ExportFileMenuItemListener;
import org.filter.listeners.OpenFileActionListener;
import org.filter.listeners.StartMenuItemListener;

public class MainMenu extends JMenuBar {

  private static final long serialVersionUID = 5157353429100075543L;
  
  public static JMenuItem startEditItem = new JMenuItem(Filter.labels.getMenuEditStartLabel());
  public static JMenuItem exportFileItem = new JMenuItem(Filter.labels.getMenuFileExportLabel());
  public static JMenuItem analizEditItem = new JMenuItem(Filter.labels.getMenuEditAnalizLabel());

  public MainMenu() {
    //file menu settings
    JMenu fileMenu = new JMenu(Filter.labels.getMenuFileLabel());
    JMenuItem openFileItem = new JMenuItem(Filter.labels.getMenuFileOpenLabel());
    JMenuItem exitItem = new JMenuItem(Filter.labels.getMenuFileExitLabel());
    
    openFileItem.addActionListener(new OpenFileActionListener());
    exitItem.addActionListener(new ExitMenuItemListener());
    
    exportFileItem.setEnabled(false);
    exportFileItem.addActionListener(new ExportFileMenuItemListener());
    
    fileMenu.add(openFileItem);
    fileMenu.add(exportFileItem);
    fileMenu.add(new JSeparator());
    fileMenu.add(exitItem);
    
    //edit menu settings
    JMenu editMenu = new JMenu(Filter.labels.getMenuEditLabel());
    editMenu.add(startEditItem);
    startEditItem.setEnabled(false);
    startEditItem.addActionListener(new StartMenuItemListener());
    editMenu.add(analizEditItem);
    analizEditItem.setEnabled(false);
    analizEditItem.addActionListener(new AnalizMenuItemListener());
    
    //help menu settings
    JMenu helpMenu = new JMenu(Filter.labels.getMenuHelpLabel());
    JMenuItem about = new JMenuItem(Filter.labels.getMenuHelpAboutLabel());
    helpMenu.add(about);
    about.addActionListener(new AboutMenuItemListener());
    
    //menu settings
    this.add(fileMenu);
    this.add(editMenu);
    this.add(helpMenu);
  }
  
  public static void activateAfterFile() {
    startEditItem.setEnabled(true);
    analizEditItem.setEnabled(true);
  }
  
  public static void activateExport() {
    exportFileItem.setEnabled(true);
  }
}
