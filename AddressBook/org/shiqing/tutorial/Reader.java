package org.shiqing.tutorial;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.shiqing.tutorial.AddressBookProtos.AddressBook;
import org.shiqing.tutorial.AddressBookProtos.Person;
import org.shiqing.tutorial.AddressBookProtos.Person.PhoneNumber;

/**
 * This is used to test deserialize
 */
public class Reader {
    private void print(AddressBook addressBook) {
        for (Person person : addressBook.getPeopleList()) {
            System.out.println("Id : " + person.getId());
            System.out.println("Name : " + person.getName());
            System.out.println("Email : " + person.getEmail());
            for (PhoneNumber phoneNumber : person.getPhonesList()) {
                System.out.print(phoneNumber.getType() + " : ");
                System.out.println(phoneNumber.getNumber());
            }
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Reader reader = new Reader();
        
        if (args.length != 1) {
            System.err.println("Usage:  ListPeople ADDRESS_BOOK_FILE");
            System.exit(-1);
        }
        
        AddressBook addressBook = AddressBook.parseFrom(new FileInputStream(args[0]));
        
        reader.print(addressBook);
    }
}
