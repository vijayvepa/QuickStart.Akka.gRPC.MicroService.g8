package $package$.cart.event;

import common.CborSerializable;
import $package$.$domain_package$.$domain$Event;

public record ItemRemoved(String cartId, String itemId) implements $domain$Event, CborSerializable {
}
