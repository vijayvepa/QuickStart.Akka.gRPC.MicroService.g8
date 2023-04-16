package $package$.popularity.repository;

import org.springframework.data.repository.Repository;
import shopping.popularity.model.$projection$;

import java.util.Optional;

public interface $projection$Repository extends Repository<$projection$, String> {

  $projection$ save($projection$ itemPopularity);
  Optional<$projection$> findById(String id);

}
