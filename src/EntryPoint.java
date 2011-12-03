




import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import java.io.FileOutputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
//import java.io.PrintWriter;



public class EntryPoint extends JPanel implements ActionListener {
       private static final long serialVersionUID = 1L;

       private JLabel currentTimeLabel;
       private JLabel remainedTimeLabel;
       private JLabel currentLabel;
       private JLabel timerLabel;
       private JLabel minLabel;
       private JLabel todoLabel;
       private JTextField timerField;
       private JTextField todoField;
       private JTextArea todoArea;
       private final static String newline = "\n";
       int minutes;
       String min_s;
       long millis, finish_millis;
       
       
       public static void main(String[] args) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                   createAndShowGUI();
               }
           });
       }
      
       private static void createAndShowGUI() {
           JFrame frame = new JFrame("Ç‹Ç¢ÇΩÇ¢Ç‹Å[");
           frame.setBounds(100, 100, 300, 300);
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.add(new EntryPoint());
           frame.pack();
           frame.setVisible(true);
       }
 
       public EntryPoint() {
            super(new GridBagLayout());
              currentTimeLabel = new JLabel();
              currentTimeLabel.setFont(new Font("ÉqÉâÉMÉmäpÉS", Font.BOLD, 22));
              remainedTimeLabel = new JLabel();
              remainedTimeLabel.setFont(new Font("ÉqÉâÉMÉmäpÉS", Font.BOLD, 22));
              currentLabel = new JLabel("åªç›ÇÃéûçè : ");
              timerLabel = new JLabel("ê›íËéûä‘ : ");
              minLabel = new JLabel("ï™");
              todoLabel = new JLabel("Ç‚ÇÈÇ±Ç∆ : ");
              timerField = new JTextField(2);
              todoField = new JTextField(10);
              todoField.addActionListener(this);
              todoArea = new JTextArea(5,15);
              todoArea.setEditable(false);
              JScrollPane scrollPane = new JScrollPane(todoArea);
              scrollPane.setPreferredSize(new Dimension(100,100));
              GridBagConstraints c = new GridBagConstraints();
              c.gridwidth = 1;
              c.gridheight = 1;
              c.insets = new Insets(10, 10, 10, 10);
              c.fill = GridBagConstraints.BOTH;
              c.weightx = 1.0;
              c.weighty = 1.0;
              c.gridx = 0;
              add(currentLabel, c);
              c.gridx = GridBagConstraints.RELATIVE;
              add(currentTimeLabel, c);
              c.gridx = 0;
              c.gridy = 1;
              add(timerLabel, c);
              c.gridx = GridBagConstraints.RELATIVE;
              add(timerField, c);
              add(minLabel,c);
              c.gridx = 0;
              c.gridy = 2;
              add(todoLabel, c);
              c.gridx = 1;
              add(todoField, c);
              c.gridwidth = GridBagConstraints.REMAINDER;
              c.gridy = 3;
              add(remainedTimeLabel, c);
              c.gridwidth = GridBagConstraints.REMAINDER;
              c.gridx = 0;
              c.gridy = 4;
              add(scrollPane, c);
              Timer timer = new Timer("Time Alert");
              timer.schedule(new CurrentTime(), 0, 500);
              this.setVisible(true);
       }
      
       public void actionPerformed(ActionEvent evt){
    	   String todoText = todoField.getText();
    	   String minText = timerField.getText();
    	   String viewText = minText + "ï™ : " + todoText;
    	   SaveFile saveFile = new SaveFile(viewText);
    	   //saveFile.setSaveText(viewText);
    	   try {
    		   //PrintWriter outFile = new PrintWriter(new OutputStreamWriter(new FileOutputStream("test2.txt"), "Shift_JIS"));
    		   FileOutputStream outFile = new FileOutputStream("test3.txt");
    		   //OutputStreamWriter newFile = new OutputStreamWriter(outFile, "Shift_JIS");
    		   ObjectOutputStream out = new ObjectOutputStream(outFile);
    		   out.writeObject(saveFile);
    		   System.out.println(saveFile);
    		   out.close();
    		   outFile.close();
    	   } catch(Exception ex) {
    		   ex.printStackTrace();
    	   }
    	   SaveFile readedData = null;
    	   try{
    		   FileInputStream inFile = new FileInputStream("test3.txt");
    		   ObjectInputStream in = new ObjectInputStream(inFile);
    		   readedData = (SaveFile)in.readObject();
    		   in.close();
    		   inFile.close();
    	   } catch(Exception ex) {
    		   ex.printStackTrace();
    	   }
    	   System.out.println(readedData.getSaveText());
    	   todoArea.append(viewText + newline);
    	   todoField.selectAll();
           min_s  = timerField.getText();
           minutes = Integer.parseInt(min_s);
           finish_millis = System.currentTimeMillis() + (minutes * 60000);
           Timer t = new Timer();
           t.schedule(new RemainedTime(), 0, 500);
           Growl growl = new Growl("Growl Demo",
                   new String[]{"system", "boss", "spam"},
                   new String[]{"system", "boss"});
           growl.init();
           growl.registerApplication();
           growl.notify("system", "äJénÅIÅIÅI", todoText);
       }
       
       class RemainedTime extends TimerTask{
    	   public void run(){
    		     long current_millis = System.currentTimeMillis();
                 long remain_millis = finish_millis - current_millis;
                 long min = TimeUnit.MILLISECONDS.toMinutes(remain_millis);
                 long sec = TimeUnit.MILLISECONDS.toSeconds(remain_millis) -
                           TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remain_millis));
                 String min_str = String.valueOf(min);
                 String sec_str = String.valueOf(sec);
                 remainedTimeLabel.setText("écÇË" + min_str + "ï™" + sec_str + "ïb");
                 if (min <= 0 && sec <= 0){
                	remainedTimeLabel.setText("Finished");
                	Growl growl = new Growl("Growl Demo",
                			new String[]{"system", "boss", "spam"},
                			new String[]{"system", "boss"});
                	growl.init();
                	growl.registerApplication();
                	growl.notify("system", "èIÇÌÇËÇæÇÊÅ[Å[Å[ÅIÅIÅI", "            Finished");
                	cancel();
                }
    	   }
       }

       class CurrentTime extends TimerTask {                    
           public void run(){   
                  String str = "kk" + "éû" + "mm" + "ï™" + "ss" + "ïb";
                  SimpleDateFormat sf = new SimpleDateFormat(str);
                  GregorianCalendar cal = new GregorianCalendar();
                  currentTimeLabel.setText(sf.format(cal.getTime()));
           }
    }
}







