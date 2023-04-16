package $package$;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import shopping.popularity.model.$projection$;
import shopping.popularity.repository.$projection$Repository;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

/** Configure the necessary components required for integration with Akka Projections */
@Configuration
@EnableJpaRepositories(basePackageClasses = $projection$Repository.class)
@EnableTransactionManagement
public class EntityConfig {



  /** An EntityManager factory using the configured database connection settings.
   * @param dataSource data source
   * @param jpaProperties jpa properties
   **/
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, @Qualifier("jpaProperties") Properties jpaProperties) {

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setDatabase(Database.POSTGRESQL);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan($projection$.class.getPackageName());
    // set the DataSource configured with settings in jdbc-connection-settings
    factory.setDataSource(dataSource);
    // load additional properties from config jdbc-connection-settings.additional-properties
    factory.setJpaProperties(jpaProperties);

    return factory;
  }



}
