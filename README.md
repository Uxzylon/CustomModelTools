# CustomModelTools

Spigot Plugin with tools for CustomModelData.

## Features

- Enforce ResourcePack Download
- Parsing of CustomModelData from the ResourcePack
- Give and Place CustomModelData Items by Category and Name
- Position and Rotation Editor

## How it works

Your resource pack will obviously need to be configured to use CustomModelData.

The plugin will search for items in "assets/minecraft/models/item/"

From there, it will look for the "predicate" fields in the json files, then the "custom_model_data" field and "model" field.

The category will be the first part of the "model" field without the namespace (e.g. "item" in "mypack:item/mymodel").

The name will be the second part of the "model" field without the namespace (e.g. "mymodel" in "mypack:item/mymodel").

The plugin will then use the category and name in the commands and in the tab completion.

## Commands

- `/model3d` - Main Command
- `/model3d reload` - Reloads the Plugin
- `/model3d give <category> <name>` - Gives the Player the Item with the given Category and Name
- `/model3d place <category> <name>` - Places the Item with the given Category and Name at the Players Location
- `/model3d select` - Selects the nearest ArmorStand
- `/model3d unselect` - Deselects the selected ArmorStand
- `/model3d move` - Moves the selected ArmorStand using a GUI
- `/model3d switch` - Switches the selected CustomModelData between ArmorStand Hand and Head
- `/model3d remove` - Removes the selected ArmorStand

## Permissions

- `custommodeltools.reload` - Allows the use of `/model3d reload`
- `custommodeltools.give` - Allows the use of `/model3d give`
- `custommodeltools.place` - Allows the use of `/model3d place`
- `custommodeltools.select` - Allows the use of `/model3d select`
- `custommodeltools.unselect` - Allows the use of `/model3d unselect`
- `custommodeltools.move` - Allows the use of `/model3d move`
- `custommodeltools.switch` - Allows the use of `/model3d switch`
- `custommodeltools.remove` - Allows the use of `/model3d remove`
- `custommodeltools.*` - Allows the use of all commands

## Config

Every text in the plugin can be changed in the config.yml.

You also have several options to change the behavior of the plugin:

- `ResourcePack.url` - The URL to the ResourcePack
- `ResourcePack.hash` - The SHA-1 Hash of the ResourcePack
- `ResourcePack.kickOnFail` - Whether to kick the Player if the ResourcePack isn't applied