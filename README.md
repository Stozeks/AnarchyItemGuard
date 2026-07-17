# AnarchyItemGuard

AnarchyItemGuard is a configurable Paper plugin for Minecraft 1.16.5 that prevents players from using selected custom items inside WorldGuard regions they do not own or belong to.

The project was created as a portfolio plugin focused on clean architecture, configurable item matching, WorldGuard integration, Maven builds, and maintainable code structure.

## Features

- WorldGuard region protection
- Configurable blocked items
- Material matching
- Display name matching
- Lore matching
- PersistentDataContainer matching
- `/aig reload` command
- Permission-based command access
- Config validation
- Safe configuration reload
- Main-hand interaction filtering
- Click-location region checks
- Maven build support

## Requirements

- Java 8
- Paper or Spigot 1.16.5
- WorldEdit compatible with WorldGuard 7
- WorldGuard 7.0.5
- Maven

## Installation

1. Build the plugin with Maven.
2. Copy the generated JAR from the `target` directory.
3. Place the JAR in the server's `plugins` directory.
4. Install compatible versions of WorldEdit and WorldGuard.
5. Start the server.
6. Edit the generated `config.yml`.
7. Use `/aig reload` to apply configuration changes.

## Commands

| Command | Description | Permission |
|---|---|---|
| `/aig reload` | Reloads the plugin configuration | `anarchyitemguard.reload` |

The reload permission is granted to server operators by default.

## Configuration

Example:

```yaml
blocked-items:
  trapka:
    material: ENDER_PEARL
    display-name: "&cТрапка"
    lore:
      - "&7Запрещено использовать"
      - "&7в чужих регионах."
    pdc-key: "customitems:item_id"
    pdc-value: "trapka"

  plast:
    material: CHORUS_FRUIT
    display-name: "&5Пласт"
    lore:
      - "&7Запрещено использовать"
      - "&7в чужих регионах."
    pdc-key: "customitems:item_id"
    pdc-value: "plast"

messages:
  blocked-in-region: "&cYou cannot use this item inside another player's region."
  reload-success: "&aAnarchyItemGuard configuration reloaded successfully."
  reload-failed: "&cThe configuration could not be reloaded. Check the server console."
  no-permission: "&cYou do not have permission to use this command."
  usage: "&eUsage: /aig reload"