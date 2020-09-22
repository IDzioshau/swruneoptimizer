package by.dobrush.swruneoptimizer.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StatType {
    @JsonProperty("HP")
    HP {
        private int[] mainStatValue = {804, 1092, 1380, 1704, 2088, 2448};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }
    },
    @JsonProperty("HP_PERCENT")
    HP_PERCENT {
        private int[] mainStatValue = {18, 19, 38, 43, 51, 63};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }

        @Override
        public String toString() {
            return "HP%";
        }
    },
    @JsonProperty("ATK")
    ATK {
        private int[] mainStatValue = {54, 73, 92, 112, 135, 160};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }
    },
    @JsonProperty("ATK_PERCENT")
    ATK_PERCENT {
        private int[] mainStatValue = {18, 19, 38, 43, 51, 63};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }

        @Override
        public String toString() {
            return "ATK%";
        }
    },
    @JsonProperty("DEF")
    DEF {
        private int[] mainStatValue = {54, 73, 92, 112, 135, 160};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }
    },
    @JsonProperty("DEF_PERCENT")
    DEF_PERCENT {
        private int[] mainStatValue = {18, 19, 38, 43, 51, 63};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }

        @Override
        public String toString() {
            return "DEF%";
        }
    },
    @JsonProperty("SPD_PERCENT")
    SPD_PERCENT {
        public int getMaxMainStatValue(int stars){
            return 0;
        }
    },
    @JsonProperty("SPD")
    SPD {
        private int[] mainStatValue = {18, 19, 25, 30, 39, 42};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }
    },
    @JsonProperty("CRI_RATE")
    CR {
        private int[] mainStatValue = {18, 19, 37, 42, 47, 58};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }
    },
    @JsonProperty("CRI_DAMAGE")
    CD {
        private int[] mainStatValue = {19, 37, 43, 57, 65, 80};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }
    },
    @JsonProperty("RES")
    RES {
        private int[] mainStatValue = {18, 19, 38, 44, 51, 64};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }
    },
    @JsonProperty("ACC")
    ACC {
        private int[] mainStatValue = {18, 19, 38, 44, 51, 64};

        public int getMaxMainStatValue(int stars){
            return mainStatValue[stars];
        }
    };

    abstract int getMaxMainStatValue(int stars);
}
