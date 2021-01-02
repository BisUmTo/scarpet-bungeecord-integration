package it.multicoredev.scarpetbungeecordintegration;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import carpet.script.CarpetScriptServer;
import carpet.script.bundled.BundledModule;
import it.multicoredev.scarpetbungeecordintegration.functions.SendPacket;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class ScarpetBungeeCordIntegrationExtension implements CarpetExtension {
    public static final String MOD_ID = "scarpet-bungeecord-integration";
    public static final String MOD_NAME = "Scarpet BungeeCord Integration";
    public static final String MOD_VERSION = "1.4.21";

    static {
        CarpetServer.manageExtension(new ScarpetBungeeCordIntegrationExtension());
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(ScarpetBungeeCordIntegrationSettings.class);
        CarpetScriptServer.registerSettingsApp(defaultScript("send_player_position", false));
    }

    @Override
    public void scarpetApi(CarpetExpression expression) {
        SendPacket.apply(expression.getExpr());
    }

    @Override
    public String version() {
        return MOD_ID;
    }

    public static BundledModule defaultScript(String scriptName, boolean isLibrary) {
        BundledModule module = new BundledModule(scriptName.toLowerCase(Locale.ROOT), null, false);
        try {
            module = new BundledModule(scriptName.toLowerCase(Locale.ROOT),
                    IOUtils.toString(
                            BundledModule.class.getClassLoader().getResourceAsStream("assets/" + MOD_ID + "/scripts/" + scriptName + (isLibrary ? ".scl" : ".sc")),
                            StandardCharsets.UTF_8
                    ), isLibrary);
        } catch (NullPointerException | IOException ignored) {
        }
        return module;
    }

    public static void noop() {
    }
}