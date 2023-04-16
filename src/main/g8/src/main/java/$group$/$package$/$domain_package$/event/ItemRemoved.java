package $group$.$package$.$domain_package$.event;

import common.CborSerializable;
import $group$.$package$.$domain_package$.$domain$Event;

public record ItemRemoved(String cartId, String itemId) implements $domain$Event, CborSerializable {
}
