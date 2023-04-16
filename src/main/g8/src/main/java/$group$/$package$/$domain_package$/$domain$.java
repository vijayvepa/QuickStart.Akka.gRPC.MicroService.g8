package $group$.$package$.$domain_package$;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.Behaviors;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.Entity;
import akka.cluster.sharding.typed.javadsl.EntityContext;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;
import akka.pattern.StatusReply;
import akka.persistence.typed.PersistenceId;
import akka.persistence.typed.javadsl.CommandHandlerWithReply;
import akka.persistence.typed.javadsl.CommandHandlerWithReplyBuilder;
import akka.persistence.typed.javadsl.CommandHandlerWithReplyBuilderByState;
import akka.persistence.typed.javadsl.EventHandler;
import akka.persistence.typed.javadsl.EventSourcedBehavior;
import akka.persistence.typed.javadsl.EventSourcedBehaviorWithEnforcedReplies;
import akka.persistence.typed.javadsl.ReplyEffect;
import $group$.$package$.$domain_package$.command.AddItem;
import $group$.$package$.$domain_package$.command.AdjustItemQuantity;
import $group$.$package$.$domain_package$.command.Checkout;
import $group$.$package$.$domain_package$.command.Get;
import $group$.$package$.$domain_package$.command.RemoveItem;
import $group$.$package$.$domain_package$.event.CheckedOut;
import $group$.$package$.$domain_package$.event.ItemAdded;
import $group$.$package$.$domain_package$.event.ItemQuantityAdjusted;
import $group$.$package$.$domain_package$.event.ItemRemoved;
import $group$.$package$.$domain_package$.model.Summary;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static akka.pattern.StatusReply.error;
import static akka.pattern.StatusReply.success;

public class $domain$ extends EventSourcedBehaviorWithEnforcedReplies<$domain$Command, $domain$Event, $domain$State> {

  public static final List<String> TAGS = List.of("carts-0", "carts-1", "carts-2", "carts-3", "carts-4");

  public static final EntityTypeKey<$domain$Command> ENTITY_TYPE_KEY =
      EntityTypeKey.create($domain$Command.class, "$domain$");

  private final String cartId;
  private final String projectionTag;

  private $domain$(
      String cartId,
      String projectionTag) {
    super(PersistenceId.of(ENTITY_TYPE_KEY.name(), cartId));
    this.projectionTag = projectionTag;
    SupervisorStrategy.restartWithBackoff(Duration.ofMillis(200), Duration.ofSeconds(5), 0.1);
    this.cartId = cartId;
  }

  public static void init(ActorSystem<?> system) {
    final ClusterSharding clusterSharding = ClusterSharding.get(system);
    clusterSharding.init(Entity.of(ENTITY_TYPE_KEY, entityContext -> $domain$.create(entityContext.getEntityId(), getRandomProjectionTag(entityContext))));
  }

  private static String getRandomProjectionTag(EntityContext<$domain$Command> entityContext) {
    final int tagIndex = Math.abs(entityContext.getEntityId().hashCode() % TAGS.size());
    return TAGS.get(tagIndex);
  }

  public static Behavior<$domain$Command> create(
      String cartId,
      String projectionTag) {
    return Behaviors.setup(ctx -> EventSourcedBehavior.start(new $domain$(cartId, projectionTag), ctx));
  }

  @Override
  public Set<String> tagsFor($domain$Event shoppingCartEvent) {
    return Collections.singleton(projectionTag);
  }

  @Override
  public $domain$State emptyState() {
    return new $domain$State(new HashMap<>(), Optional.empty());
  }

  @Override
  public CommandHandlerWithReply<$domain$Command, $domain$Event, $domain$State> commandHandler() {
    return open$domain$().orElse(checkedOut$domain$()).orElse(get$domain$()).build();
  }

  private CommandHandlerWithReplyBuilderByState<$domain$Command, $domain$Event, $domain$State, $domain$State> open$domain$() {
    final CommandHandlerWithReplyBuilder<$domain$Command, $domain$Event, $domain$State> builder = newCommandHandlerWithReplyBuilder();
    return builder.forState(state -> !state.isCheckedOut())
        .onCommand(AddItem.class, this::onAddItem)
        .onCommand(Checkout.class, this::onCheckout)
        .onCommand(RemoveItem.class, this::onRemoveItem)
        .onCommand(AdjustItemQuantity.class, this::onAdjustItemQuantity);

  }

  private CommandHandlerWithReplyBuilderByState<$domain$Command, $domain$Event, $domain$State, $domain$State> checkedOut$domain$() {
    final CommandHandlerWithReplyBuilder<$domain$Command, $domain$Event, $domain$State> builder = newCommandHandlerWithReplyBuilder();
    return builder.forState($domain$State::isCheckedOut)
        .onCommand(AddItem.class, (state, command) -> replyError(command.replyTo(), "Can't add items to checked out cart"))
        .onCommand(RemoveItem.class, (state, command) -> replyError(command.replyTo(), "Can't remove items from checked out cart"))
        .onCommand(AdjustItemQuantity.class, (state, command) -> replyError(command.replyTo(), "Can't adjust quantity on items from checked out cart"))

        .onCommand(Checkout.class, (state, command) -> replyError(command.replyTo(), "Already checked out"));

  }

  private CommandHandlerWithReplyBuilderByState<$domain$Command, $domain$Event, $domain$State, $domain$State> get$domain$() {
    return newCommandHandlerWithReplyBuilder().forAnyState().onCommand(Get.class, this::onGet);
  }

  private ReplyEffect<$domain$Event, $domain$State> onAddItem(
      $domain$State state,
      AddItem command) {

    if (state.hasItem(command.itemId())) {
      return Effect().reply(command.replyTo(), error("Item " + command.itemId() + " ' was already added to this shopping cart."));

    }

    if (command.quantity() <= 0) {
      return Effect().reply(command.replyTo(), error("Quantity must be > 0"));
    }

    return Effect().persist(new ItemAdded(cartId, command.itemId(), command.quantity()))
        .thenReply(command.replyTo(), updatedCart -> success(updatedCart.toSummary()));
  }

  private ReplyEffect<$domain$Event, $domain$State> onCheckout(
      $domain$State state,
      Checkout command) {


    if (state.isEmpty()) {
      return Effect().reply(command.replyTo(), error("Cannot checkout an empty shopping cart."));
    }

    return Effect().persist(new CheckedOut(cartId, Instant.now()))
        .thenReply(command.replyTo(), updatedCart -> success(updatedCart.toSummary()));
  }

  private ReplyEffect<$domain$Event, $domain$State> replyError(
      ActorRef<StatusReply<Summary>> statusReplyActorRef,
      String error) {
    return Effect().reply(statusReplyActorRef, error(error));
  }

  private ReplyEffect<$domain$Event, $domain$State> onGet(
      $domain$State state,
      Get command) {

    return Effect().reply(command.replyTo(), state.toSummary());
  }

  @Override
  public EventHandler<$domain$State, $domain$Event> eventHandler() {
    return newEventHandlerBuilder().forAnyState()
        .onEvent(ItemAdded.class, this::updateItem)
        .onEvent(CheckedOut.class, this::handleCheckedOut)
        .onEvent(ItemRemoved.class, this::handleItemRemoved)
        .onEvent(ItemQuantityAdjusted.class, this::handleItemQuantityAdjusted)
        .build();
  }

  private $domain$State updateItem(
      $domain$State state,
      ItemAdded event) {
    return state.updateItem(event.itemId(), event.quantity());
  }

  private $domain$State handleCheckedOut(
      $domain$State state,
      CheckedOut event) {
    return state.checkout();
  }

  private ReplyEffect<$domain$Event, $domain$State> onRemoveItem(
      $domain$State state,
      RemoveItem command) {


    if (!state.hasItem(command.itemId())) {
      return Effect().reply(command.replyTo(), error("Item not found: " + command.itemId()));
    }

    return Effect().persist(new ItemRemoved(cartId, command.itemId()))
        .thenReply(command.replyTo(), updatedItem -> success(state.toSummary()));
  }


  private $domain$State handleItemRemoved(
      $domain$State state,
      ItemRemoved event) {
    return state.removeItem(event.itemId());
  }

  private ReplyEffect<$domain$Event, $domain$State> onAdjustItemQuantity(
      $domain$State state,
      AdjustItemQuantity command) {


    if (!state.hasItem(command.itemId())) {
      return Effect().reply(command.replyTo(), error("Item not found: " + command.itemId()));
    }

    if (command.updatedQuantity() < 0) {
      return Effect().reply(command.replyTo(), error("Quantity {} must be > 0" + command.updatedQuantity()));
    }

    return Effect().persist(new ItemQuantityAdjusted(cartId, command.itemId(), command.updatedQuantity()))
        .thenReply(command.replyTo(), updatedItem -> success(updatedItem.toSummary()));
  }


  private $domain$State handleItemQuantityAdjusted(
      $domain$State state,
      ItemQuantityAdjusted event) {
    return state.updateItem(event.itemId(), event.updatedQuantity());
  }


}
