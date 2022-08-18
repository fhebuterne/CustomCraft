package fr.fabienhebuterne.customcraft.commands;

import fr.fabienhebuterne.customcraft.CustomCraft;
import fr.fabienhebuterne.customcraft.domain.config.ConfigService;
import fr.fabienhebuterne.customcraft.domain.config.CustomCraftConfig;
import fr.fabienhebuterne.customcraft.exceptions.BadArgumentsException;
import fr.fabienhebuterne.customcraft.exceptions.CustomCraftAlreadyExistException;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandCreateTest {

    private Server serverMock;

    @BeforeEach
    public void setupBukkit() {
        MockitoAnnotations.initMocks(this);
        serverMock = Mockito.mock(Server.class);
        Mockito.when(serverMock.getLogger()).thenReturn(Logger.getGlobal());
        Bukkit.setServer(serverMock);
    }

    @Test
    public void should_open_inventory_for_a_player() throws BadArgumentsException, CustomCraftAlreadyExistException {
        // Given
        Player playerMock = Mockito.mock(Player.class);
        Command commandMock = Mockito.mock(Command.class);
        CustomCraft customCraft = Mockito.mock(CustomCraft.class);
        ConfigService<CustomCraftConfig> configService = Mockito.mock(ConfigService.class);

        // Mock ItemMeta
        ItemFactory itemFactory = Mockito.mock(ItemFactory.class);
        BDDMockito.given(Bukkit.getItemFactory()).willReturn(itemFactory);
        BDDMockito.given(itemFactory.getItemMeta(ArgumentMatchers.any())).willReturn(Mockito.mock(ItemMeta.class));

        CommandCreate commandCreate = new CommandCreate();
        commandCreate.setInstance(customCraft);

        Inventory inventoryMock = Mockito.mock(Inventory.class);
        BDDMockito.given(Bukkit.createInventory(playerMock, 9, "CustomCraft - Recipe type")).willReturn(inventoryMock);

        BDDMockito.given(customCraft.getCustomCraftConfig()).willReturn(configService);
        BDDMockito.given(configService.getSerializable()).willReturn(new CustomCraftConfig());

        // When
        commandCreate.runFromPlayer(serverMock, playerMock, "create", commandMock, new String[]{"create", "craftNameArgOne"});

        // Then
        Mockito.verify(inventoryMock, Mockito.times(9)).setItem(ArgumentMatchers.anyInt(), ArgumentMatchers.any());
        Mockito.verify(playerMock).openInventory(inventoryMock);
    }

}