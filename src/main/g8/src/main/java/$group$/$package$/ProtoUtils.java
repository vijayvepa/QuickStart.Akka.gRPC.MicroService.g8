package $group$.$package$;

import $group$.$package$.$domain_package$.event.ItemAdded;
import $group$.$package$.$domain_package$.model.Summary;
import $group$.$package$.$domain_package$.proto.Cart;
import $group$.$package$.$domain_package$.proto.Get$projection$Response;
import $group$.$package$.$domain_package$.proto.Item;
import $group$.$package$.$projection_package$.model.$projection$;

import java.util.List;
import java.util.Optional;

public class ProtoUtils {
  private ProtoUtils(){}

  public static Cart toProtoSummary(Summary summary) {
    final List<Item> protoItems = summary.items().entrySet().stream()
        .map(entry -> Item.newBuilder()
            .setItemId(entry.getKey())
            .setQuantity(entry.getValue())
            .build()).toList();

    return Cart.newBuilder().addAllItems(protoItems).build();
  }

  public static Get$projection$Response toProto$projection$(Optional<$projection$> itemPopularity) {
    long count = itemPopularity.map($projection$::count).orElse(0L);
    String id = itemPopularity.map($projection$::itemId).orElse("");
    return Get$projection$Response.newBuilder()
        .setItemId(id).setPopularityCount(count).build();
  }

  public static shopping.cart.proto.ItemAdded toProtoItemAdded(ItemAdded someItemAdded) {
    return shopping.cart.proto.ItemAdded.newBuilder()
        .setCartId(someItemAdded.cartId())
        .setItemId(someItemAdded.itemId())
        .setQuantity(someItemAdded.quantity())
        .build();
  }
}
