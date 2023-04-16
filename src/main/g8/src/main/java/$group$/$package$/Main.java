package $package$;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import akka.management.javadsl.AkkaManagement;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import $package$.$domain_package$.$domain$;
import shopping.grpc.$domain$Server;
import shopping.grpc.$domain$ServiceImpl;
import shopping.kafka.ProduceEventsProjection;
import shopping.popularity.$projection$Projection;
import shopping.popularity.repository.$projection$Repository;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);


  public static void main(String[] args) {
    ActorSystem<Void> system = ActorSystem.create(Behaviors.empty(), "$domain$Service");
    try {
      init(system);
    } catch (Exception e) {
      logger.error("Terminating due to initialization failure.", e);
      system.terminate();
    }
  }

  public static void init(ActorSystem<Void> system) {
    AkkaManagement.get(system).start();
    ClusterBootstrap.get(system).start();

    ApplicationContext springContext = SpringIntegration.applicationContext(system);

    $projection$Repository itemPopularityRepository =
        springContext.getBean($projection$Repository.class);
    final JpaTransactionManager jpaTransactionManager =
        springContext.getBean(JpaTransactionManager.class);

    $projection$Projection.init(system, jpaTransactionManager, itemPopularityRepository);
    ProduceEventsProjection.init(system, jpaTransactionManager);

    final Config config = system.settings().config();
    final String grpcInterface = config.getString("$name$.grpc.interface");
    final int grpcPort = config.getInt("$name$.grpc.port");

    final $domain$ServiceImpl shoppingCartService = new $domain$ServiceImpl(system, itemPopularityRepository);
    $domain$Server.start(grpcInterface, grpcPort, system, shoppingCartService);
    $domain$.init(system);


  }
}