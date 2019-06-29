package com.jproger.conferencetelegrambot.model;

import com.jproger.conferencetelegrambot.api.ContactAPI;
import com.jproger.conferencetelegrambot.entities.Contact;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ContactStore implements ContactAPI {

    private Map<String, Contact> contacts = new ConcurrentHashMap<>();

    @Override
    public boolean telegramIDExists(String id) {
        return this.contacts.containsKey(id);
    }

    @Override
    public Set<Contact> getContacts() {
        return Collections.unmodifiableSet(new HashSet<>(this.contacts.values()));
    }

    @Override
    public void addContact(Contact contact) {
        this.contacts.put(contact.getTelegramID(), contact);
    }

    @Override
    public Contact getContactByTelegramID(String telegramID) {
        return this.contacts.get(telegramID);
    }
}
