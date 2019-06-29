package com.jproger.conferencetelegrambot.api;

import com.jproger.conferencetelegrambot.entities.Contact;
import java.util.List;

public interface ContactAPI {

  boolean isTelegramIDAlreadyExists(String id);

  List<Contact> getContacts();

  void addContact(Contact contact);

  Contact getContctByTelegramID(String id);
}
