package io.github.flemmli97.flan.platform.integration.webmap;

import io.github.flemmli97.flan.claim.Claim;

public class WebmapCalls {

    public static boolean dynmapLoaded;
    public static boolean bluemapLoaded;
    public static boolean squaremapLoaded;

    public static void addClaimMarker(Claim claim) {
        if (dynmapLoaded)
            DynmapIntegration.addClaimMarker(claim);
        if (bluemapLoaded)
            BluemapIntegration.addClaimMarker(claim);
        if (squaremapLoaded)
            SquaremapIntegration.addClaimMarker(claim);

    }

    public static void removeMarker(Claim claim) {
        if (dynmapLoaded)
            DynmapIntegration.removeMarker(claim);
        if (bluemapLoaded)
            BluemapIntegration.removeMarker(claim);
        if (squaremapLoaded)
            SquaremapIntegration.removeMarker(claim);
    }

    public static void changeClaimName(Claim claim) {
        if (dynmapLoaded)
            DynmapIntegration.changeClaimName(claim);
        if (bluemapLoaded)
            BluemapIntegration.changeClaimName(claim);
        if (squaremapLoaded)
            SquaremapIntegration.changeClaimName(claim);
    }

    public static void changeClaimOwner(Claim claim) {
        if (dynmapLoaded)
            DynmapIntegration.changeClaimOwner(claim);
        if (bluemapLoaded)
            BluemapIntegration.changeClaimOwner(claim);
        if (squaremapLoaded)
            SquaremapIntegration.changeClaimOwner(claim);
    }

    public static void onExtendDownwards(Claim claim) {
        if (dynmapLoaded) {
            DynmapIntegration.removeMarker(claim);
            DynmapIntegration.addClaimMarker(claim);
        }
        if (squaremapLoaded){
            SquaremapIntegration.removeMarker(claim);
            SquaremapIntegration.addClaimMarker(claim);
        }
        if (bluemapLoaded)
            BluemapIntegration.addClaimMarker(claim);
    }
}
