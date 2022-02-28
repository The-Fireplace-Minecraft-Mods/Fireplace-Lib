package dev.the_fireplace.libtest.player;

import com.mojang.authlib.GameProfile;
import dev.the_fireplace.lib.api.player.injectables.GameProfileFinder;
import dev.the_fireplace.lib.api.uuid.injectables.EmptyUUID;
import dev.the_fireplace.libtest.setup.TestFailedError;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

public final class GameProfileFinderTest
{
    private static final String PLAYER_NAME = "The_Fireplace";
    private static final UUID PLAYER_UUID = UUID.fromString("0b1ec5ad-cb2a-43b7-995d-889320eb2e5b");
    private static final String INVALID_NAME_EMPTY = "";
    private static final String INVALID_NAME_LONG = "ThisIsAVeryLongUsernameThatNoPlayerCanHave";
    private final UUID emptyUuid;
    private final GameProfileFinder gameProfileFinder;

    @Inject
    public GameProfileFinderTest(GameProfileFinder gameProfileFinder, EmptyUUID emptyUUID) {
        this.gameProfileFinder = gameProfileFinder;
        this.emptyUuid = emptyUUID.get();
    }

    public void execute() {
        test_findProfile_validName_returnValidData();
        test_findProfile_validUuid_returnValidData();
        test_findProfile_emptyName_returnEmpty();
        test_findProfile_invalidName_returnEmpty();
        test_findProfile_emptyUuid_returnEmpty();
    }

    private void test_findProfile_emptyUuid_returnEmpty() {
        Optional<GameProfile> profileFromEmptyUuid = gameProfileFinder.findProfile(emptyUuid);
        if (profileFromEmptyUuid.isPresent()) {
            throw new TestFailedError("Profile received for empty uuid!");
        }
    }

    private void test_findProfile_invalidName_returnEmpty() {
        Optional<GameProfile> profileFromInvalidName = gameProfileFinder.findProfile(INVALID_NAME_LONG);
        if (profileFromInvalidName.isPresent()) {
            throw new TestFailedError("Profile received for invalid name!");
        }
    }

    private void test_findProfile_emptyName_returnEmpty() {
        Optional<GameProfile> profileFromEmptyName = gameProfileFinder.findProfile(INVALID_NAME_EMPTY);
        if (profileFromEmptyName.isPresent()) {
            throw new TestFailedError("Profile received for empty name!");
        }
    }

    private void test_findProfile_validUuid_returnValidData() {
        Optional<GameProfile> playerProfileFromId = gameProfileFinder.findProfile(PLAYER_UUID);
        if (playerProfileFromId.isEmpty()) {
            throw new TestFailedError("Profile from uuid not found.");
        }
        if (!PLAYER_NAME.equals(playerProfileFromId.get().getName())) {
            throw new TestFailedError("Name received was not expected - got " + playerProfileFromId.get().getName());
        }
    }

    private void test_findProfile_validName_returnValidData() {
        Optional<GameProfile> playerProfileFromName = gameProfileFinder.findProfile(PLAYER_NAME);
        if (playerProfileFromName.isEmpty()) {
            throw new TestFailedError("Profile from name not found.");
        }
        if (!PLAYER_UUID.equals(playerProfileFromName.get().getId())) {
            throw new TestFailedError("UUID received was not expected - got " + playerProfileFromName.get().getId());
        }
    }
}
