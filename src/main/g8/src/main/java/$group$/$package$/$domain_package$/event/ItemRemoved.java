package $group$.$package$.$domain_package$.event;

import common.CborSerializable;
import $group$.$package$.$domain_package$.$domain$Event;

public record ItemRemoved(String the$domain$Id, String itemId) implements $domain$Event, CborSerializable {
}
