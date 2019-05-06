package Encryption;

/**
 * @author Sam Haggans
 */

//Import statements
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class EncryptionGUI extends javax.swing.JFrame {
	//Instance Variables used by the program
	private static final long serialVersionUID = 1L;
	private static ArrayList<String> services = new ArrayList<String>();
    private static ArrayList<String> usernames = new ArrayList<String>();
    private static ArrayList<String> emails = new ArrayList<String>();
    private static ArrayList<String> pws = new ArrayList<String>();
    private static final String masterKey = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*\'\"(){}[]:;<>,./?|-=_+`~";
    private static String password;
    private static String openFile;
    private static String encryptionDir;
    
    //Method to encrypt basic text. This creates and returns a random key, and the encrypted contents.
    public static String[] encryptText(String toEncrypt) throws Exception{
        String encrypted = "";
        String key = shuffle(masterKey);
        int countChar = 0;
        String oldKey = key;
        for (int i = 0; i < toEncrypt.length(); i++){
            countChar+=1;
            if (countChar>64){
				key = shift(key);
				countChar = 0;
            }
            encrypted += encryptChar(toEncrypt.substring(i, i+1), key);        
        }
        String[] returns = {encrypted, oldKey};
        return returns;
    }
    
    //Method to encrypt text with a predefined key. This returns only the encrypted data
    public static String encryptTextWithKey(String toEncrypt, String key) throws Exception{
        String encrypted = "";
        int countChar = 0;
        for (int i = 0; i < toEncrypt.length(); i++){
            countChar+=1;
            if (countChar>64){
                key = shift(key);
                countChar = 0;
            }
            encrypted += encryptChar(toEncrypt.substring(i, i+1), key);
        }
        return encrypted;
    }  
    
    //This method encrypts one character from a larger string. It then returns this encrypted character.
    private static String encryptChar(String chara, String key){
        int charIndex = key.indexOf(chara);
        try{
        	return masterKey.substring(charIndex, charIndex+1);}
        catch (Exception err) {
            return chara;//If a character is reached that cannot be encrypted, return it unchanged.
        }
    }
    
    //Decrypt basic text, taking in a String array with the text and key. 
    //Returns decrypted text and original key.
    //The key is decrypted with the password of the program before use.
    public static String[] decryptText(String[] info) throws Exception{
        String decrypted = "";
        String toDecrypt = info[0].replace("\r\n", "\n");
        String key = info[1];
        key = decryptKey(key, password);
        String origKey = key;
        int countChar = 0;
        for (int i = 0; i < toDecrypt.length(); i++){
            countChar+=1;
            if (countChar>64){
                key = shift(key); 
                countChar = 0;
            }
            decrypted += decryptChar(toDecrypt.substring(i, i+1), key);
        }
        String[] returns = {decrypted, origKey};
        return returns;
    }
    
    //Same as the above function, except the key does not need to be decrypted with the password.
    public static String decryptTextWithKey(String toDecrypt, String key) throws Exception{
        String decrypted = "";
        int countChar = 0;
        toDecrypt = toDecrypt.replace("\r\n", "\n");
        for (int i = 0; i < toDecrypt.length(); i++){
            countChar+=1;
            if (countChar>64){
                key = shift(key);
                countChar = 0;
            }
            decrypted += decryptChar(toDecrypt.substring(i, i+1), key);
        }
        return decrypted;
    }
    
    //The same as the above functions, except this function adds the decrypted data to the
    //correct ArrayLists to tell the program what service, emails, passwords, etc. there are.
    //Only used on main.txt (passwords file).
    public static String[] decryptPassText(String[] info) throws Exception{
        String decrypted = "";
        String toDecrypt = info[0].replace("\r\n", "\n");
        String key = decryptKey(info[1],password);
        String origKey = key;
        int countChar = 0;
        for (int i = 0; i < toDecrypt.length(); i++){
            countChar+=1;
            if (countChar>64){
                key = shift(key);
                countChar = 0;
            }
            decrypted += decryptChar(toDecrypt.substring(i, i+1), key);
        }
        ArrayList<String> linesList = new ArrayList<String>();
        String[] lines = decrypted.split("\n",10000);
        for (int i = 0; i < lines.length; i++){
            linesList.add(lines[i]);
        }
        for (int i = 0; i < linesList.size(); i++){
            try {
                if (linesList.get(i).split("~/", 10)[0].length()>1){ //catch any empty lines from showing up as a service
                    services.add(linesList.get(i).split("~/", 10)[0]);
	                usernames.add(linesList.get(i).split("~/", 10)[1]);
	                emails.add(linesList.get(i).split("~/", 10)[2]);
	                pws.add(linesList.get(i).split("~/", 10)[3]);
                }
            }catch (Exception err){
                System.out.println(err);//Catch any broken lines that were read, which would mess up finding the ~/ separator character
            }
        }
        String[] returns = {decrypted, origKey};
        return returns;
    }
    
    //Decrypts one character out of many.
    private static String decryptChar(String chara, String key){
        int charIndex = masterKey.indexOf(chara);
        try{
        	return key.substring(charIndex, charIndex+1);}
        catch (Exception err) {
            return chara;//If a character is reached that cannot be used, just leave it unedited.
        }
    }
    
    //Shuffle a string of text to a random other arrangement.
    private static String shuffle(String text) {
    	String[] characters = new String[text.length()];
        for (int i = 0; i < text.length(); i++) {
        	characters[i] = text.substring(i, i+1);
        }
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = (int)(characters.length * Math.random());
            String temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return makeString(characters);
    }
    
    //A method to shift the key used by the program, based on information from the key and from s.txt.
    private static String shift(String keyStart) throws Exception{
    	keyStart = keyStart.replace(System.lineSeparator(),"");//Remove any line separators from the key.
    	String returnKey = "";
        BufferedReader reader = new BufferedReader(new FileReader(encryptionDir + "/s.txt"));
        ArrayList<String> lines = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            lines.add(line+System.lineSeparator());
            line = reader.readLine();
        }
        reader.close();
        int keyVal = keyStart.indexOf("Q");//Find the index of Q in the starting key.
        String shiftKey = lines.get(keyVal);//Get that line number of that index val from s.txt.
        int shiftVal;
        int newVal;
        //Below line: if the program somehow finds a key with 95 characters, likely a read error because of windows newline chars,
        //just use the first 94 characters.
        while (keyStart.length()>94){keyStart = keyStart.substring(0, keyStart.length()-1);}
        for (int i = 0; i< keyStart.length();i++){
        	//Find the value of the character at the current position in the starting key.
            keyVal = masterKey.indexOf(keyStart.substring(i,i+1));
            //Find the value of the character in the current position in the shifting key from s.txt.
            shiftVal = masterKey.indexOf(shiftKey.substring(i,i+1));
            newVal = keyVal+shiftVal;//Add the two values together.
            if (newVal >93){
                newVal -=94;//If the values were more than 93 (indexerror), subtract 94 from it.
            }
            //Ensures that there are no duplicates in the new key.
            returnKey+=checkDuplicate(masterKey.substring(newVal, newVal+1), returnKey);
        }
        return returnKey;
    }
    
    //Read from a file, returning the contents, key, and password.
    public static String[] readFrom(String fileName) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        ArrayList<String> lines = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            lines.add(line+System.lineSeparator());
            line = reader.readLine();
        }
        String stringResult = makeString(lines);//Will return everything minus the last 2 lines.
        //Returns the result, the key (2nd to last line), and the password (last line).
        String[] returnArray = {stringResult, lines.get(lines.size()-2),lines.get(lines.size()-1)};
        reader.close();
        return returnArray;
    }
    
    //Encrypt a file. Primarily just uses the encryptText method.
    public static void encryptFile(String fileName, String toEncrypt) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        String[] returnVals = encryptText(toEncrypt);
        String keyWrite=encryptKey(returnVals[1],password);//Encrypt the key with the password
        String passWrite=encryptTextWithKey(password,returnVals[1]);//Encrypt the password with the key.
        //With the above two lines, the two values encrypt eachother, meaning that inputting the password
        //can find the key if the password is correct.
        writer.write(returnVals[0] + "\n" + keyWrite+ "\n" + passWrite);
        writer.close();
    }
    
    //Decrypt a file, almost completely using the decryptText method.
    public static String decryptFile(String fileName) throws Exception {
        String[] returnVals = decryptText(readFrom(fileName));
        return returnVals[0];
    }
    
    //Encrypt a key with a password.
    private static String encryptKey(String key, String pass){
        String returnKey = "";
        int selectVal = 0;
        String password = pass;
        while (password.length()<100){
            password+=password;//Make password long enough to encrypt the entire key.
        }
        for (int i = 0; i < key.length(); i++){
            selectVal = masterKey.indexOf(password.substring(i, i+1)) + masterKey.indexOf(key.substring(i, i+1));
            if (selectVal>93){//If the added values of the password and the key are above 93, make it loop around.
                selectVal = selectVal-94;
            }
            returnKey+=masterKey.substring(selectVal, selectVal+1);
        }
        return returnKey;
    }
    
    //Decrypt a key with a password.
    private static String decryptKey(String key, String pass) { 
        String returnKey = "";
        int selectVal = 0;
        String passwordy = pass;
        while (passwordy.length()<100){
            passwordy+=passwordy;
        }
        for (int i =0;i<key.length();i++){
            selectVal = masterKey.indexOf(key.substring(i,i+1))-masterKey.indexOf(passwordy.substring(i,i+1));
            if (selectVal<0){
                selectVal = 94+selectVal;//If the subtracted value is negative, do 94 plus it, looping around backwards.
            }  
            returnKey+=masterKey.substring(selectVal,selectVal+1);
        }
        return returnKey;
    }
    
    //Used to decrypt the password (main.txt) file.
    public static String decryptPassFile(String fileName) throws Exception {
        String[] returnVals = decryptPassText(readFrom(fileName));
        return returnVals[0];
    }
    
    //Make a string from an arraylist (excludes the last 2 lines of the array).
    private static String makeString(ArrayList<String> array){
        String addString = "";
        for (int i = 0; i < array.size()-2;i++){
            addString += array.get(i);
        }
        return addString;
    }
    
    //Make a string from a String array. 
    private static String makeString(String[] array){
        String addString = "";
        for (int i = 0; i < array.length;i++){
            addString += array[i];
        }
        return addString;
    }
    
    //Checks if the inputted password is in fact correct.
    private static boolean checkPassword(String passwordy) throws Exception{
        String[] read = readFrom(encryptionDir+"/main.txt");
        String keyTry = decryptKey(read[1].replace(System.lineSeparator(),""),passwordy);
        String passTry = decryptTextWithKey(read[2].replace(System.lineSeparator(),""),keyTry);
        passTry = passTry.split("\n")[0];
        return (passTry.replace(System.lineSeparator(),"").equals(passwordy));
    }
    
    //Constructor.
    public EncryptionGUI() {
        initComponents();
        
        File dir = new File(System.getProperty("user.home")+"/encryptionGUI");
        File dirDrive = new File(System.getProperty("user.home")+"/Google Drive/encryptionGUI");
        if (!(dir.exists()||dirDrive.exists())) {
        	int driveSelect = JOptionPane.YES_NO_OPTION;
        	int useDrive = JOptionPane.showConfirmDialog(null, "Would you like to use Google Drive as the folder to store encryption data?\nIt will still be secured and encrypted, just in the Google Drive folder\nSelect no if you don't know what this means or don't use Drive.","Use Google Drive",driveSelect);
        	if(useDrive == JOptionPane.YES_OPTION){
        		encryptionDir = System.getProperty("user.home")+"/Google Drive/encryptionGUI";
        		dirDrive.mkdir();
        	}
        	else {
        		encryptionDir = System.getProperty("user.home")+"/encryptionGUI";
        		dir.mkdir();
        	}
        	
        }
        else if (dirDrive.exists()) {
        	encryptionDir = System.getProperty("user.home")+"/Google Drive/encryptionGUI";
        }
        else {
        	encryptionDir = System.getProperty("user.home")+"/encryptionGUI";
        }
        File sFile = new File(encryptionDir+"/s.txt");
        if (!sFile.exists()){
            try {
                generateS();
            }catch (Exception err){System.out.println(err);}
        }
        File passFile = new File(encryptionDir+"/main.txt");
        if (!passFile.exists()){
            try {
                passFile.createNewFile();
                JTextField createPass = new JTextField();
                Object[] description = {"You are running this program for the first time.\nPlease input a password below to use for the program.\nThis will be used for all file decryptions and to enter the program\n","Password:", createPass};
                int passChoose = JOptionPane.showConfirmDialog(null, description, "Set up the program", JOptionPane.OK_CANCEL_OPTION);
                if (passChoose == JOptionPane.YES_OPTION){
                	password = createPass.getText();
                    encryptFile(encryptionDir+"/main.txt","");
                }
                else {
                	System.exit(0);//Exit if "no" or X option is pressed.
                }
            }catch (Exception err){System.out.println(err);}
        
        }
        //All above is trying to determine if the program needs to create the main.txt and s.txt
        //files. If so, they are created, and the program is set up for the first time.
        else {
            try {
            	//Now asking for a password to enter, as the program has already been run before.
                boolean rightPassword = false;
                String passThing = "";
                JPasswordField createPass = new JPasswordField();
                Object[] description = {"Please input password to access program","Password:", createPass};
                int passChoose = JOptionPane.showConfirmDialog(null, description, "Sign in", JOptionPane.OK_CANCEL_OPTION);
                if (passChoose == JOptionPane.YES_OPTION){
                	rightPassword = checkPassword(String.valueOf(createPass.getPassword()));
                    passThing = String.valueOf(createPass.getPassword());
                }
                else {
                	System.exit(0);//Exit if "no" or X option is pressed.
                }
                while (!rightPassword){//While password is incorrect, keep asking.
                    createPass = new JPasswordField();
                    Object[] newDescription = {"Incorrect Password","Password:", createPass};
                    int passChooseNext = JOptionPane.showConfirmDialog(null, newDescription, "Sign in", JOptionPane.OK_CANCEL_OPTION);
                    if (passChooseNext == JOptionPane.YES_OPTION){
                    	rightPassword = checkPassword(String.valueOf(createPass.getPassword()));
                        passThing = String.valueOf(createPass.getPassword());
                    }
                    else {
                    	System.exit(0);//Exit if "no" or X option is pressed.
                    }
                }
                password = passThing;
                PassText.setText(decryptPassFile(encryptionDir+"/main.txt"));
            }
            catch (Exception err){
                System.out.println(err);
            }   
        }
        //Setting some objects in the GUI to be invisible.
        PlainPanel.setVisible(false);
        PlainShowPanel.setVisible(false);
        PassPane.setVisible(false);
    }
    //Generated information by netbeans to create the GUI window.                        
    private void initComponents() {

        MainPanel = new javax.swing.JPanel();
        PlainShowPanel = new javax.swing.JPanel();
        PlainPanel = new javax.swing.JPanel();
        Encrypt = new javax.swing.JButton();
        FileName = new javax.swing.JLabel();
        FileTextPane = new javax.swing.JScrollPane();
        FileText = new javax.swing.JTextArea();
        NoSave = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        FindInput = new javax.swing.JButton();
        AddInput = new javax.swing.JToggleButton();
        SelectFile = new javax.swing.JButton();
        PassPane = new javax.swing.JScrollPane();
        PassText = new javax.swing.JTextArea();
        EditPassword = new javax.swing.JButton();

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

        Encrypt.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        Encrypt.setText("Close and Encrypt");
        Encrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EncryptActionPerformed(evt);
            }
        });

        FileName.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        FileName.setText("File:");

        FileText.setColumns(20);
        FileText.setRows(5);
        FileTextPane.setViewportView(FileText);

        NoSave.setFont(new java.awt.Font("Lucida Grande", 0, 20)); // NOI18N
        NoSave.setText("Close without Saving");
        NoSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PlainPanelLayout = new javax.swing.GroupLayout(PlainPanel);
        PlainPanel.setLayout(PlainPanelLayout);
        PlainPanelLayout.setHorizontalGroup(
            PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlainPanelLayout.createSequentialGroup()
                .addGroup(PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlainPanelLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(FileName))
                    .addGroup(PlainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Encrypt)
                            .addComponent(NoSave))
                        .addGap(26, 26, 26)
                        .addComponent(FileTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PlainPanelLayout.setVerticalGroup(
            PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlainPanelLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(FileName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PlainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlainPanelLayout.createSequentialGroup()
                        .addComponent(Encrypt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(NoSave))
                    .addComponent(FileTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        FindInput.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        FindInput.setText("Find Password");
        FindInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FindInputActionPerformed(evt);
            }
        });

        AddInput.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        AddInput.setText("Add A Password");
        AddInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddInputActionPerformed(evt);
            }
        });

        SelectFile.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(AddInput, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(FindInput, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(SelectFile, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FindInput)
                    .addComponent(AddInput))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SelectFile)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        PassText.setColumns(20);
        PassText.setRows(5);
        PassPane.setViewportView(PassText);

        EditPassword.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        EditPassword.setText("Edit a Password");
        EditPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PlainShowPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EditPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(PlainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PassPane, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EditPassword))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PlainShowPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(PlainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PassPane, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        
    
    //Plaintext file saved.
    private void EncryptActionPerformed(java.awt.event.ActionEvent evt) {                                        
       try {
	       PlainPanel.setVisible(false);
	       PlainShowPanel.setVisible(false);
	       String toWrite = FileText.getText();
	       encryptFile(openFile,toWrite);
       } catch (Exception err){
            System.out.println(err);
       }
    }                                       

    //Add password button.
    private void AddInputActionPerformed(java.awt.event.ActionEvent evt) {                                         
	    try {
	        JTextField serviceEnter = new JTextField();
	        JTextField usernameEnter = new JTextField();
	        JTextField emailEnter = new JTextField();
	        JTextField passwordEnter = new JTextField();
	        Object[] description = {"Input what you wish to add as a password below.\nNote that you MUST put in a value for all four boxes.\nIf you do not have a value for that, just type anything like N/A.\n","Service:", serviceEnter, "Username:", usernameEnter, "Email:", emailEnter, "Password:", passwordEnter};
	        //Creates pop-up window, with the above objects.
	        int addPass = JOptionPane.showConfirmDialog(null, description, "Add a service", JOptionPane.OK_CANCEL_OPTION);
	        if (addPass != JOptionPane.YES_OPTION){
	            return;//Return if yes is not selected.
	        }
	        if (addPass == JOptionPane.YES_OPTION){//If yes selected (redundant check to above).
	            while (serviceEnter.getText().equals("")||usernameEnter.getText().equals("")||emailEnter.getText().equals("")||passwordEnter.getText().equals("")){//If any blank values are added.
	                Object[] newDescription = {"No blank values!\nUse N/A if necessary.","Service:", serviceEnter, "Username:", usernameEnter, "Email:", emailEnter, "Password:", passwordEnter};
	                addPass = JOptionPane.showConfirmDialog(null, newDescription, "Add a service", JOptionPane.OK_CANCEL_OPTION);
	                if (addPass != JOptionPane.YES_OPTION){
	                    return;//Return if yes is not selected.
	                }
	            }
	            if (noCaseContains(services,serviceEnter.getText())==-1){//Ensure that the service does not already exist in the program.
	                PassText.append("\n" + serviceEnter.getText() + "~/" + usernameEnter.getText() + "~/" + emailEnter.getText() + "~/" + passwordEnter.getText());
	                String toWrite = PassText.getText();
	                encryptFile(encryptionDir+"/main.txt", toWrite);
	            }
	            else {
	                JOptionPane.showMessageDialog(MainPanel, "That service already exists! Please use the update password functionality if you wish to change the information.", "Error", 0);
	            }
	            
	        }
	    } catch (Exception err) {
	        System.out.println(err);
	    }
	    
	    try {
	      PassText.setText(decryptPassFile(encryptionDir+"/main.txt"));
	    } catch (Exception err) {System.out.println(err);}
    }   
    
    //Find a password button.
    private void FindInputActionPerformed(java.awt.event.ActionEvent evt) {                                          
        JTextField findService = new JTextField();
        Object[] description = {"Input the name of a service to find","Service:", findService};
        int passChooseNext = JOptionPane.showConfirmDialog(null, description, "Find a Service", JOptionPane.OK_CANCEL_OPTION);
        if (passChooseNext == JOptionPane.YES_OPTION){
            int index = noCaseContains(services, findService.getText());//Get index of the service in the services array.
            try {
              JOptionPane.showMessageDialog(MainPanel, 
                "Username: " + usernames.get(index) + "\nEmail: " + emails.get(index) + "\nPassword: " + pws.get(index), 
                "Information for " + services.get(index), 
                -1);
            } catch (Exception err) {
              JOptionPane.showMessageDialog(MainPanel, "There is no password for that service. Check your spelling or create a new password.", "Error", 0);
            }
        } 
        else {
        	return;//Return if no selected
        }
        
    }
    
    //Select a plaintext file.
    private void SelectFileActionPerformed(java.awt.event.ActionEvent evt) {                                           
        try {
            JFileChooser selectFile = new JFileChooser();
            selectFile.showOpenDialog(MainPanel);//Make file chooser.
            String openFiley = selectFile.getSelectedFile().getAbsolutePath();
            String fileExtension = openFiley.substring(openFiley.indexOf("."),openFiley.length());
            while (!fileExtension.equals(".txt")){//Only get .txt files.
                JOptionPane.showMessageDialog(MainPanel, "You must select a .txt file", "Error", 0);
                selectFile = new JFileChooser();
                selectFile.showOpenDialog(MainPanel);
                openFiley = selectFile.getSelectedFile().getAbsolutePath();
                fileExtension = openFiley.substring(openFiley.indexOf(".")-1,openFiley.length());
            }
            openFile = openFiley;
            PlainPanel.setVisible(true);
            PlainShowPanel.setVisible(true);
            FileName.setText("File: " + openFile);
            try {
                String text = decryptFile(openFiley);//Decrypt the file.
                FileText.setText(text);
            }
            catch (Exception err){
                System.out.println(err);
                return;
            }
        }catch (Exception err){
            System.out.println(err);
        }
    }                                          
    
    //Close a plaintext file without encrypting the contents.
    private void NoSaveActionPerformed(java.awt.event.ActionEvent evt) {                                       
        PlainPanel.setVisible(false);
        PlainShowPanel.setVisible(false);
        FileText.setText("");
    }
    
    //Edit password.
    private void EditPasswordActionPerformed(java.awt.event.ActionEvent evt) {                                             
        JTextField findService = new JTextField();
        Object[] descriptionFirst = {"Input the name of a service to update","Service:", findService};
        int passChooseNext = JOptionPane.showConfirmDialog(null, descriptionFirst, "Update a service", JOptionPane.OK_CANCEL_OPTION);
        if (passChooseNext == JOptionPane.YES_OPTION){
            int index = noCaseContains(services, findService.getText());
            if (index == -1){//If there isn't anything to update.
                JOptionPane.showMessageDialog(MainPanel, "There is no password for that service, so it cannot be updated. Check your spelling or create a new password.", "Error", 0); 
            }
            else{
                try {
                    JTextField usernameEnter = new JTextField();
                    JTextField emailEnter = new JTextField();
                    JTextField passwordEnter = new JTextField();
                    Object[] description = {"Input what you wish to update below.\nNote that you MUST put in a value for all four boxes.\nIf you do not have a value for that, just type anything like N/A.\n","Username:", usernameEnter, "Email:", emailEnter, "Password:", passwordEnter};
                    int addPass = JOptionPane.showConfirmDialog(null, description, "Update a service", JOptionPane.OK_CANCEL_OPTION);
                    if (addPass != JOptionPane.YES_OPTION){
                        return;
                    }
                    if (addPass == JOptionPane.YES_OPTION){
                        while (usernameEnter.getText().equals("")||emailEnter.getText().equals("")||passwordEnter.getText().equals("")){//No blank values allowed.
                            Object[] newDescription = {"No blank values!\nUse N/A if necessary.","Username:", usernameEnter, "Email:", emailEnter, "Password:", passwordEnter};
                            addPass = JOptionPane.showConfirmDialog(null, newDescription, "Add a service", JOptionPane.OK_CANCEL_OPTION);
                            if (addPass != JOptionPane.YES_OPTION){
                                return;
                            }
                        }
                        String toWrite = updatePassword(services.get(index),usernameEnter.getText(),emailEnter.getText(),passwordEnter.getText());
                        PassText.setText(toWrite);
                        encryptFile(encryptionDir+"/main.txt",toWrite);
                        //Write the new password.
                    }
                }
                catch (Exception err) {
                    System.out.println(err);
                }
            }
        }
    } 
    
    //Main function, essentially creates the GUI. Netbeans created this by default.
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
            java.util.logging.Logger.getLogger(EncryptionGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EncryptionGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EncryptionGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EncryptionGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EncryptionGUI().setVisible(true);  
            }
        });
        
    }
    
    //Create the s file when needed.
    public static void generateS() throws IOException {
         for (int i = 0; i < masterKey.length();i++) {
                String sGen =  shuffle(masterKey);//Shuffle the key randomly.
                BufferedWriter writer = new BufferedWriter(new FileWriter(encryptionDir+"/s.txt", true));
                writer.append(sGen);
                if (i != masterKey.length()-1){
                    writer.append(System.lineSeparator());}//Only make newline when not on last line.
                writer.close();
        }
    }
    
    //Check duplicate to make the new keys all only 1 index per char and include all chars.
    public static String checkDuplicate(String object, String inside){
        int indexVal = masterKey.indexOf(object);
        while (inside.contains(object)){
            if (indexVal < 93){indexVal++;}            
            else {indexVal = 0;}
            object = masterKey.substring(indexVal,indexVal+1); 
        }
        return object;
    }
    
    //Check if an ArrayList contains a string, not case sensetive. Return the index.
    private static int noCaseContains(ArrayList<String> array, String value){
        for (int i = 0; i<array.size();i++){
            if (value.toLowerCase().equals(array.get(i).toLowerCase())){
                return i;
            }
        }
        return -1;//Like indexOf, return -1 if not found.
    }
    
    //Method to update the password.
    private String updatePassword(String serviceName, String newUser, String newEmail, String newPass){
        int index = noCaseContains(services,serviceName);
        usernames.set(index,newUser);
        emails.set(index,newEmail);
        pws.set(index,newPass);
        String toWrite = "";
        for (int i = 0; i<services.size();i++){
            toWrite+=System.lineSeparator() + services.get(i) + "~/" + usernames.get(i) + "~/" + emails.get(i) + "~/" + pws.get(i);
        }
        try {
            encryptFile(encryptionDir+"/main.txt", toWrite);
        }
        catch (Exception err){System.out.println(err);}
        return toWrite;
    }
   
    //Below created by Netbeans.
    
    // Variables declaration - do not modify                     
    private javax.swing.JToggleButton AddInput;
    private javax.swing.JButton EditPassword;
    private javax.swing.JButton Encrypt;
    private javax.swing.JLabel FileName;
    private javax.swing.JTextArea FileText;
    private javax.swing.JScrollPane FileTextPane;
    private javax.swing.JButton FindInput;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JButton NoSave;
    private javax.swing.JScrollPane PassPane;
    private javax.swing.JTextArea PassText;
    private javax.swing.JPanel PlainPanel;
    private javax.swing.JPanel PlainShowPanel;
    private javax.swing.JButton SelectFile;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration                   
}
