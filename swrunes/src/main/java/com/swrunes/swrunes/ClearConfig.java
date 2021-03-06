package com.swrunes.swrunes;

public class ClearConfig {
    public static void main(String[] args) {
        //System.out.println("Clear config file");
        ConfigInfo cf = ConfigInfo.getInstance();
        cf.favourite.clear();
        cf.loadPetLevel = 1;
        cf.globalLocks = "";
        cf.upPet40 = true;
        cf.useBigUI = false;

        for (ConfigInfo.PetSetting p1 : cf.petMaps.values()) {
            p1.isSaved = false;
            p1.runeFilterValue = 5;
        }
        cf.saveFileOther();
        //System.out.println("Done !");
    }
}
