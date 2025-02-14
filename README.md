# EntityTTL

A plugin that automatically removes entities after a configurable amount of time,
intended to combat simple projectile based lag machines.

> [!NOTE]
> This plugin is mainly obsolete on modern versions of paper that have the [entity despawn time](https://docs.papermc.io/paper/reference/world-configuration#entities_spawning_despawn_time) options.

## Requirements
Paper 1.19.4 or up

## Config
```yaml
entities:
  # Removal times for entities can be configured here, in ticks.
  # Values below 0 will have no effect (disabled).
  # The default 2400 ticks will make these entities disappear after 2 minutes.
  snowball: 2400
  llama_spit: 2400
  egg: 2400
  ender_pearl: 2400
  wither_skull: 2400
  potion: 2400
  experience_bottle: 2400
```
