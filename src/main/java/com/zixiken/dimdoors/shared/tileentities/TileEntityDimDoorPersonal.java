/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zixiken.dimdoors.shared.tileentities;

import com.zixiken.dimdoors.shared.EnumPocketType;
import com.zixiken.dimdoors.shared.PocketRegistry;
import com.zixiken.dimdoors.shared.RiftRegistry;
import com.zixiken.dimdoors.shared.TeleporterDimDoors;
import com.zixiken.dimdoors.shared.util.Location;
import com.zixiken.dimdoors.shared.world.DimDoorDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 *
 * @author Robijnvogel
 */
public class TileEntityDimDoorPersonal extends TileEntityDimDoor {

    public TileEntityDimDoorPersonal() {
        canRiftBePaired = false;
    }

    @Override
    public boolean tryTeleport(Entity entity) { //this door is never paired
        Location locationOfThisRift = RiftRegistry.INSTANCE.getRiftLocation(this.riftID);
        Location tpLocation;
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) entity;
            if (locationOfThisRift.getDimensionID() == DimDoorDimensions.getPocketDimensionType(EnumPocketType.PRIVATE).getId()) {
                tpLocation = PocketRegistry.INSTANCE.getPocket(this.pocketID, EnumPocketType.PRIVATE).getDepthZeroLocation();
            } else {
                int otherDoorID = PocketRegistry.INSTANCE.getPrivateDimDoorID(entityPlayer.getCachedUniqueIdString());
                tpLocation = RiftRegistry.INSTANCE.getTeleportLocation(otherDoorID);
                int privatePocketID = RiftRegistry.INSTANCE.getPocketID(otherDoorID);
                PocketRegistry.INSTANCE.getPocket(privatePocketID, EnumPocketType.PRIVATE).setDepthZeroLocation(this.getTeleportTargetLocation());
            }
        } else {
            return false;
        }
        return TeleporterDimDoors.instance().teleport(entity, tpLocation);
    }

}
