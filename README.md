# RealEstate
A light GriefPrevention addon which makes you able to buy/sell claims listed for sale.

This plugin made to target directly for servers using Vault and GriefPrevention. This plugin is made, so there is a free light weight and efficient addon for all the servers that wants to implement a real estate aspect to their server.

# Commands only work when standing inside the claims or thru the GUI.(Exception of help/list/gui) 
* /property help
* /property create <name> <price>
* /property buy
* /property delete
* /property list/gui

When a player buys a property, the ownership depends on the claim-type. If the claim is a subclaim, they will only be trusted, and the receipt is stored in a file, easily to access for that player in case he gets untrusted.
If the claim is a normal claim, and not a subclaim, then the ownership will be transferred to the buyer, including the claim blocks, as the seller gets his claimblocks back.

# Permissions:
* property.help
* property.create
* property.buy
* property.delete
* property.list
* property.admin

