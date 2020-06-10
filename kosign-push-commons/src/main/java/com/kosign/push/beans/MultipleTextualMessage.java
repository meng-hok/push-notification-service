package com.kosign.push.beans;

import java.util.HashSet;
import java.util.Set;

public class MultipleTextualMessage extends TextualMessage {
    Set<String> recipients;

    // getter/setter (generated)
    public MultipleTextualMessage() {
        recipients = new HashSet<>();
    }

    public void addRecipient(String recipient) {
        this.recipients.add(recipient);
    }

    public Set<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<String> recipients) {
        this.recipients = recipients;
    }

}
