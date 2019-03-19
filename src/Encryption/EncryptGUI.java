package Encryption;

/**
 * @author Sam Haggans
 */
import java.util.Scanner;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
public class EncryptGUI extends javax.swing.JFrame {
    private static ArrayList<String> services = new ArrayList<String>();
    private static ArrayList<String> usernames = new ArrayList<String>();
    private static ArrayList<String> emails = new ArrayList<String>();
    private static ArrayList<String> pws = new ArrayList<String>();
    private String password;
    public static String[] encryptText(String toEncrypt) throws Exception{
        String encrypted = "";
        String key = shuffle("abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*\'\"(){}[]:;<>,./?|-=_+`");
        int countChar = 0;
        for (int i = 0; i < toEncrypt.length();i++){
            countChar+=1;
            if (countChar>19){
                //key = shift(key);
            }
            encrypted += encryptChar(toEncrypt.substring(i,i+1),key);
        }
        String[] returns = {encrypted,key};
        return returns;
    }
    
    private static String encryptChar(String chara,String key){
        String masterKey = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*\'\"(){}[]:;<>,./?|-=_+`";
        int charIndex = key.indexOf(chara);
        try{
        return masterKey.substring(charIndex,charIndex+1);}
        catch (Exception StringIndexOutOfBoundsException) {
            return chara;
        }
    }
    
    public static String[] decryptText(String[] info) throws Exception{
        String decrypted = "";
        String toDecrypt = info[0];
        String key = info[1];
        String origKey = key;
        int countChar = 0;
        for (int i = 0; i < toDecrypt.length();i++){
            countChar+=1;
            if (countChar>19){
                //key = shift(key);
            }
            decrypted += decryptChar(toDecrypt.substring(i,i+1),key);
        }
        
        ArrayList<String> linesList = new ArrayList<String>();
        String[] lines = decrypted.split(System.lineSeparator(),10000);
        for (int i = 0; i< lines.length;i++){
            linesList.add(lines[i]);
        }
        for (int i = 0; i< linesList.size();i++){
            try {
                if (linesList.get(i).split("~/",10)[0].length()>1){ //catch any empty lines from showing up as a service and ruining indexing
                    services.add(linesList.get(i).split("~/",10)[0]);
                }
            usernames.add(linesList.get(i).split("~/",10)[1]);
            emails.add(linesList.get(i).split("~/",10)[2]);
            pws.add(linesList.get(i).split("~/",10)[3]);
            }catch (Exception err){
                System.out.print("");//Catch any empty lines that were read, which would mess up finding the ~/ separator character
            }
        }
        String[] returns = {decrypted,origKey};
        return returns;
    }
    private static String decryptChar(String chara,String key){
        String masterKey = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*\'\"(){}[]:;<>,./?|-=_+`";
        int charIndex = masterKey.indexOf(chara);
        try{
        return key.substring(charIndex,charIndex+1);}
        catch (Exception StringIndexOutOfBoundsException) {
            return chara;
        }
    }
    private static String shuffle(String text) {
        char[] characters = text.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = (int)(Math.random() * characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }
    
    private static String shift(String keyStart) throws Exception{
        String returnKey = "";
        String masterKey = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*\'\"(){}[]:;<>,./?|-=_+`";
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        BufferedReader reader = new BufferedReader(new FileReader(s+"/s.txt"));
        ArrayList<String> lines = new ArrayList<String>();
        //System.out.println("Purging array");
        //purgeArrayList(lines);
        String line = reader.readLine();
        while (line != null) {
            lines.add(line+System.lineSeparator());
            line = reader.readLine();
        }
        reader.close();
        int keyVal;
        keyVal = masterKey.indexOf("Q");
        String shiftKey = lines.get(keyVal);
        int shiftVal;
        int newVal;
        for (int i = 0; i< keyStart.length();i++){
            keyVal = masterKey.indexOf(keyStart.substring(i,i+1));
            shiftVal = masterKey.indexOf(shiftKey.substring(i,i+1));
            newVal = keyVal+shiftVal;
            if (newVal >92){
                newVal -=93;
            }
            returnKey+=masterKey.substring(newVal,newVal+1);
        }
        return returnKey;
    }
    
    public static String[] readFrom(String fileName) throws Exception{
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        BufferedReader reader = new BufferedReader(new FileReader(s+"/"+fileName));
        ArrayList<String> lines = new ArrayList<String>();
        //System.out.println("Purging array");
        //purgeArrayList(lines);
        String line = reader.readLine();
        while (line != null) {
            lines.add(line+System.lineSeparator());
            line = reader.readLine();
        }
        String stringResult = makeString(lines);
        String[] returnArray = {stringResult, lines.get(lines.size()-1)};
        reader.close();
        return returnArray;
    }
    
    public static void encryptFile(String fileName, String toEncrypt) throws Exception {
        Scanner userInput = new Scanner(System.in);
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        //System.out.println("Enter File to Encrypt: ");
        //String fileName = userInput.nextLine();
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        //System.out.println("Enter Text to Decrypt: ");
        //String toDecrypt = userInput.nextLine();
        //System.out.println("Enter Key for Decryption: ");
        //String key = userInput.nextLine();
        //System.out.println("Enter Text to Encrypt: ");
        //String toEncrypt = userInput.nextLine();
        String[] returnVals;
        returnVals = encryptText(toEncrypt);
        writer.write(returnVals[0] + "\n"+returnVals[1]);
        writer.close();
    }
    public static String decryptFile(String fileName) throws Exception, IOException {
        //Scanner userInput = new Scanner(System.in);
        Path currentRelativePath = Paths.get("");
        //System.out.println("Enter File to Decrypt: ");
        //String fileName = userInput.nextLine();
        String s = currentRelativePath.toAbsolutePath().toString();
        //System.out.println("Enter Text to Decrypt: ");
        //String toDecrypt = userInput.nextLine();
        String[] returnVals;
        returnVals = decryptText(readFrom(fileName));
        //BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        //writer.write(returnVals[0]);
        //writer.close();
        return returnVals[0];
    }
    private static String makeString(ArrayList array){
        String addString = "";
        for (int i = 0; i < array.size()-1;i++){
            addString += array.get(i);
        }
        return addString;
    }
    
    public EncryptGUI() {
        initComponents();
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        File sFile = new File(s+"/s.txt");
        if (!sFile.exists()){
            try {
                generateS();
            }catch (Exception error){}
        }
        PlainPanel.setVisible(false);
        PlainShowPanel.setVisible(false);
        try {
        FileText.setText(decryptFile("goat.txt"));}
        catch (Exception error){
            return;
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPanel = new javax.swing.JPanel();
        ServiceFind = new javax.swing.JTextField();
        FindInput = new javax.swing.JButton();
        PlainShowPanel = new javax.swing.JPanel();
        PlainPanel = new javax.swing.JPanel();
        Encrypt = new javax.swing.JButton();
        FileName = new javax.swing.JLabel();
        FileTextPane = new javax.swing.JScrollPane();
        FileText = new javax.swing.JTextArea();
        AddInput = new javax.swing.JToggleButton();
        SelectFile = new javax.swing.JButton();

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ServiceFind.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        ServiceFind.setText("Service");
        ServiceFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ServiceFindActionPerformed(evt);
            }
        });

        FindInput.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        FindInput.setText("Find Password");
        FindInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FindInputActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PlainShowPanelLayout = new javax.swing.GroupLayout(PlainShowPanel);
        PlainShowPanel.setLayout(PlainShowPanelLayout);
        PlainShowPanelLayout.setHorizontalGroup(
            PlainShowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 256, Short.MAX_VALUE)
        );
        PlainShowPanelLayout.setVerticalGroup(
            PlainShowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 183, Short.MAX_VALUE)
        );

        Encrypt.setText("Write Contents");
        Encrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncryptActionPerformed(evt);
            }
        });

        FileName.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        FileName.setText("File: goat.txt");

        FileText.setColumns(20);
        FileText.setRows(5);
        FileTextPane.setViewportView(FileText);

        javax.swing.GroupLayout PlainPanelLayout = new javax.swing.GroupLayout(PlainPanel);
        PlainPanel.setLayout(PlainPanelLayout);
        PlainPanelLayout.setHorizontalGroup(
            PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlainPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FileName)
                    .addGroup(PlainPanelLayout.createSequentialGroup()
                        .addComponent(Encrypt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FileTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        PlainPanelLayout.setVerticalGroup(
            PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlainPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(FileName)
                .addGap(18, 18, 18)
                .addGroup(PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Encrypt)
                    .addComponent(FileTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        AddInput.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        AddInput.setText("Add A Password");
        AddInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddInputActionPerformed(evt);
            }
        });

        SelectFile.setText("Select a Plaintext File");
        SelectFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(ServiceFind, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(FindInput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SelectFile)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AddInput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(PlainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PlainShowPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(140, 140, 140))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ServiceFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FindInput)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(SelectFile)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PlainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addComponent(AddInput))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(PlainShowPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void EncryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncryptActionPerformed
       try {
        PlainPanel.setVisible(false);
        PlainShowPanel.setVisible(false);
        String toWrite = FileText.getText();
        encryptFile("goat.txt",toWrite);
       }
        catch (Exception IOException){
            return;
        }
    }//GEN-LAST:event_EncryptActionPerformed

    private void AddInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddInputActionPerformed
    try {
        JTextField serviceEnter = new JTextField();
        JTextField usernameEnter = new JTextField();
        JTextField emailEnter = new JTextField();
        JTextField passwordEnter = new JTextField();
        Object[] description = {"Service:", serviceEnter, "Username:", usernameEnter, "Email:", emailEnter, "Password:", passwordEnter};
        JOptionPane.showConfirmDialog(null, description, "Add a service", JOptionPane.OK_CANCEL_OPTION);
        FileText.append(System.lineSeparator() + serviceEnter.getText() + "~/" + usernameEnter.getText() + "~/" + emailEnter.getText() + "~/" + passwordEnter.getText());
        String toWrite = this.FileText.getText();
        encryptFile("goat.txt", toWrite);
        }
        catch (Exception IOException)
        {
            System.out.println("bad");
            return;
        }
    try
    {
      this.FileText.setText(decryptFile("goat.txt"));
    }
    catch (Exception IOException) {}
    }//GEN-LAST:event_AddInputActionPerformed

    private void ServiceFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ServiceFindActionPerformed
        
    }//GEN-LAST:event_ServiceFindActionPerformed

    private void FindInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FindInputActionPerformed
    int index = services.indexOf(ServiceFind.getText());
    try
    {
      JOptionPane.showMessageDialog(MainPanel, 
        "Username: " + usernames.get(index) + "\nEmail: " + emails.get(index) + "\nPassword: " + pws.get(index), 
        "INFORMATION FOR " + services.get(index), 
        -1);
    }
    catch (Exception err)
    {
      JOptionPane.showMessageDialog(MainPanel, "There is no password for that service. Check your spelling or create a new password.", "Error", 0);
    }
    ServiceFind.setText("Service");
    }//GEN-LAST:event_FindInputActionPerformed

    private void SelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectFileActionPerformed
        JFileChooser selectFile = new JFileChooser();
        selectFile.showOpenDialog(MainPanel);
        String openFile = selectFile.getSelectedFile().getName();
        String fileExtension = openFile.substring(openFile.indexOf("."),openFile.length());
        while (openFile.equals("goat.txt")||!fileExtension.equals(".txt")){
            JOptionPane.showMessageDialog(MainPanel, "You must select a .txt file, and it cannot be the main passwords file (goat.txt)", "Error", 0);
            selectFile = new JFileChooser();
            selectFile.showOpenDialog(MainPanel);
            openFile = selectFile.getSelectedFile().getName();
            fileExtension = openFile.substring(openFile.indexOf(".")-1,openFile.length());
        }
        PlainPanel.setVisible(true);
        PlainShowPanel.setVisible(true);
        try {
            FileText.setText(decryptFile(openFile));
        }
        catch (Exception IOException){
            return;
        }
        
    }//GEN-LAST:event_SelectFileActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EncryptGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EncryptGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EncryptGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EncryptGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EncryptGUI().setVisible(true); 
            }
            
        });
        
    }
    public static void generateS() throws IOException {   
        String masterKey = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*\'\"(){}[]:;<>,./?|-=_+`";
         for (int i = 0; i < masterKey.length();i++) {
                String sGen =  shuffle(masterKey);
                BufferedWriter writer = new BufferedWriter(new FileWriter("s.txt", true));
                writer.append(sGen);
                if (i != masterKey.length()-1){
                    writer.append("\n");}
                writer.close();
        }
    }
    public static void generateP() throws IOException {   
        String masterKey = "01234";
         for (int i = 0; i < 93;i++) {
                String pGen =  shuffle(masterKey).substring(0,1);
                BufferedWriter writer = new BufferedWriter(new FileWriter("p.txt", true));
                writer.append(pGen);
                if (i != 92){
                    writer.append("\n");}
                writer.close();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton AddInput;
    private javax.swing.JButton Encrypt;
    private javax.swing.JLabel FileName;
    private javax.swing.JTextArea FileText;
    private javax.swing.JScrollPane FileTextPane;
    private javax.swing.JButton FindInput;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JPanel PlainPanel;
    private javax.swing.JPanel PlainShowPanel;
    private javax.swing.JButton SelectFile;
    private javax.swing.JTextField ServiceFind;
    // End of variables declaration//GEN-END:variables
}
