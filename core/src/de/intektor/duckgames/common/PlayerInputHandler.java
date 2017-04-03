package de.intektor.duckgames.common;

import de.intektor.duckgames.entity.entities.EntityPlayer;

/**
 * @author Intektor
 */
public class PlayerInputHandler {

    public static void handlePadControl(EntityPlayer player, float movementAngle, float movementStrength, float aimingAngle, float aimingStrength) {
        movementStrength = Math.abs(Math.min(1, movementStrength));
        aimingStrength = Math.abs(Math.min(1, aimingStrength));
        boolean shouldJump = movementAngle > Math.toRadians(30) && movementAngle < Math.toRadians(150);
        boolean shouldCrouch = movementAngle < Math.toRadians(-30) && movementAngle > Math.toRadians(-150);

        player.motionX = (float) (Math.cos(movementAngle) * movementStrength);
        player.isJumping = shouldJump;

        player.setAim(aimingAngle, aimingStrength);

    }
}
