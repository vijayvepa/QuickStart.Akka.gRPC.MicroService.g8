package $package$.cart.event;

import common.CborSerializable;
import $package$.$domain_package$.$domain$Event;

public record ItemAdded(String cartId, String itemId, int quantity) implements $domain$Event, CborSerializable {

}
