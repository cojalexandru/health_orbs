"# **Pickable Orbs** - Starter Guide" +
"Here you'll see everything you need to create a custom orb/modify the defaults ones or regenerate the default ones." +


"## Locating the orbs folder" +
"Depending on the minecraft instances manager you're using you should be able to get to the mods config folder pretty easy." +
"The easiest method that is for sure going to work on any of these launchers is opening the game going to the Settings menu and opening the Resource Packs tab and clicking on the **Open Resource Pack folder** button which is going to open you a file manager instance located in the almost exact spot that you need to be so you can begin everything, " +
"so to do that go back one folder in the directories tree and after that you should see a **config** folder, click on that one and then you should see a **pickableorbs** folder, open that and that's it!" +

"## Modifying already existing orbs" +
"This is just a matter of making sure that the generation of the default orbs is set to false in the **common.toml** file, after that go to the **orbs** folder where you're going to see each orb in it's glory, open one of those files to start editing whatever orb you like." +
"## Creating custom orbs" +
"This one is easy as well, just create a new **.json** file and in the next step you'll see every parameter that you can enter in there and what it does." +

"## Orbs Data" +
"So in the end, the structure of an orb should look something of the sort:" +

"{" +
"  "OrbData": {" +
"    "type": "healing"," +
"    "effectMultiplier": 2," +
"    "color": "#FF0046"," +
"    "blockList": ["minecraft:spawner"]," +
"    "blockNames": []," +
"    "blockListType": "whitelist"," +
"    "blockDropChance": 30.0," +
"    "entityList": ["minecraft:pig", "minecraft:sheep"]," +
"    "entityNames": []," +
"    "entityListType": "blacklist"," +
"    "entityDropChance": 15.5" +
"  }," +
"  "ExtraData": {" +
"    "pickup-message": "You've picked up a health orb!"," +
"    "animation": false," +
"    "sound": true," +
"    "follow-player": false," +
"    "pickup-delay": 10" +
"  }" +
"}" +

"## Orbs Data - what do they mean????" +
"Well let's start with the **OrbData** field first, there we can have:" +
">     "name": "<name>" -> The default name that an orb receives is the name of the file, but you can change that by adding this parameter" +
">     "type": "<type>" -> At the moment there are 8 types of orbs, **healing**, **poisonous**, **damaging**, **jumping**, **speedster**, **confusion**, **levitation** and **fire_resistance**." +
">     "effectMultiplier": "<number value>", -> Sets the multiplier of the effect" +
">     "effectDuration": "<number value>", -> Sets the duration of the effect, unavailable for the **healing** and **damaging** types." +
">     "color": "<color hex code>", -> Sets the color that should be applied over the plain texture of an orb, just use any color picker you can find on google that also offers you an hex code." +
">     "blockList": [<list of block tags and id's>], -> Sets the blocks that should/shouldn't have a chance of spawning this Orb when they're broken, ex: "blockList": ["forge:storage_blocks/netherite", "minecraft:dirt"]" +
">     "blockNames": [<list of block names>], -> It does the same thing as the "blockList" parameter but in this cause you need to put the names of blocks, this was added for those who find a mod that is incompatible but still want a way of adding blocks from that mod to "the "blockList", ex: "blockNames": ["Dirt", "Stone"]" +
">     "blockListType": "<whitelist/blacklist>", -> Sets the type of the **blockList** and **blockNames** lists." +
">     "blockDropChance": <number ranging from 0.0 to 100.0>, -> Sets the chance of the orb to spawn then one of the blocks in the block lists are broken." +
">     "entityList": [<list of entity id's>], -> Sets the entities that should/shouldn't have a chance of spawning this Orb when they're killed, ex: "entityList": ["minecraft:skeleton", "minecraft:chicken"]" +
">     "entityNames": [<list of block names>], -> It does the same thing as the "entityList" parameter but in this cause you need to put the names of entities, this was added for those who find a mod that is incompatible but still want a way of adding entities from that "mod to the "entityList", ex: "entityNames": ["Wither Skeleton", "Blaze"]" +
">     "entityListType": "<whitelist/blacklist>", -> Sets the type of the **entityList** and **entityNames** lists." +
">     "entityDropChance": <number ranging from 0.0 to 100.0>, -> Sets the chance of the orb to spawn then one of the entities in the entity lists are killed." +

"Ok that's all for that field, let's do the **ExtraData** field now:" +

">     "pickup-message": "<string message>", -> Sets the message that the player should receive when he pickups up this orb." +
">     "animation": <true/false>, -> Toggles the animation for this orb." +
">     "sound": <true/false>, -> Toggles the sound for this orb." +
">     "follow-player": <true/false>, -> Toggles the follow player function (exactly how an Experience Orb does in vanilla) for this orb." +
">     "pickup-delay": <time in ticks (20 ticks = 1 second)> -> Sets the pickup delay(the amount of time that has to pass after the orb was spawned so that a player can pick it up) of this orb." +

### I know it seems like a lot but really it isn't that much, especially after you've read it one time and figured what everything means, if you're still having issues/you didn't understand something contact me on the [Decursio Project Discord](https://discord.com/invite/EWuqDrPF49)."