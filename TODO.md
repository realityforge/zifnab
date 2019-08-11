# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

* Change usages of `Collections.unmodifiableList(...)` so that they are compiled out in
  production mode.

* Create `XConfig` objects for representing all the entities that we are going to be manipulating.
  These should have the ability:
  - to parse data from `DataElement` instances.
  - parse delta variants from `DataElement` instances.
  - output to `DataElement` instances.
  - validate according to other `XConfig` instances.
  - Convert to `XModel` instances that is the fully resolved model?

* Investigate other tools and see if they offer any ideas:
  - https://github.com/endless-sky/endless-sky-tools
  - https://github.com/EndlessSkyCommunity/awesome-endless-sky

* Add a simple application that exposes a (read-only?) GraphQL API for accessing ES data.

* Consider renaming `Minable` to `Mineral`, `MineralAsteroid` or similar.

* Add separate verification phase that verifies hard constraints. These constraints may include;
  - cross-entity references all valid. i.e. `System.Trade.Name` exists as commodity defined in `Trade.Commodity.Name`
  - images referenced in sprites are all present and accounted for.
  - sounds referenced are all present and accounted for.
  - No planets with duplicate names exist.
  - No systems with duplicate names exist.
  - `StellarObject` instances have position.
  - `StellarObject` instances do not contain themselves.
  - `Commodity` instances either have both high and low or neither fields set.
  etc.
