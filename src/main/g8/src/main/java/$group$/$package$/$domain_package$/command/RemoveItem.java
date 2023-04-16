package $group$.$package$.$domain_package$.command;

import akka.actor.typed.ActorRef;
import akka.pattern.StatusReply;
import common.CborSerializable;
import $group$.$package$.$domain_package$.$domain$Command;
import $group$.$package$.$domain_package$.model.Summary;

public record RemoveItem(String itemId,  ActorRef<StatusReply<Summary>> replyTo) implements $domain$Command, CborSerializable {
}
