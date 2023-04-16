package $package$.cart.command;

import akka.actor.typed.ActorRef;
import akka.pattern.StatusReply;
import $package$.$domain_package$.$domain$Command;
import $package$.$domain_package$.model.Summary;

public record Checkout(ActorRef<StatusReply<Summary>> replyTo) implements $domain$Command {
}
