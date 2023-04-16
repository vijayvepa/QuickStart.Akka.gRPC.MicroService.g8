package $group$.$package$.$projection_package$.repository;

import org.springframework.data.repository.Repository;
import $group$.$package$.$projection_package$.model.$projection$;

import java.util.Optional;

public interface $projection$Repository extends Repository<$projection$, String> {

  $projection$ save($projection$ itemPopularity);
  Optional<$projection$> findById(String id);

}
