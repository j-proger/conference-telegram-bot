package com.jproger.conferencetelegrambot.api;

import com.jproger.conferencetelegrambot.entities.Contact;
import java.util.List;
import java.util.Set;

public interface ContactAPI {

  boolean telegramIDExists(String id);

  Set<Contact> getContacts();

  void addContact(Contact contact);

  Contact getContactByTelegramID(String id);
}
