package io.github.flemmli97.flan.platform.integration.webmap;

import com.mojang.authlib.GameProfile;
import io.github.flemmli97.flan.claim.Claim;
import io.github.flemmli97.flan.claim.ClaimStorage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;
import xyz.jpenilla.squaremap.api.marker.Rectangle;
import xyz.jpenilla.squaremap.api.Point;

import java.awt.*;
import java.util.*;


public class SquaremapIntegration {


    private static final HashMap<String, SimpleLayerProvider> markerLayers = new HashMap<>();
    private static final String markerID = "flan-claims", markerLabel = "Flan Claims";

    public static void reg(MinecraftServer server) {
        Squaremap api = SquaremapProvider.get();

        for (MapWorld level : api.mapWorlds()) {
            Key tempId = Key.of(markerID + "-" + level.identifier().namespace() + "-" + level.identifier().value());

            if (!level.layerRegistry().hasEntry(tempId)) {
                SimpleLayerProvider layer = SimpleLayerProvider.builder(markerLabel)
                        .defaultHidden(false)
                        .zIndex(9)
                        .layerPriority(9)
                        .showControls(true)
                        .build();
                level.layerRegistry().register(tempId, layer);
                markerLayers.put(level.identifier().asString(), layer);
            }
        }
        new Thread(() -> {
            for (ServerLevel level : server.getAllLevels()) {
                for (Map.Entry<UUID, Set<Claim>> x : ClaimStorage.get(level).getClaims().entrySet()) {
                    for (Claim claim : x.getValue()) {
                        addClaimMarker(claim);
                    }
                }
            }
        }).start();
        WebmapCalls.squaremapLoaded = true;
    }

    public static void addClaimMarker(Claim claim) {
        if (markerLayers.isEmpty())
            return;

        int[] dim = claim.getDimensions();
        Rectangle marker = Marker.rectangle(Point.of(dim[0], dim[2]), Point.of(dim[1], dim[3]));

        marker.markerOptions(
                MarkerOptions.builder()
                        .hoverTooltip(claimLabel(claim))
                        .strokeColor(new Color(lineColor(claim.isAdminClaim())))
                        .strokeOpacity(0.8F)
                        .strokeWeight(3)
                        .fillColor(new Color(fillColor(claim.isAdminClaim())))
                        .fillOpacity(0.2F)
                        .build()
        );

        markerLayers.get(getWorldKey(claim.getWorld())).addMarker(Key.of(claim.getClaimID().toString().toLowerCase()), marker);
    }

    public static void removeMarker(Claim claim) {
        if (markerLayers.isEmpty())
            return;
        markerLayers.get(getWorldKey(claim.getWorld())).removeMarker(Key.of(claim.getClaimID().toString().toLowerCase()));
    }

    public static void changeClaimName(Claim claim) {
        if (markerLayers.isEmpty())
            return;
        removeMarker(claim);
        addClaimMarker(claim);
    }

    public static void changeClaimOwner(Claim claim) {
        if (markerLayers.isEmpty())
            return;
        removeMarker(claim);
        addClaimMarker(claim);
    }

    private static String getWorldKey(Level level) {
        return level.dimension().location().toString();
    }

    private static int lineColor(boolean admin) {
        return admin ? 0xb50909 : 0xffa200;
    }

    private static int fillColor(boolean admin) {
        return admin ? 0xff0000 : 0xe0e01d;
    }

    private static String claimLabel(Claim claim) {
        String name = claim.getClaimName();
        if (claim.isAdminClaim()) {
            if (name == null || name.isEmpty()) {
                return "Admin Claim";
            } else {
                return name + " - " + "Admin Claim";
            }
        }
        Optional<GameProfile> prof = claim.getWorld().getServer().getProfileCache().get(claim.getOwner());
        if (name == null || name.isEmpty()) {
            return prof.map(GameProfile::getName).orElse("UNKNOWN") + "'s Claim";
        } else {
            return name + " - " + prof.map(GameProfile::getName).orElse("UNKNOWN") + "'s Claim";
        }
    }
}
