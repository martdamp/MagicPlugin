package com.elmakers.mine.bukkit.plugins.magic.spells;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.elmakers.mine.bukkit.blocks.BlockList;
import com.elmakers.mine.bukkit.plugins.magic.BlockSpell;
import com.elmakers.mine.bukkit.plugins.magic.SpellResult;
import com.elmakers.mine.bukkit.utilities.Target;
import com.elmakers.mine.bukkit.utilities.borrowed.ConfigurationNode;

public class DisintegrateSpell extends BlockSpell
{
	private final static int             DEFAULT_PLAYER_DAMAGE = 1;
	private final static int             DEFAULT_ENTITY_DAMAGE = 100;

	@Override
	public SpellResult onCast(ConfigurationNode parameters) 
	{
		Target target = getTarget();
		
		int playerDamage = parameters.getInteger("player_damage", DEFAULT_PLAYER_DAMAGE);
		int entityDamage = parameters.getInteger("entity_damage", DEFAULT_ENTITY_DAMAGE);

		if (target.isEntity())
		{
			Entity targetEntity = target.getEntity();
			if (targetEntity instanceof LivingEntity)
			{
				LivingEntity li = (LivingEntity)targetEntity;
				if (li instanceof Player)
				{
					li.damage(mage.getDamageMultiplier() * playerDamage, getPlayer());
				}
				else
				{
					li.damage(mage.getDamageMultiplier() * entityDamage, getPlayer());
				}
				return SpellResult.CAST;
			}
		}

		if (!target.hasTarget())
		{
			return SpellResult.NO_TARGET;
		}
		
		Block targetBlock = target.getBlock();
		if (!hasBuildPermission(targetBlock)) 
		{
			return SpellResult.INSUFFICIENT_PERMISSION;
		}
		if (mage.isIndestructible(targetBlock)) 
		{
			return SpellResult.NO_TARGET;
		}

		BlockList disintigrated = new BlockList();
		disintigrated.add(targetBlock);
		
		if (isUnderwater())
		{
			targetBlock.setType(Material.STATIONARY_WATER);
		}
		else
		{
			targetBlock.setType(Material.AIR);
		}
		
		registerForUndo(disintigrated);
		return SpellResult.CAST;
	}
}
