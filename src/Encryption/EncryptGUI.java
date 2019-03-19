package Encryption;

/**
 * @author Sam Haggans
 */
import java.util.Scanner;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;
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
        jDialog1.setVisible(true);
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
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jDialog1 = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        ServiceFind = new javax.swing.JTextField();
        FindInput = new javax.swing.JButton();
        PlainShowPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        FileTextPane = new javax.swing.JScrollPane();
        FileText = new javax.swing.JTextArea();
        PlainPanel = new javax.swing.JPanel();
        Decrypt = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        Encrypt = new javax.swing.JButton();
        AddPanel = new javax.swing.JPanel();
        ServiceAdd = new javax.swing.JTextField();
        UsernameAdd = new javax.swing.JTextField();
        EmailAdd = new javax.swing.JTextField();
        PasswordAdd = new javax.swing.JTextField();
        AddInput = new javax.swing.JToggleButton();

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

        jLabel2.setText("YOU HAVE DONE SOMETHING");

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(156, 156, 156))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel2)
                .addContainerGap(229, Short.MAX_VALUE))
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

        jLabel1.setText("File: goat.txt");

        FileText.setColumns(20);
        FileText.setRows(5);
        FileTextPane.setViewportView(FileText);

        javax.swing.GroupLayout PlainShowPanelLayout = new javax.swing.GroupLayout(PlainShowPanel);
        PlainShowPanel.setLayout(PlainShowPanelLayout);
        PlainShowPanelLayout.setHorizontalGroup(
            PlainShowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlainShowPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PlainShowPanelLayout.createSequentialGroup()
                .addContainerGap(102, Short.MAX_VALUE)
                .addComponent(FileTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        PlainShowPanelLayout.setVerticalGroup(
            PlainShowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlainShowPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(FileTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        Decrypt.setText("View Contents");
        Decrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DecryptActionPerformed(evt);
            }
        });

        jButton3.setText("Edit File");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        Encrypt.setText("Write Contents");
        Encrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncryptActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PlainPanelLayout = new javax.swing.GroupLayout(PlainPanel);
        PlainPanel.setLayout(PlainPanelLayout);
        PlainPanelLayout.setHorizontalGroup(
            PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Decrypt)
                    .addGroup(PlainPanelLayout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Encrypt)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        PlainPanelLayout.setVerticalGroup(
            PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlainPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(Decrypt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(Encrypt))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ServiceAdd.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        ServiceAdd.setText("Service");
        ServiceAdd.setSelectionStart(0);

        UsernameAdd.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        UsernameAdd.setText("Username");
        UsernameAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernameAddActionPerformed(evt);
            }
        });

        EmailAdd.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        EmailAdd.setText("Email");

        PasswordAdd.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        PasswordAdd.setText("Password");

        AddInput.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        AddInput.setText("Add Password");
        AddInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddInputActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AddPanelLayout = new javax.swing.GroupLayout(AddPanel);
        AddPanel.setLayout(AddPanelLayout);
        AddPanelLayout.setHorizontalGroup(
            AddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddPanelLayout.createSequentialGroup()
                .addComponent(AddInput)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(AddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EmailAdd)
                    .addComponent(ServiceAdd)
                    .addComponent(UsernameAdd, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(AddPanelLayout.createSequentialGroup()
                        .addComponent(PasswordAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
        );
        AddPanelLayout.setVerticalGroup(
            AddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddPanelLayout.createSequentialGroup()
                .addComponent(ServiceAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(UsernameAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(EmailAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PasswordAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(AddInput)
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(190, 190, 190)
                        .addComponent(ServiceFind, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(FindInput))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(AddPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(101, 101, 101)
                        .addComponent(PlainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)
                        .addComponent(PlainShowPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ServiceFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FindInput))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(AddPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(PlainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PlainShowPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void EncryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EncryptActionPerformed
       try {
        String toWrite = FileText.getText();
        encryptFile("goat.txt",toWrite);
       }
        catch (Exception IOException){
            return;
        }
    }//GEN-LAST:event_EncryptActionPerformed

    private void DecryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DecryptActionPerformed
        try {
        FileText.setText(decryptFile("goat.txt"));}
        catch (Exception IOException){
            return;
        }
    }//GEN-LAST:event_DecryptActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void UsernameAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsernameAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UsernameAddActionPerformed

    private void AddInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddInputActionPerformed
        this.FileText.append(System.lineSeparator() + ServiceAdd.getText() + "~/" + UsernameAdd.getText() + "~/" + EmailAdd.getText() + "~/" + PasswordAdd.getText());
    try
    {
      JOptionPane.showInputDialog(
        this.MainPanel, 
        "Add information below to add a password into the records. It will be encrypted and safe.", 
        "Add Password", 
        -1, 
        null, 
        null, 
        "Service");
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
        int index = services.indexOf(this.ServiceFind.getText());
    try
    {
      JOptionPane.showMessageDialog(this.MainPanel, 
        "Username: " + usernames.get(index) + "\nEmail: " + emails.get(index) + "\nPassword: " + pws.get(index), 
        "INFORMATION FOR " + services.get(index), 
        -1);
    }
    catch (Exception err)
    {
      JOptionPane.showMessageDialog(this.MainPanel, "There is no password for that service. Check your spelling or create a new password.", "Error", 0);
    }

    }//GEN-LAST:event_FindInputActionPerformed

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
    private javax.swing.JPanel AddPanel;
    private javax.swing.JButton Decrypt;
    private javax.swing.JTextField EmailAdd;
    private javax.swing.JButton Encrypt;
    private javax.swing.JTextArea FileText;
    private javax.swing.JScrollPane FileTextPane;
    private javax.swing.JButton FindInput;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JTextField PasswordAdd;
    private javax.swing.JPanel PlainPanel;
    private javax.swing.JPanel PlainShowPanel;
    private javax.swing.JTextField ServiceAdd;
    private javax.swing.JTextField ServiceFind;
    private javax.swing.JTextField UsernameAdd;
    private javax.swing.JButton jButton3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    // End of variables declaration//GEN-END:variables
}
