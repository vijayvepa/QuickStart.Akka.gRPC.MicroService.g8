package $group$.$package$;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import akka.management.javadsl.AkkaManagement;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import $group$.$package$.$domain_package$.$domain$;
import $group$.$package$.grpc.$domain$Server;
import $group$.$package$.grpc.$domain$ServiceImpl;
import $group$.$package$.kafka.ProduceEventsProjection;
import $group$.$package$.$projection_package$.$projection$Projection;
import $group$.$package$.$projection_package$.repository.$projection$Repository;

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

    final $domain$ServiceImpl shopping$domain$Service = new $domain$ServiceImpl(system, itemPopularityRepository);
    $domain$Server.start(grpcInterface, grpcPort, system, shopping$domain$Service);
    $domain$.init(system);


  }
}
