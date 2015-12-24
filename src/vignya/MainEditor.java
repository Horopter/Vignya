package vignya;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainEditor
{
   private JFrame menuArea;
   private JPanel contentArea;
   private JTextPane notesText;
   public String fileName = "(untitled)";
   final String titular = " Vignya (version 1)";
   public MainEditor()
    {
      prepareGUI();
    }
   class Handler
   {
       private void saveConfirm(String userInput, boolean full)
        {
                JFileChooser fileChooser = new JFileChooser();
                if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(menuArea)) 
                {
                        // read the file name and call the saveIt method.
                        String fileName = fileChooser.getSelectedFile().getName();
                        if (fileName != null && !fileName.endsWith(".txt")) {
                                fileName = fileName + ".txt";
                        }
                        File file = new File(fileChooser.getSelectedFile().getParent() + File.separator + fileName);
                        menuArea.setTitle(file.getName()+titular);
                        saveIt(file, userInput,full);
                }
        }
        private void saveIt(File file, String userInput, boolean value)
        {
            if(value==true)
            {
                if(!file.exists()) 
                {
                        // Write mode
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                                writer.write(userInput, 0, userInput.length());
                        } catch (IOException exception) {
                                //
                                JOptionPane.showMessageDialog(menuArea, "Error saving the file." + exception.getMessage() , "Error saving file!", JOptionPane.ERROR_MESSAGE);
                        }
                        
                }
                else 
                {
                        // Append mode
                        try {
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                                writer.append(userInput);
                            }
                        } catch (IOException exception) {
                                //
                                JOptionPane.showMessageDialog(menuArea, "Error saving the file." + exception.getMessage() , "Error saving file!", JOptionPane.ERROR_MESSAGE);
                        }
                }
        }
            else
            {
                try 
                {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) 
                    {
                                writer.append(userInput);
                    }
                } 
                catch (IOException exception)
                {
                    JOptionPane.showMessageDialog(menuArea, "Error saving the file." + exception.getMessage() , "Error saving file!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
   }
   class NewHandler
   {
       private boolean isNotEmpty(String str) 
        {
            return str != null && !"".equals(str.trim());
        }
        private void NewCommand()
        {
            String userInput = notesText.getText(); 
                        if (isNotEmpty(userInput)) {    
                                int choice = JOptionPane.showConfirmDialog(menuArea, "Save this file before creating a new one?", "Save file?", JOptionPane.YES_NO_OPTION);
                                if (choice == JOptionPane.YES_OPTION) {
                                                Handler h = new Handler();
                                                h.saveConfirm(userInput,true);
                                } else {
                                        notesText.setText("");                         
                                }
                        } else {
                                notesText.setText("");
                        }
        }
   }
   
   class OpenHandler
   {
       private String openFile(File file) throws IOException {
                StringBuilder builder = null;
                if(file.exists()) {
                    //
                    
                    builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        char[] characters = new char[4096];
                        int i;
                        while ((i = reader.read(characters, 0, characters.length)) != -1) {
                            builder.append(characters);
                        }
                    }
                }
                return builder != null ? builder.toString().trim() : "";
        }
       private void OpenCommand() throws IOException
       {
            JFileChooser fileChooser = new JFileChooser();
            if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(menuArea)) 
            {
                File file = fileChooser.getSelectedFile();
                System.out.println("File selected" + file.getName());
                menuArea.setTitle(file.getName()+titular);
                notesText.setText(openFile(file));
                notesText.setCaretPosition(0);
            }
       }
   }
   
   class SaveHandler
   {
       private void SaveCommand(boolean tf)
       {
           Handler h = new Handler();
           h.saveConfirm(notesText.getText(),tf);
       }
   }
   
   public static void main(String[] args)
   {
      MainEditor  swingMenuDemo = new MainEditor();     
      swingMenuDemo.showMenuDemo();
      swingMenuDemo.showTextPaneDemo();
   }
   private void prepareGUI()
   {
      menuArea = new JFrame(fileName+titular);
      menuArea.setSize(500,500);
      menuArea.setLayout(new GridLayout(1,1));

      menuArea.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      contentArea = new JPanel();
      contentArea.setLayout(new FlowLayout());
      menuArea.add(contentArea);
      menuArea.setVisible(true);  
   }
   
   private void showMenuDemo()
   {
      final JMenuBar menuBar = new JMenuBar();

      JMenu fileMenu = new JMenu("File");
      JMenu editMenu = new JMenu("Edit"); 

     
      JMenuItem newMenuItem = new JMenuItem("New                Ctrl+N");
      newMenuItem.setMnemonic(KeyEvent.VK_N);
      newMenuItem.setActionCommand("New");
      newMenuItem.addActionListener((ActionEvent ae) -> {
          MainEditor.NewHandler n = new NewHandler();
          n.NewCommand();
          //System.out.println("Finished");
      });

      JMenuItem openMenuItem = new JMenuItem("Open              Ctrl+O");
      openMenuItem.setMnemonic(KeyEvent.VK_O);
      openMenuItem.setActionCommand("Open");
      openMenuItem.addActionListener((ActionEvent ae) -> {
          MainEditor.OpenHandler n = new OpenHandler();
          try {
              n.OpenCommand();
              //System.out.println("Finished");
          } catch (IOException ex) {
              Logger.getLogger(MainEditor.class.getName()).log(Level.SEVERE, null, ex);
          }
      });

      JMenuItem saveMenuItem = new JMenuItem("Save               Ctrl+S");
      saveMenuItem.setMnemonic(KeyEvent.VK_S);
      saveMenuItem.setActionCommand("Save");
      saveMenuItem.addActionListener((ActionEvent ae) -> {
          MainEditor.SaveHandler n = new SaveHandler();
          n.SaveCommand(true);
          //System.out.println("Finished");
      });
      
      JMenuItem saveAsMenuItem = new JMenuItem("SaveAs               ");
      saveAsMenuItem.setActionCommand("SaveAs");
      saveAsMenuItem.addActionListener((ActionEvent ae) -> {
          MainEditor.SaveHandler n = new SaveHandler();
          n.SaveCommand(false);
          //System.out.println("Finished");
      });
      
      JMenuItem exitMenuItem = new JMenuItem("Exit                 Ctrl+X");
      newMenuItem.setMnemonic(KeyEvent.VK_X);
      exitMenuItem.setActionCommand("Exit");
      exitMenuItem.addActionListener((ActionEvent ae) -> {
          if(ae.getActionCommand().equals("Exit") &&
                  JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(menuArea, "Are you sure you want to exit?", "Quit!", JOptionPane.YES_NO_OPTION))
          {
              menuArea.dispose();
              System.exit(0);
          }
          //System.out.println("Finished");
      });

      JMenuItem undoMenuItem = new JMenuItem("Undo                 Ctrl+Z");
      undoMenuItem.setMnemonic(KeyEvent.VK_Z);
      undoMenuItem.setActionCommand("Undo");
      
      JMenuItem findMenuItem = new JMenuItem("Find                   Ctrl+F");
      findMenuItem.setMnemonic(KeyEvent.VK_F);
      findMenuItem.setActionCommand("Find");
      
      JMenuItem replaceMenuItem = new JMenuItem("Replace           Ctrl+R");
      replaceMenuItem.setMnemonic(KeyEvent.VK_R);
      replaceMenuItem.setActionCommand("Replace");
      
      fileMenu.add(newMenuItem);
      fileMenu.add(openMenuItem);
      fileMenu.add(saveMenuItem);
      fileMenu.add(saveAsMenuItem);
      fileMenu.addSeparator();
      fileMenu.add(exitMenuItem);        
      fileMenu.setBackground(Color.WHITE);
      
      editMenu.add(undoMenuItem);
      editMenu.addSeparator();
      editMenu.add(findMenuItem);
      editMenu.add(replaceMenuItem);
      editMenu.setBackground(Color.WHITE);
      
      
      menuBar.add(fileMenu);
      menuBar.add(editMenu);

      menuArea.setJMenuBar(menuBar);
      menuArea.setVisible(true);     
   }

   private void showTextPaneDemo()
   {
	      notesText =  new JTextPane();
	      JScrollPane scrollPane = new JScrollPane(notesText);
              TextLineNumber tln = new TextLineNumber(notesText);
              scrollPane.setRowHeaderView( tln );
	      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	      contentArea.setLayout(new BorderLayout());
	      contentArea.add(scrollPane,BorderLayout.CENTER);
	      menuArea.setVisible(true); 
   }
}
