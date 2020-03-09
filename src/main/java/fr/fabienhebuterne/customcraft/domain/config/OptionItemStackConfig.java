package fr.fabienhebuterne.customcraft.domain.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class OptionItemStackConfig implements ConfigurationSerializable {

    private boolean blockCanBePlaced = true;

    public boolean isBlockCanBePlaced() {
        return blockCanBePlaced;
    }

    public void setBlockCanBePlaced(boolean blockCanBePlaced) {
        this.blockCanBePlaced = blockCanBePlaced;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("blockCanBePlaced", blockCanBePlaced);
        return map;
    }

    public static OptionItemStackConfig deserialize(Map<String, Object> map) {
        OptionItemStackConfig optionItemStackConfig = new OptionItemStackConfig();
        optionItemStackConfig.blockCanBePlaced = (boolean) map.get("blockCanBePlaced");
        return optionItemStackConfig;
    }

    @Override
    public String toString() {
        return "OptionItemStackConfig{" +
                "blockCanBePlaced=" + blockCanBePlaced +
                '}';
    }
}
