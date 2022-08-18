package fr.fabienhebuterne.customcraft.domain.config;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.json.ItemStackAdapter;
import fr.fabienhebuterne.customcraft.json.ItemStackMapKeyAdapter;
import fr.fabienhebuterne.customcraft.json.ItemStackMapValueAdapter;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.HashMap;

public class ConfigService<T> {

    private File configFile;
    private String fileName;
    private CustomCraft instance;
    private Class<T> clazzSerialization;
    private T entity;
    private Gson gson;

    public ConfigService(CustomCraft instance,
                         String fileName,
                         Class<T> clazzSerialization) {
        this.instance = instance;
        this.fileName = fileName;
        this.clazzSerialization = clazzSerialization;
        this.gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(ItemStack.class, new ItemStackAdapter(instance.itemStackSerializer))
                .registerTypeAdapter(HashMap.class, new ItemStackMapValueAdapter(instance.itemStackSerializer))
                .registerTypeAdapter(HashMap.class, new ItemStackMapKeyAdapter(instance.itemStackSerializer))
                .setPrettyPrinting()
                .create();
    }

    public void createOrLoadConfig(boolean copyFromRessource) throws IOException {
        configFile = new File(this.instance.getDataFolder(), this.fileName + ".json");

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            if (copyFromRessource) {
                this.instance.saveResource(fileName + ".json", false);
            } else {
                configFile.createNewFile();
            }
        }

        this.loadConfig();
    }

    public void loadConfig() {
        try {
            InputStreamReader fileReader = new InputStreamReader(new FileInputStream(configFile), Charsets.UTF_8);
            this.entity = gson.fromJson(fileReader, clazzSerialization);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T getSerializable() {
        if (this.entity == null) {
            try {
                return clazzSerialization.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return this.entity;
    }

    public void save(T object) {
        this.entity = object;
        try {
            FileWriter fileWriter = new FileWriter(configFile);
            gson.toJson(object, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
