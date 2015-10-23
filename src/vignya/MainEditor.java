package vignya;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainEditor {
   private JFrame mainFrame;
   private JPanel controlPanel; 
   public String fileName = "(untitled)";
   public MainEditor(){
      prepareGUI();
   }

   public static void main(String[] args){
      MainEditor  swingMenuDemo = new MainEditor();     
      swingMenuDemo.showMenuDemo();
      swingMenuDemo.showTextAreaDemo();
   }
   
   private void prepareGUI(){
      mainFrame = new JFrame(fileName+" Vignya (version 1)");
      mainFrame.setSize(500,500);
      mainFrame.setLayout(new GridLayout(1,1));

      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());
      mainFrame.add(controlPanel);
      mainFrame.setVisible(true);  
   }

   private void showMenuDemo(){
      final JMenuBar menuBar = new JMenuBar();

      JMenu fileMenu = new JMenu("File");
      JMenu editMenu = new JMenu("Edit"); 

     
      JMenuItem newMenuItem = new JMenuItem("New                Ctrl+N");
      newMenuItem.setMnemonic(KeyEvent.VK_N);
      newMenuItem.setActionCommand("New");

      JMenuItem openMenuItem = new JMenuItem("Open              Ctrl+O");
      openMenuItem.setActionCommand("Open");

      JMenuItem saveMenuItem = new JMenuItem("Save               Ctrl+S");
      saveMenuItem.setActionCommand("Save");

      JMenuItem exitMenuItem = new JMenuItem("Exit                 Ctrl+X");
      exitMenuItem.setActionCommand("Exit");

      JMenuItem cutMenuItem = new JMenuItem("Cut");
      cutMenuItem.setActionCommand("Cut");

      JMenuItem copyMenuItem = new JMenuItem("Copy");
      copyMenuItem.setActionCommand("Copy");

      JMenuItem pasteMenuItem = new JMenuItem("Paste");
      pasteMenuItem.setActionCommand("Paste");

      fileMenu.add(newMenuItem);
      fileMenu.add(openMenuItem);
      fileMenu.add(saveMenuItem);
      fileMenu.addSeparator();
      fileMenu.add(exitMenuItem);        
      editMenu.add(cutMenuItem);
      editMenu.add(copyMenuItem);
      editMenu.add(pasteMenuItem);
      fileMenu.setBackground(Color.WHITE);
      editMenu.setBackground(Color.WHITE);
      menuBar.add(fileMenu);
      menuBar.add(editMenu);


      mainFrame.setJMenuBar(menuBar);
      mainFrame.setVisible(true);     
   }

   private void showTextAreaDemo(){

	      final JTextArea commentTextArea =  new JTextArea();

	      JScrollPane scrollPane = new JScrollPane(commentTextArea);
	      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	      controlPanel.setLayout(new BorderLayout());
	      controlPanel.add(scrollPane,BorderLayout.CENTER);
	      mainFrame.setVisible(true);  
   }
}