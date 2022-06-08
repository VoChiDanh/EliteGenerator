package me.kafein.elitegenerator.storage;

import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.user.User;

import javax.annotation.Nullable;
import java.util.UUID;

public interface Storage {

    User loadUser(final UUID userUUID);

    void saveUser(final User user);

    void putGeneratorToUser(UUID userUUID, UUID generatorUUID);

    @Nullable
    Generator loadGenerator(final UUID generatorUUID);

    void saveGenerator(final Generator generator);

    void deleteGenerator(final UUID generatorUUID);

}
