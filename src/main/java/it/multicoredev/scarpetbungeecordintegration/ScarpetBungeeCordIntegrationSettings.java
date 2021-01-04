package it.multicoredev.scarpetbungeecordintegration;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.*;

public class ScarpetBungeeCordIntegrationSettings {
    public static final String REDCRAFT = "redcraft";

    @Rule(
            desc = "Every 30 seconds sends Player Position.",
            appSource = "send_player_position",
            category = {SURVIVAL, FEATURE, REDCRAFT}
    )
    public static boolean sendPlayerPosition = false;

}
