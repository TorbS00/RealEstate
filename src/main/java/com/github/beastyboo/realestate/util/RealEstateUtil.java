package com.github.beastyboo.realestate.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.List;

/**
 * Created by Torbie on 29.11.2020.
 */
public enum RealEstateUtil {

    INSTANCE;

    public ItemStack itemFactory(Material material, String  name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Saves a file with the json string.
     */
    public void saveFile(File file, String json) {
        final FileWriter fw;
        try {
            file.createNewFile();
            fw = new FileWriter(file);
            fw.write(json);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);

        }
    }

    /**
     * Loads the json string from a file.
     */
    public String loadContent(File file) {
        if(file.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                final  StringBuilder text = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    text.append(line);
                }
                reader.close();
                return text.toString();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        return "";
    }

}
