package $group$.$package$.grpc;


import akka.actor.typed.ActorSystem;
import akka.actor.typed.DispatcherSelector;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import common.GrpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import $group$.$package$.ProtoUtils;
import $group$.$package$.$domain_package$.$domain$;
import $group$.$package$.$domain_package$.$domain$Command;
import $group$.$package$.$domain_package$.command.AddItem;
import $group$.$package$.$domain_package$.command.AdjustItemQuantity;
import $group$.$package$.$domain_package$.command.Checkout;
import $group$.$package$.$domain_package$.command.Get;
import $group$.$package$.$domain_package$.command.RemoveItem;
import $group$.$package$.$projection_package$.model.$projection$;
import $group$.$package$.$domain_package$.model.Summary;
import $group$.$package$.proto.AddItemRequest;
import $group$.$package$.proto.AdjustItemQuantityRequest;
import $group$.$package$.proto.$domain$Response;
import $group$.$package$.proto.CheckoutRequest;
import $group$.$package$.proto.Get$domain$Request;
import $group$.$package$.proto.Get$projection$Request;
import $group$.$package$.proto.Get$projection$Response;
import $group$.$package$.proto.RemoveItemRequest;
import $group$.$package$.proto.$domain$Service;
import $group$.$package$.$projection_package$.repository.$projection$Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import static common.GrpcUtils.convertError;


public class $domain$ServiceImpl implements $domain$Service {

  private final ClusterSharding sharding;
  private final Duration timeout;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final $projection$Repository repository;

  private final Executor blockingJdbcExecutor;

  public $domain$ServiceImpl(
      ActorSystem<?> system,
      $projection$Repository repository) {
    sharding = ClusterSharding.get(system);
    timeout = system.settings().config().getDuration("$name$.ask-timeout");
    blockingJdbcExecutor = getBlockingJdbcExecutor(system);
    this.repository = repository;
  }

  private Executor getBlockingJdbcExecutor(ActorSystem<?> system) {
    return system.dispatchers().lookup(DispatcherSelector.fromConfig("akka.projection.jdbc.blocking-jdbc-dispatcher"));
  }

  @Override
  public CompletionStage<$domain$Response> addItem(AddItemRequest in) {

    logger.info("addItem {} to cart {}", in.getItemId(), in.getThe$domain$Id());
    final EntityRef<$domain$Command> entityRef = getEntityRef(in.getThe$domain$Id());
    final CompletionStage<Summary> reply = entityRef.askWithStatus(replyTo -> new AddItem(in.getItemId(), in.getQuantity(), replyTo), timeout);

    final CompletionStage<$domain$Response> cart = reply.thenApply(ProtoUtils::toProtoSummary);

    return convertError(cart);
  }

  @Override
  public CompletionStage<$domain$Response> removeItem(RemoveItemRequest in) {
    logger.info("Performing RemoveItem  to cart {}", in.getThe$domain$Id());
    final EntityRef<$domain$Command> entityRef = getEntityRef(in.getThe$domain$Id());

    final CompletionStage<Summary> reply = entityRef.askWithStatus(replyTo -> new RemoveItem(in.getItemId(), replyTo), timeout);

    final CompletionStage<$domain$Response> response = reply.thenApply(ProtoUtils::toProtoSummary);

    return convertError(response);
  }

  private EntityRef<$domain$Command> getEntityRef(String entityId) {
    return sharding.entityRefFor($domain$.ENTITY_TYPE_KEY, entityId);
  }

  @Override
  public CompletionStage<$domain$Response> checkout(CheckoutRequest in) {
    logger.info("checkout {} ", in.getThe$domain$Id());

    final EntityRef<$domain$Command> entityRef = getEntityRef(in.getThe$domain$Id());
    final CompletionStage<Summary> summary = entityRef.askWithStatus(Checkout::new, timeout);
    final CompletionStage<$domain$Response> cart = summary.thenApply(ProtoUtils::toProtoSummary);
    return convertError(cart);

  }

  @Override
  public CompletionStage<$domain$Response> get$domain$(Get$domain$Request in) {
    logger.info("get$domain$ {}", in.getThe$domain$Id());
    final EntityRef<$domain$Command> entityRef = getEntityRef(in.getThe$domain$Id());
    final CompletionStage<Summary> get = entityRef.ask(Get::new, timeout);

    final CompletionStage<$domain$Response> proto$domain$ = GrpcUtils.handleNotFound(
        get,
        summary -> summary.items().isEmpty(),
        ProtoUtils::toProtoSummary,
        String.format("$domain$ %s is empty", in.getThe$domain$Id()));

    return convertError(proto$domain$);

  }

  @Override
  public CompletionStage<$domain$Response> adjustItemQuantity(AdjustItemQuantityRequest in) {
    logger.info("Performing AdjustItemQuantity  to entity {}", in.getThe$domain$Id());
    final EntityRef<$domain$Command> entityRef = getEntityRef(in.getThe$domain$Id());

    final CompletionStage<Summary> reply = entityRef.askWithStatus(replyTo -> new AdjustItemQuantity(in.getItemId(), in.getQuantity(), replyTo), timeout);

    final CompletionStage<$domain$Response> response = reply.thenApply(ProtoUtils::toProtoSummary);

    return convertError(response);
  }

  @Override
  public CompletionStage<Get$projection$Response> get$projection$(Get$projection$Request in) {

    final CompletableFuture<Optional<$projection$>> itemPopularityOptional = CompletableFuture.supplyAsync(() -> repository.findById(in.getItemId()), blockingJdbcExecutor);
    return itemPopularityOptional.thenApply(ProtoUtils::toProto$projection$);
  }


}