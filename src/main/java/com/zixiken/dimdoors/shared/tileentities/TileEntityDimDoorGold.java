package com.zixiken.dimdoors.shared.tileentities;

import com.zixiken.dimdoors.DimDoors;
import com.zixiken.dimdoors.shared.EnumPocketType;
import com.zixiken.dimdoors.shared.IChunkLoader;
import com.zixiken.dimdoors.shared.PocketRegistry;
import com.zixiken.dimdoors.shared.RiftRegistry;
import com.zixiken.dimdoors.shared.util.Location;
import com.zixiken.dimdoors.shared.world.DimDoorDimensions;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class TileEntityDimDoorGold extends TileEntityDimDoor implements IChunkLoader {

    private Ticket chunkTicket;
    private boolean initialized = false;

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void initialize(Ticket ticket) {
        initialized = true;
        chunkTicket = ticket;

        /*
		// Only do anything if this function is running on the server side
		// NOTE: We don't have to check whether this block is the upper door
		// block or the lower one because only one of them should have a
		// link associated with it.
		if (!worldObj.isRemote) {
			DimData dimension = PocketManager.createDimensionData(worldObj);

			// Check whether a ticket has already been assigned to this door
			if (chunkTicket == null) {
				// No ticket yet.
				// Check if this area should be loaded and request a new ticket.
				if (isValidChunkLoaderSetup(dimension)) {
					chunkTicket = ChunkLoaderHelper.createTicket(pos, worldObj);
				}
			} else {
				// A ticket has already been provided.
				// Check if this area should be loaded. If not, release the ticket.
				if (!isValidChunkLoaderSetup(dimension)) {
					ForgeChunkManager.releaseTicket(chunkTicket);
					chunkTicket = null;
				}
			}

			// If chunkTicket isn't null at this point, then this is a valid door setup.
			// The last step is to request force loading of the pocket's chunks.
			if (chunkTicket != null) {
				ChunkLoaderHelper.forcePocketChunks(dimension, chunkTicket);
			}
		}
         */
    }

    @Override
    public void invalidate() {
        ForgeChunkManager.releaseTicket(chunkTicket);
        super.invalidate();
    }

    @Override
    protected int getNewTeleportDestination() {
        //DimDoors.log(this.getClass(), "Trying to find suitable destination rift.");
        int otherRiftID = RiftRegistry.INSTANCE.getRandomUnpairedRiftIDAtDepth(getRiftID(), depth);
        if (otherRiftID < 0) {
            Location origLocation = RiftRegistry.INSTANCE.getRiftLocation(this.riftID);
            if (origLocation.getDimensionID() == DimDoorDimensions.getPocketDimensionType(EnumPocketType.DUNGEON).getId()) { //if this dimdoor is a pocket Dungeon
                origLocation = PocketRegistry.INSTANCE.getPocket(pocketID, pocketType).getDepthZeroLocation();
            }
            otherRiftID = PocketRegistry.INSTANCE.getEntranceDoorIDOfNewPocket(EnumPocketType.DUNGEON, getRandomlyTransFormedDepth(), origLocation);
        }

        if (otherRiftID < 0) {
            DimDoors.warn(this.getClass(), "No suitable destination rift was found. This probably means that a pocket was created without any Doors.");
        } else {
            RiftRegistry.INSTANCE.pair(getRiftID(), otherRiftID);
        }

        return otherRiftID;
    }
}
