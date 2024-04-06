package com.myzuji.sadk.system;

public final class CompatibleAlgorithm {
    private static boolean compatibleSM2WithoutZ;

    private CompatibleAlgorithm() {
    }

    public static boolean isCompatibleSM2WithoutZ() {
        return compatibleSM2WithoutZ;
    }

    public static void setCompatibleSM2WithoutZ(boolean compatibleSM2WithoutZ) {
        CompatibleAlgorithm.compatibleSM2WithoutZ = compatibleSM2WithoutZ;
    }

    static {
        compatibleSM2WithoutZ = CompatibleConfig.SM2VerifiedWithoutZCompatible;
    }
}
