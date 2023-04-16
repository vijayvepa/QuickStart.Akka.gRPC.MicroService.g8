package $package$.$domain_package$;

import common.CborSerializable;
import $package$.$domain_package$.model.Summary;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class $domain$State implements CborSerializable {
  private final Map<String, Integer> items;
  private Optional<Instant> checkoutDate;

  public $domain$State(
      Map<String, Integer> items,
      Optional<Instant> checkoutDate) {
    this.items = items;
    this.checkoutDate = checkoutDate;
  }

  public Summary toSummary() {
    return new Summary(new HashMap<>(items), false);
  }

  public int itemCount(String itemId) {
    return items.get(itemId);
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public boolean hasItem(String itemId) {
    return items.containsKey(itemId);
  }

  public $domain$State updateItem(
      String itemId,
      int quantity) {
    if (quantity == 0) {
      items.remove(itemId);
      return this;
    }

    items.put(itemId, quantity);
    return this;
  }

  public $domain$State removeItem(String itemId){
    items.remove(itemId);
    return this;
  }

  public boolean isCheckedOut() {
    return checkoutDate.isPresent();
  }

  public $domain$State checkout() {
    checkoutDate = Optional.of(Instant.now());
    return this;
  }

  public Map<String, Integer> items() {
    return items;
  }

  public Optional<Instant> checkoutDate() {
    return checkoutDate;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = ($domain$State) obj;
    return Objects.equals(this.items, that.items) &&
        Objects.equals(this.checkoutDate, that.checkoutDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, checkoutDate);
  }

  @Override
  public String toString() {
    return "$domain$State[" +
        "items=" + items + ", " +
        "checkoutDate=" + checkoutDate + ']';
  }

}
