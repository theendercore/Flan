package io.github.flemmli97.flan.platform.integration.webmap;

import com.mojang.authlib.GameProfile;
import io.github.flemmli97.flan.claim.Claim;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;
import xyz.jpenilla.squaremap.api.marker.Rectangle;
import xyz.jpenilla.squaremap.api.Point;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;


public class SquaremapIntegration {

    static Logger LOGGER = LoggerFactory.getLogger(SquaremapIntegration.class.getSimpleName());

    private static final HashMap<String, SimpleLayerProvider> markerLayers = new HashMap<>();
    private static final String markerID = "flan-claims", markerLabel = "Flan Claims";

    public static void reg() {
        LOGGER.info("register Square");
        Squaremap api = SquaremapProvider.get();

        for (MapWorld level : api.mapWorlds()) {

            // squaremap=xyz.jpenilla:squaremap-api:1.2.3

            Key tempId = Key.of(markerID + "-" + level.identifier().asString().replace(":", "-"));

            LOGGER.info("level - {}", level.identifier().asString());
            LOGGER.info("has entry - {}", level.layerRegistry().hasEntry(tempId));
            level.layerRegistry().entries().forEach(p -> LOGGER.info("registry - {} : {}", p.right().getLabel(), p.left()));
            if (!level.layerRegistry().hasEntry(tempId)) {
                SimpleLayerProvider layer = SimpleLayerProvider.builder(markerLabel)
                        .defaultHidden(false)
                        .zIndex(9)
                        .layerPriority(9)
                        .showControls(true)
                        .build();
                LOGGER.info("marker - {}", tempId);
                level.layerRegistry().register(tempId, layer);
                markerLayers.put(level.identifier().asString(), layer);
            }
        }
        WebmapCalls.squaremapLoaded = true;
    }

    public static void addClaimMarker(Claim claim) {
        LOGGER.info("add claim");
        if (markerLayers.isEmpty())
            return;

        double[] dim = Arrays.stream(claim.getDimensions()).mapToDouble((a) -> (double) a).toArray();
        Rectangle marker = Marker.rectangle(Point.of(dim[0], dim[1]), Point.of(dim[2], dim[3]));

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
