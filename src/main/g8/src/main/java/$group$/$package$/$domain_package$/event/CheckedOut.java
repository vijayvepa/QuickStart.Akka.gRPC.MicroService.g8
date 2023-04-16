package $group$.$package$.$domain_package$.event;

import $group$.$package$.$domain_package$.$domain$Event;

import java.time.Instant;

public record CheckedOut(String the$domain$Id, Instant eventTime) implements $domain$Event {
}
