package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.attachment.Attachment;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Address;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Email;
import seedu.address.model.task.Name;
import seedu.address.model.task.Phone;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;

/**
 * JAXB-friendly version of the Task.
 */
public class XmlAdaptedTask {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Task's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String priority;
    @XmlElement(required = true)
    private String deadline;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();
    @XmlElement
    private List<XmlAdaptedAttachment> attachments = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedTask. This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTask() {
    }

    /**
     * Constructs an {@code XmlAdaptedTask} with the given task details.
     */
    public XmlAdaptedTask(String name, String phone, String priority, String deadline, String email, String address,
                          List<XmlAdaptedTag> tagged, List<XmlAdaptedAttachment> attachments) {
        this.name = name;
        this.phone = phone;
        this.priority = priority;
        this.deadline = deadline;
        this.email = email;
        this.address = address;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
        if (attachments != null) {
            this.attachments = new ArrayList<>(attachments);
        }
    }

    /**
     * Converts a given Task into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedTask
     */
    public XmlAdaptedTask(Task source) {
        name = source.getName().value;
        phone = source.getPhone().value;
        priority = source.getPriority().value;
        deadline = source.getDeadline().toString();
        email = source.getEmail().value;
        address = source.getAddress().value;
        tagged = source.getTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());
        attachments = source.getAttachments().stream()
            .map(XmlAdaptedAttachment::new)
            .collect(Collectors.toList());
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted
     *                               task
     */
    public Task toModelType() throws IllegalValueException {


        if (name == null) {
            throw new IllegalValueException(
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (priority == null) {
            throw new IllegalValueException(
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Priority.class.getSimpleName()));
        }
        if (!Priority.isValidPriority(priority)) {
            throw new IllegalValueException(Priority.MESSAGE_PRIORITY_CONSTRAINTS);
        }
        final Priority modelPriority = new Priority(priority);

        if (deadline == null) {
            throw new IllegalValueException(
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Deadline.class.getSimpleName()));
        }
        Deadline modelDeadline;
        try {
            modelDeadline = new Deadline(deadline);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(Deadline.MESSAGE_DEADLINE_CONSTRAINTS, e);
        }

        if (email == null) {
            throw new IllegalValueException(
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(
                String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final List<Tag> taskTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            taskTags.add(tag.toModelType());
        }
        final Set<Tag> modelTags = new HashSet<>(taskTags);

        final List<Attachment> taskAttachments = new ArrayList<>();
        for (XmlAdaptedAttachment attachment : attachments) {
            taskAttachments.add(attachment.toModelType());
        }
        final Set<Attachment> modelAttachments = new HashSet<>();
        Set<String> attachmentNames = new HashSet<>();
        for (Attachment attachment : taskAttachments) {
            if (attachmentNames.contains(attachment.getName())) {
                throw new IllegalValueException(Attachment.MESSAGE_DUPLICATE_ATTACHMENT_NAME);
            } else {
                modelAttachments.add(attachment);
                attachmentNames.add(attachment.getName());
            }
        }

        return new Task(modelName, modelPhone, modelPriority, modelEmail,
            modelDeadline, modelAddress, modelTags, modelAttachments);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedTask)) {
            return false;
        }

        XmlAdaptedTask otherPerson = (XmlAdaptedTask) other;
        return Objects.equals(name, otherPerson.name)
            && Objects.equals(phone, otherPerson.phone)
            && Objects.equals(priority, otherPerson.priority)
            && Objects.equals(email, otherPerson.email)
            && Objects.equals(address, otherPerson.address)
            && tagged.equals(otherPerson.tagged)
            && attachments.equals(otherPerson.attachments);
    }
}
