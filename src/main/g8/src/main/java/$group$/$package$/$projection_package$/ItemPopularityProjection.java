package $package$.$projection_package$;

import akka.actor.typed.ActorSystem;
import akka.cluster.sharding.typed.ShardedDaemonProcessSettings;
import akka.cluster.sharding.typed.javadsl.ShardedDaemonProcess;
import akka.persistence.jdbc.query.javadsl.JdbcReadJournal;
import akka.persistence.query.Offset;
import akka.projection.ProjectionBehavior;
import akka.projection.ProjectionId;
import akka.projection.eventsourced.EventEnvelope;
import akka.projection.eventsourced.javadsl.EventSourcedProvider;
import akka.projection.javadsl.ExactlyOnceProjection;
import akka.projection.javadsl.SourceProvider;
import akka.projection.jdbc.javadsl.JdbcHandler;
import akka.projection.jdbc.javadsl.JdbcProjection;
import common.JpaSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import $package$.$domain_package$.$domain$;
import $package$.$domain_package$.event.ItemAdded;
import $package$.$domain_package$.$domain$Event;
import $package$.$projection_package$.model.$projection$;
import $package$.$projection_package$.repository.$projection$Repository;

import java.util.Optional;

public class $projection$Projection {
  private $projection$Projection() {
  }

  public static void init(
      ActorSystem<?> system,
      JpaTransactionManager transactionManager,
      $projection$Repository repository
  ) {

    final ShardedDaemonProcess process = ShardedDaemonProcess.get(system);
    process.init(
        ProjectionBehavior.Command.class,
        "$projection$Projection",
        $domain$.TAGS.size(),
        index -> ProjectionBehavior.create(create$projection$ProjectionForIndex(system, transactionManager, repository, index)),
        ShardedDaemonProcessSettings.create(system),
        Optional.of(ProjectionBehavior.stopMessage())

    );

  }

  private static ExactlyOnceProjection<Offset, EventEnvelope<$domain$Event>> create$projection$ProjectionForIndex(
      ActorSystem<?> system,
      JpaTransactionManager transactionManager,
      $projection$Repository repository,
      int index) {

    final String tag = $domain$.TAGS.get(index);
    final SourceProvider<Offset, EventEnvelope<$domain$Event>> sourceProvider =
        EventSourcedProvider.eventsByTag(system, JdbcReadJournal.Identifier(), tag);

    return JdbcProjection.exactlyOnce(
        ProjectionId.of("$projection$Projection", tag),
        sourceProvider,
        () -> new JpaSession(transactionManager),
        () -> new $projection$ProjectionHandler(tag, repository),
        system
    );
  }

  static class $projection$ProjectionHandler extends JdbcHandler<EventEnvelope<$domain$Event>, JpaSession> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String tag;
    private final $projection$Repository repository;

    public $projection$ProjectionHandler(
        String tag,
        $projection$Repository repository) {
      this.tag = tag;
      this.repository = repository;
    }

    private $projection$ findOrNew(String itemId) {
      return repository.findById(itemId).orElseGet(() -> new $projection$(itemId, 0L, 0L));
    }

    @Override
    public void process(
        JpaSession session,
        EventEnvelope<$domain$Event> eventEnvelope) {

      final $domain$Event event = eventEnvelope.event();


      Optional<$projection$> updated$projection$ = getUpdated$projection$(event);
      updated$projection$.ifPresent(repository::save);
    }

    private Optional<$projection$> getUpdated$projection$($domain$Event event) {

      if (event instanceof ItemAdded someItemAdded) {
        final $projection$ existing$projection$ = findOrNew(someItemAdded.itemId());

        final $projection$ updated$projection$ = existing$projection$.changeCount(someItemAdded.quantity());
        logger.info("$projection$ProjectionHandler({}) item patched for '{}' : [{}]", this.tag, someItemAdded.itemId(), updated$projection$);
        return Optional.of(updated$projection$);

      }
      return Optional.empty();
    }

  }

}
