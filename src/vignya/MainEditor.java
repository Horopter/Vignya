package vignya;

import javax.swing.JFrame;
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
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;


public class MainEditor
{
   private JFrame menuArea;
   private JTextArea contentArea;
   private JTextPane notesText;
   private JDialog findDialog;
   private List<Integer> searchIndices;
   private boolean lastDirection = true;
   private boolean directionChanged;
   private int currentSearchIndex;
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
                //System.out.println("File selected" + file.getName());
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
   
   class FindHandler
   {
       private void unhideFindDialog()
       {
                findDialog.setVisible(true);
                searchIndices = null;
                currentSearchIndex = 0; 
        }
       private void createAndShowFindDialog() {
                findDialog = new JDialog(menuArea, "Search");
                findDialog.setSize(240,120);
                findDialog.setLocationByPlatform(true);
                findDialog.setResizable(false);
                Container findPane = findDialog.getContentPane();
                JLabel findLabel = new JLabel("Find");
                JTextField tF = new JTextField(15);
                JLabel dir = new JLabel("Direction");
                ButtonGroup buttons = new ButtonGroup();
                JRadioButton forward = new JRadioButton("Forward", true);
                JRadioButton backward = new JRadioButton("Backward");
                buttons.add(forward);
                buttons.add(backward);
 
                JCheckBox checkBox = new JCheckBox("Insensitive");
 
                JButton searchButton = new JButton("Search");
                searchButton.setActionCommand("Search");
                searchButton.addActionListener((ActionEvent ae) -> {
                    JTextField tF1 = (JTextField)findDialog.getContentPane().getComponent(1);
                    JRadioButton forward1 = (JRadioButton)findDialog.getContentPane().getComponent(3);
                    boolean isForward = forward1.isSelected();
                    String key = tF1.getText();
                    String contentText = notesText.getText();
                    //System.out.println(key + contentText);
                    if (key == null || "".equals(key.trim())) {
                        JOptionPane.showMessageDialog(findDialog, "Please enter a word to search!");
                        return;
                    }
                    directionChanged = lastDirection ^ isForward;
                    lastDirection = isForward;
                    if (searchIndices == null) {
                        searchIndices = new ArrayList<>();
                        int index = 0;
                        while (contentText.indexOf(key, index + key.length()) != -1) {
                            index = contentText.indexOf(key, index + key.length());
                            searchIndices.add(index);
                            //System.out.println("Index:" + index);
                            Highlighter highlighter = notesText.getHighlighter();
                            HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
                            int p0 = contentText.indexOf(key);
                            int p1 = p0 + key.length();
                            try {
                                highlighter.addHighlight(p0, p1, painter );
                            } catch (BadLocationException ex) {
                                Logger.getLogger(MainEditor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    if (currentSearchIndex >= searchIndices.size() || currentSearchIndex < 0) {
                        if (directionChanged && currentSearchIndex >= searchIndices.size()) {
                            currentSearchIndex = searchIndices.size() - 2;
                            //System.out.println("Reducing the searchIndex by 2");
                        } else if (directionChanged && currentSearchIndex < 0) {
                            currentSearchIndex = 1;
                            //System.out.println("Reducing the searchIndex to 1");
                        } else {
                            //System.out.println("currentSearchIndex:" + currentSearchIndex);
                            if (searchIndices.size() > 0) {
                                JOptionPane.showMessageDialog(findDialog, "No more search!");
                            } else {
                                JOptionPane.showMessageDialog(findDialog, "Keyword not found!");
                            }
                            return;
                        }
                    }
                    if (isForward) {
                        //System.out.println("Searching in forward direction.");
                        int contentIndex = searchIndices.get(currentSearchIndex);
                        contentArea.select(contentIndex, contentIndex + key.length());
                        currentSearchIndex++;
                    } else {
                        //System.out.println("Searching in reverse direction.");
                        int contentIndex = searchIndices.get(currentSearchIndex);
                        contentArea.select(contentIndex, contentIndex + key.length());
                        currentSearchIndex--;
                    }
                });                           
 
                GridBagLayout layout = new GridBagLayout();
                findDialog.setLayout(layout);
                GridBagConstraints fieldprops = new GridBagConstraints();
                GridBagConstraints labelConstraints = new GridBagConstraints();
 
                labelConstraints.weightx = 0.0;
                labelConstraints.gridwidth = 1;
                labelConstraints.anchor = GridBagConstraints.WEST;
                layout.addLayoutComponent(findLabel, labelConstraints);
 
                fieldprops.weightx = 1.0;
                fieldprops.gridwidth = GridBagConstraints.REMAINDER;
                fieldprops.anchor = GridBagConstraints.WEST;
                fieldprops.fill = GridBagConstraints.HORIZONTAL;
                layout.addLayoutComponent(tF, fieldprops);
 
                labelConstraints.weightx = 0.0;
                labelConstraints.gridwidth = 1;
                labelConstraints.anchor = GridBagConstraints.WEST;                              
                layout.addLayoutComponent(dir, labelConstraints);
 
                fieldprops.weightx = 0.0;
                fieldprops.gridwidth = 1;
                fieldprops.anchor = GridBagConstraints.WEST;
                layout.addLayoutComponent(forward, fieldprops);
 
                fieldprops.weightx = 0.0;
                fieldprops.gridwidth = GridBagConstraints.REMAINDER;
                fieldprops.anchor = GridBagConstraints.WEST;
                layout.addLayoutComponent(backward, fieldprops);
 
                fieldprops.weightx = 0.0;
                fieldprops.gridwidth = 1;
                fieldprops.anchor = GridBagConstraints.WEST;
 
                layout.addLayoutComponent(checkBox, fieldprops);
 
                fieldprops.weightx = 0.0;
                fieldprops.gridwidth = GridBagConstraints.REMAINDER;
                fieldprops.anchor = GridBagConstraints.EAST;
                layout.addLayoutComponent(searchButton, fieldprops);
 
                findPane.add(findLabel);
                findPane.add(tF);
                findPane.add(dir);
                findPane.add(forward);
                findPane.add(backward);
                findPane.add(checkBox);
                findPane.add(searchButton);                                
                
                //findDialog.pack();
                findDialog.setVisible(true);
        }
       private void FindCommand()
       {
           if (findDialog != null) {
                                unhideFindDialog();
                        } else {
                                createAndShowFindDialog();
                        }
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
         public void menuAreaClosing(WindowEvent menuAreaEvent){
            System.exit(0);
         }        
      });    
      contentArea = new JTextArea();
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
      });

      JMenuItem openMenuItem = new JMenuItem("Open              Ctrl+O");
      openMenuItem.setMnemonic(KeyEvent.VK_O);
      openMenuItem.setActionCommand("Open");
      openMenuItem.addActionListener((ActionEvent ae) -> {
          MainEditor.OpenHandler n = new OpenHandler();
          try {
              n.OpenCommand();
              
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
          
      });
      
      JMenuItem saveAsMenuItem = new JMenuItem("SaveAs               ");
      saveAsMenuItem.setActionCommand("SaveAs");
      saveAsMenuItem.addActionListener((ActionEvent ae) -> {
          MainEditor.SaveHandler n = new SaveHandler();
          n.SaveCommand(false);
          
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
          
      });
      
      JMenuItem findMenuItem = new JMenuItem("Find                   Ctrl+F");
      findMenuItem.setMnemonic(KeyEvent.VK_F);
      findMenuItem.setActionCommand("Find");
      findMenuItem.addActionListener((ActionEvent ae) -> {
          MainEditor.FindHandler n = new FindHandler();
          n.FindCommand();
      });
      
      
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
