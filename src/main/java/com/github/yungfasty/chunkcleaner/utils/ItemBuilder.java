package com.github.yungfasty.chunkcleaner.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder {

    @Getter @Setter private ItemStack itemStack;
    @Getter @Setter private ItemMeta itemMeta;

    public static ItemBuilder of(ConfigurationSection configurationSection) {

        ItemBuilder itemBuilder = new ItemBuilder();

        if (!configurationSection.getString("url").isEmpty()) {

            itemBuilder.type(Material.SKULL_ITEM);
            itemBuilder.durability((short) 3);
            itemBuilder.texture(configurationSection.getString("url"));

        } else {

            itemBuilder.type(Material.valueOf(configurationSection.getString("type")));
            itemBuilder.durability((short) configurationSection.getInt("data"));

        }

        itemBuilder.amount(1);
        itemBuilder.glow(configurationSection.getBoolean("glow"));
        itemBuilder.lore(configurationSection.getStringList("lore").stream().map(l -> l.replaceAll("&", "§")).collect(Collectors.toList()));
        itemBuilder.name(configurationSection.getString("name").replaceAll("&", "§"));

        return itemBuilder;
    }

    public static ItemBuilder of(Material material) {

        ItemStack itemStack1 = new ItemStack(material);
        ItemBuilder itemBuilder = new ItemBuilder();

        itemBuilder.setItemStack(itemStack1);
        itemBuilder.setItemMeta(itemStack1.getItemMeta());

        return itemBuilder;

    }

    public ItemBuilder texture(String url) {

        if (!url.startsWith("http://textures.minecraft.net/texture/"))
            url = "http://textures.minecraft.net/texture/" + url;

        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(url.getBytes()), null);

        profile.getProperties().put("textures", new Property(
                "textures", new String(Base64.encodeBase64(String.format(
                "{textures:{SKIN:{url:\"%s\"}}}", url).getBytes()))));

        try { FieldUtils.writeField(skullMeta, "profile", profile, true); }
        catch (Exception exception) { exception.printStackTrace(); }

        this.itemMeta = skullMeta;
        this.itemStack.setDurability((short) 3);

        return this;

    }

    public ItemBuilder item(ItemStack itemStack) {

        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();

        return this;

    }

    public ItemBuilder name(String string) {

        this.itemMeta.setDisplayName(string);

        return this;

    }

    public ItemBuilder lore(String... lore) {

        this.itemMeta.setLore(Arrays.asList(lore));

        return this;

    }

    public ItemBuilder addLore(String... lore) {

        List<String> lore2 = this.itemMeta.getLore();

        lore2.addAll(Arrays.asList(lore));

        this.itemMeta.setLore(lore2);

        return this;

    }

    public ItemBuilder lore(List<String> lore) {

        this.itemMeta.setLore(lore);

        return this;

    }

    public ItemBuilder amount(int amount) {

        this.itemStack.setAmount(amount);

        return this;

    }

    public ItemBuilder owner(String owner) {
        SkullMeta skullMeta = (SkullMeta) this.itemMeta;
        skullMeta.setOwner(owner);

        this.itemMeta = skullMeta;
        return this;
    }

    public ItemBuilder durability(short amount) {

        this.itemStack.setDurability(amount);
        this.itemMeta = this.itemStack.getItemMeta();

        return this;

    }

    public ItemBuilder type(Material material) {

        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();

        return this;

    }

    public ItemStack wrap() {

        this.itemStack.setItemMeta(this.itemMeta);

        return this.itemStack;

    }

    public ItemBuilder glow(boolean mustGlows) {

        if (mustGlows) {

            itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        }

        return this;

    }

    public ItemBuilder flag(ItemFlag... flags) {

        itemMeta.addItemFlags(flags);

        return this;

    }

}
