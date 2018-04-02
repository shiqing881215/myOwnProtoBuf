package org.shiqing.tutorial;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.shiqing.tutorial.AddressBookProtos.AddressBook;
import org.shiqing.tutorial.AddressBookProtos.Person;
import org.shiqing.tutorial.AddressBookProtos.Person.PhoneNumber;
import org.shiqing.tutorial.AddressBookProtos.Person.PhoneType;

/**
 * Use the generated class to write things.
 * This is used to test serialize
 */
public class Writer {
    
    /**
     * A method pop up for user to input and return a person object
     * @param stdin
     * @param stdout
     * @return
     * @throws IOException
     */
    private Person addPerson(BufferedReader stdin, PrintStream stdout) throws IOException {
        // Instantiate the person builder
        Person.Builder builder = Person.newBuilder();
        
        // Id is required
        // We don't fail here specifically if id is empty, we let Protobuf handle that
        stdout.print("Enter id");
        builder.setId(Integer.valueOf(stdin.readLine()));
        
        // Name is required 
        stdout.print("Enter name");
        builder.setName(stdin.readLine());
        
        // Email is optional
        stdout.print("Enter email (blank for none)");
        String email = stdin.readLine();
        if (!email.isEmpty()) {
            builder.setEmail(email);
        }
        
        // A list of phone numbers
        while (true) {
            stdout.print("Enter phone number (or leave it blank when finish)");
            String number = stdin.readLine();
            // Finish
            if (number.isEmpty()) {
                break;
            }
            
            // Always use builder to create the object
            PhoneNumber.Builder phoneNumberBuilder = PhoneNumber.newBuilder();
            phoneNumberBuilder.setNumber(number);
            
            // Get phone type
            stdout.print("Is this a mobile or home or work phone number ?");
            String phoneType = stdin.readLine();
            if (phoneType.equalsIgnoreCase("mobile")) {
                phoneNumberBuilder.setType(PhoneType.MOBILE);
            } else if (phoneType.equalsIgnoreCase("home")) {
                phoneNumberBuilder.setType(PhoneType.HOME);
            } else if (phoneType.equalsIgnoreCase("work")) {
                phoneNumberBuilder.setType(PhoneType.WORK);
            } else {
                stdout.print("Use default phone type : HOME");
            }
            
            builder.addPhones(phoneNumberBuilder.build());
        }
        
        // Finally build person object
        return builder.build();
    }
    
    /**
     * Read a new person and write to an existing file
     * @param fileName
     * @throws IOException 
     */
    private void write(String fileName) throws IOException {
        AddressBook.Builder addressBookBuilder = AddressBook.newBuilder();
        
        // First read the existing address book
        try {
            addressBookBuilder.mergeFrom(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            System.out.println(fileName + ": File not found.  Creating a new file.");
        }
        
        addressBookBuilder.addPeople(addPerson(new BufferedReader(new InputStreamReader(System.in)),
                System.out));
        
        // Write the new address book back to disk.
        FileOutputStream output = new FileOutputStream(fileName);
        addressBookBuilder.build().writeTo(output);
        output.close();
    }
    
    // Test it out
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage:  AddPerson ADDRESS_BOOK_FILE");
            System.exit(-1);
        }
        
        System.out.println("Start writing...");
        Writer writer = new Writer();
        writer.write(args[0]);
        System.out.println("End writing...");
    }
}
