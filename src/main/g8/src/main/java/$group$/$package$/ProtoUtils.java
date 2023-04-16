package $group$.$package$;

import $group$.$package$.$domain_package$.event.ItemAdded;
import $group$.$package$.$domain_package$.model.Summary;
import $group$.$package$.proto.$domain$Response;
import $group$.$package$.proto.Get$projection$Response;
import $group$.$package$.proto.Item;
import $group$.$package$.$projection_package$.model.$projection$;

import java.util.List;
import java.util.Optional;

public class ProtoUtils {
  private ProtoUtils(){}

  public static $domain$Response toProtoSummary(Summary summary) {
    final List<Item> protoItems = summary.items().entrySet().stream()
        .map(entry -> Item.newBuilder()
            .setItemId(entry.getKey())
            .setQuantity(entry.getValue())
            .build()).toList();

    return $domain$Response.newBuilder().addAllItems(protoItems).build();
  }

  public static Get$projection$Response toProto$projection$(Optional<$projection$> itemPopularity) {
    long count = itemPopularity.map($projection$::count).orElse(0L);
    String id = itemPopularity.map($projection$::itemId).orElse("");
    return Get$projection$Response.newBuilder()
        .setItemId(id).setPopularityCount(count).build();
  }

  public static $group$.$package$.proto.ItemAdded toProtoItemAdded(ItemAdded someItemAdded) {
    return $group$.$package$.proto.ItemAdded.newBuilder()
        .setThe$domain$Id(someItemAdded.the$domain$Id())
        .setItemId(someItemAdded.itemId())
        .setQuantity(someItemAdded.quantity())
        .build();
  }
}
