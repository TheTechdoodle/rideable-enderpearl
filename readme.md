![Screenshot](https://i.imgur.com/Oca5ck9.png)

This simple plugin allows players to fly across the sky on any enderpearls they throw.

The WorldGuard flag "ride-enderpearl" can be set to "deny" to prevent riding enderpearls in a region.

Permissions:
 - rideableenderpearl.ride\
   Allows the player to use the plugin. Granted by default.
 - rideableenderpearl.reload\
   Permission to reload the config.

Commands:
 - /rideableenderpearl or /rep\
   Reloads the config

Config:
```yaml
enderpearl-particles: true
prevent-suffocation-damage: true
prevent-throwing-while-riding: true
prevent-dismount: true
```