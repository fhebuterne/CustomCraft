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

    public void createOrLoadConfig() {
        configFile = new File(this.instance.getDataFolder(), this.fileName);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            this.instance.saveResource(fileName + ".yml", false);
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
        return configFileConfiguration.getSerializable(this.pathSerialization, this.clazzSerialization);
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
