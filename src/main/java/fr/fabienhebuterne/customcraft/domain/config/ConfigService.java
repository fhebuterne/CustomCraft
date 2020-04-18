package fr.fabienhebuterne.customcraft.domain.config;

import fr.fabienhebuterne.customcraft.CustomCraft;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.File;
import java.io.IOException;

public class ConfigService<T extends ConfigurationSerializable> {

    private File configFile;
    private FileConfiguration configFileConfiguration;
    private String fileName;
    private CustomCraft instance;
    private String pathSerialization;
    private Class<T> clazzSerialization;

    public ConfigService(CustomCraft instance,
                         String fileName,
                         String pathSerialization,
                         Class<T> clazzSerialization) {
        this.instance = instance;
        this.fileName = fileName;
        this.pathSerialization = pathSerialization;
        this.clazzSerialization = clazzSerialization;
    }

    public void createOrLoadConfig(boolean copyFromRessource) throws IOException {
        configFile = new File(this.instance.getDataFolder(), this.fileName + ".yml");

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            if (copyFromRessource) {
                this.instance.saveResource(fileName + ".yml", false);
            } else {
                configFile.createNewFile();
            }
        }

        configFileConfiguration = new YamlConfiguration();
        try {
            configFileConfiguration.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getFileConfig() {
        return this.configFileConfiguration;
    }

    public T getSerializable() {
        T serializable = configFileConfiguration.getSerializable(this.pathSerialization, this.clazzSerialization);

        if (serializable == null) {
            try {
                return clazzSerialization.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return serializable;
    }

    public void save(Object object) {
        try {
            configFileConfiguration.set(this.pathSerialization, object);
            configFileConfiguration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
