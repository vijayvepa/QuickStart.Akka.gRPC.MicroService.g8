package $group$.$package$.$domain_package$.event;

import common.CborSerializable;
import $group$.$package$.$domain_package$.$domain$Event;

public record ItemQuantityAdjusted(String the$domain$Id, String itemId, int updatedQuantity) implements $domain$Event, CborSerializable {
}
