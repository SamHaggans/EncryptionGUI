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
    //private String password;
    private String openFile;
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
    
    public static String encryptTextWithKey(String toEncrypt, String key) throws Exception{
        String encrypted = "";
        int countChar = 0;
        for (int i = 0; i < toEncrypt.length();i++){
            countChar+=1;
            if (countChar>19){
                //key = shift(key);
            }
            encrypted += encryptChar(toEncrypt.substring(i,i+1),key);
        }
        return encrypted;
    }  
    private static String encryptChar(String chara,String key){
        String masterKey = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*\'\"(){}[]:;<>,./?|-=_+`";
        int charIndex = key.indexOf(chara);
        try{
        return masterKey.substring(charIndex,charIndex+1);}
        catch (Exception err) {
            return chara;
        }
    }
    public static String[] decryptText(String[] info) throws Exception{
        String decrypted = "";
        String toDecrypt = info[0];
        String password = "Password";
        String key = decryptKey(info[1],password);
        String origKey = key;
        int countChar = 0;
        for (int i = 0; i < toDecrypt.length();i++){
            countChar+=1;
            if (countChar>19){
                //key = shift(key);
            }
            decrypted += decryptChar(toDecrypt.substring(i,i+1),key);
        }
        String[] returns = {decrypted,origKey};
        return returns;
    }
    public static String[] decryptPassText(String[] info) throws Exception{
        String decrypted = "";
        String toDecrypt = info[0];
        String password = "Password";
        String key = decryptKey(info[1],password);
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
                //Catch any broken lines that were read, which would mess up finding the ~/ separator character
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
        catch (Exception err) {
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
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        ArrayList<String> lines = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            lines.add(line+System.lineSeparator());
            line = reader.readLine();
        }
        String stringResult = makeString(lines);
        String[] returnArray = {stringResult, lines.get(lines.size()-2),lines.get(lines.size()-1)};
        reader.close();
        return returnArray;
    }
    
    public static void encryptFile(String fileName, String toEncrypt) throws Exception {
        String password = "Password";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        String[] returnVals = encryptText(toEncrypt);
        String keyWrite=encryptKey(returnVals[1],password);
        String passWrite=encryptTextWithKey(password,returnVals[1]);
        writer.write(returnVals[0] + "\n"+keyWrite+ "\n"+passWrite);
        writer.close();
    }
    public static String decryptFile(String fileName) throws Exception {
        String[] returnVals = decryptText(readFrom(fileName));
        return returnVals[0];
    }
    private static String encryptKey(String key, String pass){
        String returnKey = "";
        int selectVal = 0;
        String password = pass;
        String masterKey = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*\'\"(){}[]:;<>,./?|-=_+`";
        while (password.length()<100){
            password+=password;
        }
        for (int i =0;i<key.length();i++){
            selectVal = masterKey.indexOf(password.substring(i,i+1))+masterKey.indexOf(key.substring(i,i+1));
            if (selectVal>92){
                selectVal = selectVal-93;
            }
            returnKey+=masterKey.substring(selectVal,selectVal+1);
        }
        return returnKey;
    }
    private static String decryptKey(String key, String pass){
        String returnKey = "";
        int selectVal = 0;
        String password = pass;
        String masterKey = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*\'\"(){}[]:;<>,./?|-=_+`";
        while (password.length()<100){
            password+=password;
        }
        for (int i =0;i<key.length();i++){
            selectVal = masterKey.indexOf(key.substring(i,i+1))-masterKey.indexOf(password.substring(i,i+1));
            if (selectVal<0){
                selectVal = 93+selectVal;
            }
            returnKey+=masterKey.substring(selectVal,selectVal+1);
        }
        return returnKey;
    }
    public static String decryptPassFile(String fileName) throws Exception, IOException {
        String[] returnVals = decryptPassText(readFrom(fileName));
        return returnVals[0];
    }
    private static String makeString(ArrayList array){
        String addString = "";
        for (int i = 0; i < array.size()-2;i++){
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
        PassPane.setVisible(false);
        try {
            PassText.setText(decryptPassFile("goat.txt"));}
        catch (Exception err){
            System.out.println(err);
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
        PlainShowPanel = new javax.swing.JPanel();
        PlainPanel = new javax.swing.JPanel();
        Encrypt = new javax.swing.JButton();
        FileName = new javax.swing.JLabel();
        FileTextPane = new javax.swing.JScrollPane();
        FileText = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        FindInput = new javax.swing.JButton();
        ServiceFind = new javax.swing.JTextField();
        AddInput = new javax.swing.JToggleButton();
        SelectFile = new javax.swing.JButton();
        PassPane = new javax.swing.JScrollPane();
        PassText = new javax.swing.JTextArea();

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

        javax.swing.GroupLayout PlainShowPanelLayout = new javax.swing.GroupLayout(PlainShowPanel);
        PlainShowPanel.setLayout(PlainShowPanelLayout);
        PlainShowPanelLayout.setHorizontalGroup(
            PlainShowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PlainShowPanelLayout.setVerticalGroup(
            PlainShowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Encrypt.setText("Write Contents");
        Encrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncryptActionPerformed(evt);
            }
        });

        FileName.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
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
                    .addComponent(Encrypt)
                    .addComponent(FileName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(FileTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PlainPanelLayout.setVerticalGroup(
            PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlainPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FileTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PlainPanelLayout.createSequentialGroup()
                        .addComponent(FileName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Encrypt)))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        FindInput.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        FindInput.setText("Find Password");
        FindInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FindInputActionPerformed(evt);
            }
        });

        ServiceFind.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        ServiceFind.setText("Service");
        ServiceFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ServiceFindActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(AddInput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FindInput, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SelectFile)
                .addGap(60, 60, 60)
                .addComponent(ServiceFind, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FindInput)
                    .addComponent(AddInput))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ServiceFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SelectFile))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PassText.setColumns(20);
        PassText.setRows(5);
        PassPane.setViewportView(PassText);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(PlainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PlainShowPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PassPane, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PlainShowPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(PassPane, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addComponent(PlainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void EncryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncryptActionPerformed
       try {
        
        PlainPanel.setVisible(false);
        PlainShowPanel.setVisible(false);
        String toWrite = FileText.getText();
        encryptFile(openFile,toWrite);
       }
        catch (Exception err){
            System.out.println("BAD"+err);
            return;
        }
    }//GEN-LAST:event_EncryptActionPerformed

    private void AddInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddInputActionPerformed
    try {
        JTextField serviceEnter = new JTextField();
        JTextField usernameEnter = new JTextField();
        JTextField emailEnter = new JTextField();
        JTextField passwordEnter = new JTextField();
        Object[] description = {"Input what you wish to add as a password below. Note that you MUST put in a value for all four boxes. If you do not have a value for that, just type anything like N/A.","Service:", serviceEnter, "Username:", usernameEnter, "Email:", emailEnter, "Password:", passwordEnter};
        JOptionPane.showConfirmDialog(null, description, "Add a service", JOptionPane.OK_CANCEL_OPTION);
        PassText.append(System.lineSeparator() + serviceEnter.getText() + "~/" + usernameEnter.getText() + "~/" + emailEnter.getText() + "~/" + passwordEnter.getText());
        String toWrite = PassText.getText();
        encryptFile("goat.txt", toWrite);
        }
        catch (Exception err)
        {
            System.out.println(err);
            return;
        }
    try
    {
      PassText.setText(decryptPassFile("goat.txt"));
    }
    catch (Exception err) {System.out.println(err);}
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
        try {
            JFileChooser selectFile = new JFileChooser();
            selectFile.showOpenDialog(MainPanel);
            String openFiley = selectFile.getSelectedFile().getAbsolutePath();
            String fileExtension = openFiley.substring(openFiley.indexOf("."),openFiley.length());
            while (openFiley.equals("goat.txt")||!fileExtension.equals(".txt")){
                JOptionPane.showMessageDialog(MainPanel, "You must select a .txt file, and it cannot be the main passwords file (goat.txt)", "Error", 0);
                selectFile = new JFileChooser();
                selectFile.showOpenDialog(MainPanel);
                openFiley = selectFile.getSelectedFile().getAbsolutePath();
                fileExtension = openFiley.substring(openFiley.indexOf(".")-1,openFiley.length());
            }
            openFile = openFiley;
            PlainPanel.setVisible(true);
            PlainShowPanel.setVisible(true);
            FileName.setText("File: " + openFile);
            //selectFile.getSelectedFile().close();
            try {
                String goat = decryptFile(openFiley);
                FileText.setText(goat);
            }
            catch (Exception err){
                System.out.println("WORSE"+err);
                return;
            }
        }catch (Exception err){
            System.out.println("WORSER"+err);//Likely exited from the selection window with nullpointerexception, not an issue
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
    private javax.swing.JScrollPane PassPane;
    private javax.swing.JTextArea PassText;
    private javax.swing.JPanel PlainPanel;
    private javax.swing.JPanel PlainShowPanel;
    private javax.swing.JButton SelectFile;
    private javax.swing.JTextField ServiceFind;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
