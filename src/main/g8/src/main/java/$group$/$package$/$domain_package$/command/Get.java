package $package$.$domain_package$.command;

import akka.actor.typed.ActorRef;
import $package$.$domain_package$.$domain$Command;
import $package$.$domain_package$.model.Summary;

public record Get(ActorRef<Summary> replyTo) implements $domain$Command {
}
