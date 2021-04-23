import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Formatter;

class Staff
{
   private String name;
   private String phone;
   private boolean dataValid;

   public Staff (String name, String phone)
   {
      dataValid = false;

      if (name.length() == 0)
      {
         JOptionPane.showMessageDialog(null, "Name cannot be blank.",
              "Name Error:", JOptionPane.ERROR_MESSAGE);

         //throw new Exception ("Invalid Name");
      }
      else if (phone.length() == 0)
      {
         JOptionPane.showMessageDialog(null, "Phone cannot be blank.",
              "Phone Error:", JOptionPane.ERROR_MESSAGE);
      }
      else
      {
         // OK, no errors, all is OK.
         dataValid  = true;
         this.name  = name;
         this.phone = phone;
      }
   }

   public String getName ()
   {
      return name;
   }

   public String getPhone ()
   {
      return phone;
   }

   public boolean isDataValid ()
   {
      return dataValid;
   }
}


public class StaffPhoneGUI extends JFrame
{
   public static final String DATA_FILE_NAME = "staffphone.txt";

   JTextArea textArea      = new JTextArea ();
   JButton   displayButton = new JButton ("Display");
   JButton   clearButton   = new JButton ("Clear");
   JButton   saveButton    = new JButton ("Save");
   JButton   addButton     = new JButton ("Add");
   JButton   editButton    = new JButton ("Edit");

   private ArrayList<Staff> staffArrayList = new ArrayList<Staff> ();

   public StaffPhoneGUI ()
   {
      setTitle ("Staff Phone App v0.03");

      JPanel flowPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));

      add (textArea,      BorderLayout.CENTER);


      flowPanel.add (displayButton);
      flowPanel.add (addButton);
      flowPanel.add (editButton);
      flowPanel.add (clearButton);
      flowPanel.add (saveButton);

      add (flowPanel, BorderLayout.SOUTH);

      displayButton.addActionListener (event -> readFile (DATA_FILE_NAME));
      clearButton.addActionListener   (event -> clearData ());
      saveButton.addActionListener    (event -> writeFile (DATA_FILE_NAME));
      addButton.addActionListener     (event -> addData ());
      editButton.addActionListener    (event -> editData ());

      readFile (DATA_FILE_NAME);

      setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);

      addWindowListener (new WindowAdapter ()
      {
         public void windowClosing (WindowEvent e)
         {
            exitApplication ();
         }
      });

   }

   private void addTextData ()
   {
      textArea.setText ("Hello World" + "\n\n" +
         "The 'Display' button is working !");
   }

   private void addData ()
   {
      String name  = JOptionPane.showInputDialog ("Enter name:");
      String phone = JOptionPane.showInputDialog ("Enter phone:");

      Staff newStaff = new Staff (name, phone);

      if (newStaff.isDataValid () == true)
      {
         staffArrayList.add (newStaff);

         System.out.println ("New staff member added, now: " +
                         staffArrayList.size() + " records.");

         refreshTextArea ();
      }
   }

   private void editData ()
   {
      /*
        Prompt user for a name
        Search out ArrayList for that name
        if Match found
           - prompt for new name
           - prompt for phone number
           - create new Staff object
           - Overwrite the old staff member in the ArrayList
        if Match not found - display an error.
      */
      String searchName  = JOptionPane.showInputDialog ("Enter name to edit:");

      boolean matchFound = false;
      int     location   = -1;

      for (int k = 0; k < staffArrayList.size(); k++)
      {
         if (searchName.compareTo (staffArrayList.get (k).getName ()) == 0)
         {
            matchFound = true;
            location   = k;
         }
      }

      if (matchFound == false)
      {
         JOptionPane.showMessageDialog(null,
              "Error: staff '" + searchName + "' could not be found.",
              "Phone Error:", JOptionPane.ERROR_MESSAGE);
      }
      else
      {
         // A match was found - the staff member IS in the ArrayList
         String name  = JOptionPane.showInputDialog ("Enter new name:");
         String phone = JOptionPane.showInputDialog ("Enter new phone:");

         Staff newStaff = new Staff (name, phone);

         if (newStaff.isDataValid () == true)
         {
            staffArrayList.set (location, newStaff);

            System.out.println ("Staff member updated !");

            refreshTextArea ();
         }
      }
   }

   private void readFile (String fileName)
   {
      String fileDataStr = "";

      staffArrayList.clear ();

      try
      {
         FileReader fileReader = new FileReader (fileName);
         Scanner inFile = new Scanner (fileReader);

         while (inFile.hasNext() == true)
         {
            String[] parts = inFile.nextLine().split (", ");
            if (parts.length == 2)
            {
               Staff newStaff = new Staff (parts [0], parts [1]);

               staffArrayList.add (newStaff);

            }
            else
            {
               throw new IOException ();
            }
         }

         inFile.close();
         fileReader.close();

         refreshTextArea ();

         System.out.println (staffArrayList.size() + " records read.");

      }
      catch (IOException err)
      {
         textArea.setText ("Error: file could not be read.");
         //System.exit(-1); // Error.
      }
   }


   private void refreshTextArea ()
   {
      textArea.setText ("");
      for (int k = 0; k < staffArrayList.size(); k++)
      {
         textArea.append (staffArrayList.get (k).getName ()
            + "\t" + staffArrayList.get (k).getPhone ()
            + "\n");
      }

      textArea.append ("\n\n" + staffArrayList.size() + " records read." + "\n");
   }


   private void writeFile (String fileName)
   {
      try
      {
         Formatter outFile = new Formatter (fileName);

         for (int k = 0; k < staffArrayList.size(); k++)
         {
            outFile.format ("%s, %s\n", staffArrayList.get(k).getName(),
                            staffArrayList.get(k).getPhone());
         }

         outFile.close();

         System.out.println (staffArrayList.size() + " records written.");
      }
      catch (IOException err)
      {
         textArea.setText ("Error: file could not be written.");
         //System.exit(-1); // Error.
      }
   }

   private void clearData ()
   {
      staffArrayList.clear ();
      textArea.setText ("");
   }

   private void exitApplication ()
   {
      writeFile (DATA_FILE_NAME);

      System.exit (0); // All is OK
   }

   public static void main (String[] args)
   {
      StaffPhoneGUI app = new StaffPhoneGUI ();
      app.setVisible (true);
      app.setSize (500, 310);
      app.setLocation (200, 200);
      //app.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
   }
} // public class StaffPhoneGUI
