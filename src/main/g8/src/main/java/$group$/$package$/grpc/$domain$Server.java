package $group$.$package$.grpc;

import akka.actor.typed.ActorSystem;
import akka.grpc.javadsl.ServerReflection;
import akka.grpc.javadsl.ServiceHandler;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.japi.function.Function;


import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletionStage;

import $group$.$package$.proto.$domain$Service;
import $group$.$package$.proto.$domain$ServiceHandlerFactory;


public class $domain$Server {

  private $domain$Server() {
  }

   public static void start(
       String host,
       int port,
       ActorSystem<?> system,
       $domain$Service shopping$domain$Service) {

    @SuppressWarnings("unchecked")
    Function<HttpRequest, CompletionStage<HttpResponse>> service =
        ServiceHandler.concatOrNotFound(
            get$domain$ServiceHandler(system, shopping$domain$Service),
            getServerReflection(system));

    CompletionStage<ServerBinding> serverBinding =
        Http.get(system).newServerAt(host, port).bind(service);

    serverBinding.whenComplete(
        (binding, ex) -> onBindingComplete(binding, ex, system));
  }

  private static Function<HttpRequest, CompletionStage<HttpResponse>> get$domain$ServiceHandler(
      ActorSystem<?> system,
      $domain$Service grpcService) {

    return $domain$ServiceHandlerFactory.create(grpcService, system);
  }

  /**
   *
   * ServerReflection enabled to support grpcurl without import-path and proto parameters
   * @param system actor system
   * @return server reflection
   */
  private static Function<HttpRequest, CompletionStage<HttpResponse>> getServerReflection(ActorSystem<?> system) {
    return ServerReflection.create(
        Collections.singletonList($domain$Service.description), system);
  }

  static void onBindingComplete(ServerBinding binding, Throwable ex, ActorSystem<?> system){

    if (binding == null) {
      system.log().error("Failed to bind gRPC endpoint, terminating system", ex);
      system.terminate();
      return;
    }

    binding.addToCoordinatedShutdown(Duration.ofSeconds(3), system);

    InetSocketAddress address = binding.localAddress();
    system.log().info(
        "Shopping API online at gRPC server {}:{}", address.getHostString(), address.getPort());
  }
}
