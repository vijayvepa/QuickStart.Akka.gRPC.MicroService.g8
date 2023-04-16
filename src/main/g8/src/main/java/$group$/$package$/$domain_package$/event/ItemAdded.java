package $group$.$package$.$domain_package$.event;

import common.CborSerializable;
import $group$.$package$.$domain_package$.$domain$Event;

public record ItemAdded(String the$domain$Id, String itemId, int quantity) implements $domain$Event, CborSerializable {

}
