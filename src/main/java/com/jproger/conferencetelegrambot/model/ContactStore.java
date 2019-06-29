package com.jproger.conferencetelegrambot.model;

import com.jproger.conferencetelegrambot.api.ContactAPI;
import com.jproger.conferencetelegrambot.entities.Contact;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactStore implements ContactAPI {

  private static volatile ContactStore instance = null;

  private List<Contact> contacts;


  private ContactStore() {
    this.contacts = new ArrayList<Contact>();
  }

  public static ContactStore getInstance() {
    if (instance == null) {

      synchronized(ContactStore.class) {
        if (instance == null) {
          instance = new ContactStore();
        }
      }
    }
    return instance;
  }


  @Override
  public boolean isTelegramIDAlreadyExists(String id) {
    Optional<Contact> contact = contacts.stream().filter(a -> id.equals(a.getTelegramID())).findFirst();
    return  Optional.ofNullable(contact).map(o -> true).orElse(false);
  }

  @Override
  public List<Contact> getContacts() {
    return contacts;
  }

  @Override
  public void addContact(Contact contact) {
    contacts.add(contact);
  }

  @Override
  public Contact getContctByTelegramID(String id) {
    return contacts.stream().filter(a -> id.equals(a.getTelegramID())).findFirst().get();
  }


}
