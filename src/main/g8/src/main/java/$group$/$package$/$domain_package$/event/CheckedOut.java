package $package$.$domain_package$.event;

import $package$.$domain_package$.$domain$Event;

import java.time.Instant;

public record CheckedOut(String cartId, Instant eventTime) implements $domain$Event {
}
