/*Sources:
Characters to Ascii/Characters from strings:
http://www.roseindia.net/java/java-conversion/CharToASCIIi.shtml
List of Ascii:
http://en.wikipedia.org/wiki/ASCII
Convert Integers/Strings to binary:
http://www.roseindia.net/java/beginners/DataConversion.shtml
Forced garbage collecting:
http://www.artima.com/forums/flat.jsp?forum=1&thread=96010
Executing code from other languages:
http://en.wikipedia.org/wiki/Java_Native_Interface
Converting an integer into a char:
http://www.dreamincode.net/forums/topic/89717-converting-integer-into-ascii-character/
Converting a char into a string
http://www.roseindia.net/java/java-conversion/CharToString.shtml
Converting a String to a byte:
http://www.javadb.com/convert-string-to-byte
http://download.oracle.com/javase/1.5.0/docs/api/java/lang/String.html#String(byte[])
Reading a file into Bytes:
http://www.exampledepot.com/egs/java.io/File2ByteArray.html
http://www.java-examples.com/read-file-byte-array-using-fileinputstream
Switch Statements:
http://download.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
JFrames:
http://www.ibiblio.org/java/course/week8/47.html
http://download.oracle.com/javase/tutorial/uiswing/components/frame.html
JPanels
http://download.oracle.com/javase/tutorial/uiswing/components/panel.html
http://www.java2s.com/Tutorial/Java/0240__Swing/UsingJPanelasacontainer.htm
File Browser:
http://download.oracle.com/javase/tutorial/uiswing/components/filechooser.html
RC4 Encryption:
http://en.wikipedia.org/wiki/Rc4
Printing in Binary:
http://mindprod.com/jgloss/binary.html
Hex to byte:
http://www.experts-exchange.com/Programming/Programming_Languages/Java/Q_21062554.html
Menus:
    JSpinners:
    http://download.oracle.com/javase/tutorial/uiswing/components/spinner.html
    JPasswordField:
    http://java.comsci.us/examples/swing/JPasswordField.html
    Layouts:
    http://download.oracle.com/javase/tutorial/uiswing/layout/flow.html
    JFormattedTextFields:
    http://download.oracle.com/javase/1.4.2/docs/api/javax/swing/JFormattedTextField.html
    http://download.oracle.com/javase/tutorial/uiswing/components/spinner.html
    TextArea:
    http://download.oracle.com/javase/1.4.2/docs/api/java/awt/TextArea.html
    Checkbox:
    http://download.oracle.com/javase/1.4.2/docs/api/java/awt/Checkbox.html
    Listeners
    http://www.javaprogrammingforums.com/java-code-snippets-tutorials/278-how-add-actionlistener-jbutton-java-swing.html
    http://www.java2s.com/Code/JavaAPI/javax.swing/JCheckBoxaddChangeListenerChangeListenerlis.htm
    Setting Size of JFrame:
    http://download.oracle.com/javase/tutorial/uiswing/layout/using.html
    JLabels: 
    http://www.artima.com/forums/flat.jsp?forum=1&thread=129397
*/
import java.util.Scanner;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
public class Encrypt 
{
    InputStream read;
    Scanner scan;
    static Encrypt runner;
    public static void main(String[] args)
    {  
        runner = new Encrypt();
        runner.Menu();
    }

    public void Menu() 
    {
        JFileChooser fileChooser = new JFileChooser();        
        JButton XORButton = new JButton("Encrypt");
        XORButton.setPreferredSize(new Dimension(81,25));
        String[] encryptionTypes = {"XOR", "RC4"};
        SpinnerListModel typesList = new SpinnerListModel(encryptionTypes);
        final JSpinner types = new JSpinner(typesList);
        String[] formatTypes = {"String","Hex"};
        SpinnerListModel formatTypesList = new SpinnerListModel(formatTypes);
        final JSpinner format = new JSpinner(formatTypesList);       
        final JTextField textField = new JTextField(10);        
        String[] choices = {"TextField","File"};
        SpinnerListModel textChoicesList = new SpinnerListModel(choices);
        SpinnerListModel passwordChoicesList = new SpinnerListModel(choices);
        final JSpinner textChoices = new JSpinner(textChoicesList);
        final JSpinner passwordChoices = new JSpinner(passwordChoicesList);
        final JPasswordField passwordField = new JPasswordField(10); 
        passwordField.setEchoChar('*');
        final Checkbox hidePassword = new Checkbox("Show Password?",false);       
        final Checkbox writeFile = new Checkbox("Write to a file?",false);
        final JTextField outputFileName = new JTextField("Output File Location", 18);
        outputFileName.setEditable(false);
        final TextArea outputArea = new TextArea("",8,26,1);
        outputArea.setEditable(false);
        XORButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                byte[] plainText;
                if(textChoices.getValue().equals("File"))
                {
                   plainText = runner.readByte(textField.getText());
                }
                else
                {
                    if(format.getValue().equals("Hex"))
                    {
                        plainText = runner.hexToByte(textField.getText());
                    }
                    else
                    {
                        plainText = textField.getText().getBytes();
                    }
                }
                byte[] key;
                if(passwordChoices.getValue().equals("File"))
                {
                  key = runner.readByte(new String(passwordField.getPassword()));
                }
                else
                {
                 key = (new String(passwordField.getPassword())).getBytes();
                }
                byte[] cipherText;
                System.out.println(textField.getText());
                System.out.println(new String(passwordField.getPassword()));
                if(types.getValue().equals("XOR"))
                {
                  cipherText = runner.XOR(plainText, key);
                }
                else
                {
                  cipherText = runner.RC4(plainText,key);
                }
                if(writeFile.getState())//if its clicked in
                {
                   runner.writeByte(outputFileName.getText(),cipherText);
                }
                else
                {
                    if(textChoices.getValue().equals("File") || format.getValue().equals("Hex"))
                    {
                        outputArea.setText(runner.byteToString(cipherText));
                    }
                    else
                    {
                        outputArea.setText(runner.bytesToHex(cipherText));
                    }
                }
            }
        });
        ItemListener hidePasswordPressed = new ItemListener() 
        {
            public void itemStateChanged(ItemEvent e) 
            {
                if(hidePassword.getState())
                {
                    passwordField.setEchoChar((char) 0);
                }
                else
                {
                    passwordField.setEchoChar('*');
                }
                System.out.println("checkbox pressed");
            }
        };
        
        hidePassword.addItemListener(hidePasswordPressed);
        ItemListener writeFilePressed = new ItemListener() 
        {
            public void itemStateChanged(ItemEvent e) 
            {
                if(e.getStateChange() == 2)
                {
                    outputFileName.setEditable(false);
                    outputFileName.setText("Output File URL");
                }
                else
                {
                    outputFileName.setText("");
                    outputFileName.setEditable(true);
                }
            }
        };
        writeFile.addItemListener(writeFilePressed);
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(XORButton); 
        panel.add(types);
        panel.add(format);
        panel.add(textField);
        panel.add(textChoices);
        panel.add(passwordField);
        panel.add(passwordChoices);
        panel.add(hidePassword);
        panel.add(writeFile);
        panel.add(outputFileName);
        panel.add(outputArea);
        panel.setBackground(Color.cyan);
        JFrame frame = new JFrame("Encrypt");
        frame.setSize(250,325);
        frame.setResizable(false);
        frame.add(panel);
        frame.setIconImage(new ImageIcon("C:\\Users\\BarryLong\\Desktop\\Encrypt\\Meter and shunt.jpg").getImage());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
    }
    
    public byte[] RC4(String unlocked, String tempKey)
    {
       return this.RC4(unlocked.getBytes(), tempKey.getBytes());
    }
    
    public byte[] RC4(byte[] unlocked, byte[] key)
    {
        byte[] S = new byte[256]; 
        for(int temp = 0; temp < 256; temp++)
        {
            S[temp] = (byte)temp;
        }
        int j = 0;
        byte swap;
        int i;
        for(i = 0; i < 256; i++)//KSA
        {
           j = (j + key[i % key.length] + S[i]) & 255;
           swap = S[i];
           S[i] = S[j];
           S[j] = swap;                
        }
        i = j = 0;
        byte[] keystream = new byte[unlocked.length];
        for(int place = 0; place < unlocked.length;place++)//PRGA //Middle argument is how much stream to gen
        {
            i = (i + 1) & 255;
            j = (j + S[i]) & 255;
            swap = S[i];
            S[i] = S[j];
            S[j] = swap;
            keystream[place] = S[(S[i] + S[j]) & 255];
        }
        byte[] ciphertext = new byte[keystream.length];
        for(int place = 0; place < ciphertext.length; place++)
        {
            ciphertext[place] = (byte)(keystream[place] ^ unlocked[place]);
        }
        return ciphertext;
    }

    public void writeByte(String fileName, byte[] bytes)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(bytes);
            fos.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public byte[] readByte(String fileName)
    {
        try
        {
            FileInputStream fis = new FileInputStream(fileName);    
            byte[] output = new byte[fis.available()];
            System.out.println(output.length);
            int amountRead = fis.read(output);//read returns an int for the number of bytes transfered into the buffer
            if (amountRead != output.length)
            throw new IOException("Didn't get all the data");
            fis.close();
            return output;

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public byte[] XOR(File input, String keyString)
    {
        byte[] key = keyString.getBytes(); 
        return XOR(input, key);
    }
    
    public byte[] XOR(File input, byte[] key)
    {
        try
        {               
            read = new FileInputStream(input);
            byte[] unlocked = new byte[(int)input.length()]; 
            read.read(unlocked);
            byte[] locked = new byte[unlocked.length];
            for(int place = 0; place < unlocked.length;place++)
            {
                locked[place] = (byte)(unlocked[place] ^ key[place % key.length]);//looping rather than multiplying out
            }       
            return locked;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }   
        return null; 
    }   
        
    public void XORStringRunner() 
    {
        System.out.println("Line to be encrypted by XOR?");
        String input = scan.nextLine();
        System.out.println("Key?");
        String key = scan.nextLine();
        System.out.println("Write to file or output byte[]?");
        System.out.println("1:Write to file\n2:Output byte[]");
        int choice = 0;
        try
        {
            choice = Integer.parseInt(scan.nextLine());
            if(choice > 2 || choice < 1)
            {
                System.out.println("Your number is in the wrong range");
            }
        }
        catch(Exception e)
        {
            System.out.println("Please use a real number");
        }  
        switch(choice)
        {
            case 1:   
                    System.out.println("File location and name?");
                    this.writeByte(scan.nextLine(),this.XOR(input,key));
                break;
            case 2: System.out.println(this.XOR(input,key).toString());
                break;
            default :break;//this happens if the case doesnt exist
        }
    }
    
    public int charToInt(char chr)//redundant can be removed later
    {
        return (int)chr;
    }
    
    public byte[] XOR(String unlocked, String key)//unlocked = the thing to be encrypted, key = password
    {
        byte[] unlockedBytes = unlocked.getBytes();
        byte[] tempBytes = key.getBytes();
        int temp = key.getBytes().length;
        byte[] keyBytes = new byte[temp];
        for(int a = 0; a < keyBytes.length; a++)
        {
            keyBytes[a] = tempBytes[a % tempBytes.length]; 
        }

        byte[] lockedBytes = new byte[unlockedBytes.length];
        for(int place = 0; place < unlockedBytes.length; place++)
        {
            lockedBytes[place] = (byte)(unlockedBytes[place] ^ keyBytes[place]);
        }
        return lockedBytes;
    }
    
    public byte[] XOR(byte[] unlockedBytes, byte[] tempBytes)//unlocked = the thing to be encrypted, key = password
    {
        byte[] keyBytes = new byte[unlockedBytes.length];
        for(int a = 0; a < keyBytes.length; a++)
        {
            keyBytes[a] = tempBytes[a % tempBytes.length]; 
        }
        byte[] lockedBytes = new byte[unlockedBytes.length];
        for(int place = 0; place < unlockedBytes.length; place++)
        {
            lockedBytes[place] = (byte)(unlockedBytes[place] ^ keyBytes[place]);
        }
        return lockedBytes;
    }
    
    public void printBytes(byte[] input)
    {
        String temp = "";
        for(int place = 0; place < input.length;place++)
        {
            System.out.printf(" %d", 0xFF & input[place]);//THANKS TO BILL SOLEY
        }                 
    }
    
    public void printHex(byte[] data)
    {
        for (int i = 0; i < data.length; ++i) 
        {
                if (i % 16 == 0)
                System.out.printf("\n   ");
        }
        System.out.printf("\n");
    }
    
    public void printBinary(byte[] input)
    {
        String output = "";
        String temp;
        String zero = "00000000";
        int b;
        for(int a = 0; a < input.length;a++)
        {
            b = 0;
            temp = Integer.toBinaryString(input[a] & 0xFF) + " ";
            System.out.printf(" %d", 0xFF & input[a]);
            while(temp.substring(b,b+1).equals("0") && b < 7)
            {
                b++;
            }
            
            output += zero.substring(0,b) + temp;
        }
        System.out.println(output);
    }
    
    public int[] stringToInt(String str)
    {
        int[] output = new int[str.length()];
        int temp;
        for(int a = 0; a < str.length(); a++)
        {
            output[a] = str.charAt(a);
        }
        return output;
    }
               
    public byte[] hexToByte(String in)
    {
        in = in.replaceAll("\\s", "");
        if (in.length() % 2 != 0)
        throw new NumberFormatException("even number of digits required");
        byte[] output = new byte[in.length() / 2];
        for(int a = 0; a < in.length(); a += 2) {
            output[a/2] = (byte) Integer.parseInt(in.substring(a,a+2), 16);
        }
        return output;
    }
    
    public String bytesToHex(byte[] data)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; ++i) 
        {
            if (i > 0)
            buf.append(i % 16 == 0 ? "\n" : " ");
            buf.append(String.format("%02x", 0xFF & data[i]));
        }
        return buf.toString();
    }
    
    public String byteToString(byte[] in)
    {
        String output = "";
        for(int a = 0;a < in.length;a++)
        {
            output += (char)in[a];
        }
        return output;
    }


}
    
        
