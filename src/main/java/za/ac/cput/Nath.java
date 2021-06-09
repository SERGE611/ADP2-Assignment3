
package za.ac.cput;
/*
 * 
 * 
 * 
 */
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Nath 
{
    private final String Out = "stakeholder.ser";

    FileWriter fileWriter;
    PrintWriter printWriter;
    FileInputStream input;
    ObjectInputStream ob;
    
    public void openFile(String filename)
    {
        try
        {
            fileWriter = new FileWriter(new File(filename));
            printWriter = new PrintWriter(fileWriter);
            System.out.println(filename);
            
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
            System.exit(1);
        }
    }
    private ArrayList<Customer> customersList()
    {
        ArrayList<Customer> customers = new ArrayList<>();
        
        try
        {
            input = new FileInputStream(new File(Out));
            ob= new ObjectInputStream(input);
            while (true)
            {
                Object obj = ob.readObject();
                
                if (obj instanceof Customer)
                {
                    customers.add((Customer) obj);
                }
            }
            
        } catch (EOFException eofe)
        {
            
        } catch (IOException | ClassNotFoundException e)
        {
           e.printStackTrace();
           System.exit(1);
            
        } finally
        {
            try
            {
                input.close();
                ob.close();
                
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (!customers.isEmpty())
        {
            Collections.sort(customers,
                    (Customer c1, Customer c2) -> 
                            c1.getStHolderId().compareTo(c2.getStHolderId())
            );
        }
        
        return customers;
    }
    
    private void writeCustomerOutFile()
    {
        String header = "======================= CUSTOMERS =========================\n";
        String placeholder = "%s\t%-12s\t%-12s\t%-12s\t%-12s\n";
        String separator = "===========================================================\n";
        
        try
        {   
            printWriter.print(header);
            printWriter.printf(placeholder, "ID", "Name", "Surname", "Date Of Birth", "Age");
            printWriter.print(separator);
            
            for (int i = 0; i < customersList().size(); i++)
            {   
                printWriter.printf(placeholder,customersList().get(i).getStHolderId(),customersList().get(i).getFirstName(),
                customersList().get(i).getSurName(),formatDate(customersList().get(i).getDateOfBirth()),calculateAge(customersList().get(i).getDateOfBirth())
                );
            }
            printWriter.printf( "\nNumber of customers who can rent: %d",canRent());
            printWriter.printf("\nNumber of customers who cannot rent: %d",canNotRent());
            
        } catch (Exception e)
        {
          System.out.println(e);
        }
    }
    private String formatDate(String dob)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        LocalDate localDate = LocalDate.parse(dob); 
        return localDate.format(dateTimeFormatter);
    }
    
    private int calculateAge(String dob)
    {
        LocalDate localDate = LocalDate.parse(dob); 
        int Year  = localDate.getYear();
        ZonedDateTime Date = ZonedDateTime.now(); 
        int currentYear = Date.getYear();
        return currentYear - Year ;
    }
    
    private int canRent()
    {
        int canRent = 0;
        
        for (int i = 0; i < customersList().size(); i++)
        {
            if (customersList().get(i).getCanRent())
            {
                canRent += 1;
            }
        }
        return canRent;
    }
    
    private int canNotRent()
    {
        int canNotRent = 0;       
        for (int i = 0; i < customersList().size(); i++)
        {
            if (!customersList().get(i).getCanRent())
            {
                canNotRent += 1;
            }
        }
        return canNotRent;
    }
    private ArrayList<Supplier> suppliersList()
    {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        
        try
        {
            input = new FileInputStream(new File(Out));
            ob = new ObjectInputStream(input);
            
            // throws an EOFException
            while (true)
            {
                Object obj = ob.readObject();
                if (obj instanceof Supplier)
                {
                    suppliers.add((Supplier) obj);
                }
            }
        } 
        catch (EOFException eofe)
        {
            
        } 
        catch (IOException | ClassNotFoundException e)
        {
           System.out.println(e);
        } finally
        {
            try
            {
                input.close();
                ob.close();
                
            } catch (IOException e)
            {
                System.out.println(e);
            }
        }
        if (!suppliers.isEmpty())
        {
            Collections.sort(suppliers, (Supplier supp, Supplier suppli) -> supp.getName().compareTo(suppli.getName())
            );
        }
        
        return suppliers;
    }
    private void writeSupplierOutFile()
    {
        String S = "======================= SUPPLIERS =========================\n";
        String M = "%s\t%-20s\t%-10s\t%-10s\n";
        String N = "===========================================================\n";
        
        try
        {
            printWriter.print(S);
            printWriter.printf(M, "ID", "Name", "Prod Type","Description");
            printWriter.print(N);
            for (int i = 0; i < suppliersList().size(); i++)
            {
                printWriter.printf(M,suppliersList().get(i).getStHolderId(), suppliersList().get(i).getName(),
                suppliersList().get(i).getProductType(),suppliersList().get(i).getProductDescription()
                );
            }
            
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }
    
    public void closeFile(String filename)
    {
        try
        {
            fileWriter.close();
            printWriter.close();
            System.out.println(filename + " has been closed");

        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
    public static void main(String[] args)
    {
        Nath Serge = new Nath();
        
        Serge.openFile("customerOutFile.txt");
        Serge.writeCustomerOutFile();
        Serge.closeFile("customerOutFile.txt");
        
        System.out.println(""); 
        
        Serge.openFile("supplierOutFile.txt");
        Serge.writeSupplierOutFile();
        Serge.closeFile("supplierOutFile.txt");
    }
}

