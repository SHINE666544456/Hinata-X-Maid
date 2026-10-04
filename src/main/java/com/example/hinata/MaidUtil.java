package com.example.hinata;

import net.minecraft.network.chat.Component;

/** Shared name check for the maid outfits. Shared between the server-side and client-side mixins. */
public final class MaidUtil {
    private MaidUtil() {}

    /** True if the item's custom name is "made" or "maid" (any capitalisation). */
    public static boolean isMaidName(Component name) {
        if (name == null) return false;
        String s = name.getString().trim();
        return s.equalsIgnoreCase("made") || s.equalsIgnoreCase("maid");
    }
}
