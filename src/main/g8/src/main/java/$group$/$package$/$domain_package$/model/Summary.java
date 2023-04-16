package $group$.$package$.$domain_package$.model;

import common.CborSerializable;

import java.util.Map;

public record Summary(Map<String, Integer> items, boolean checkedOut) implements CborSerializable {
}
