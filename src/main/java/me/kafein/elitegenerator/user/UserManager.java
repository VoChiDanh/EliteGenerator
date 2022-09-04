package me.kafein.elitegenerator.user;

import me.kafein.elitegenerator.EliteGenerator;
import me.kafein.elitegenerator.generator.Generator;
import me.kafein.elitegenerator.generator.GeneratorManager;
import me.kafein.elitegenerator.storage.Storage;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserManager {

    final private GeneratorManager generatorManager = EliteGenerator.getInstance().getGeneratorManager();
    final private Storage storage = EliteGenerator.getInstance().getStorageManager().get();

    final private Map<UUID, User> users = new HashMap<>();

    public CompletableFuture<Boolean> saveUsers() {

        return CompletableFuture.supplyAsync(() -> {

            if (users.isEmpty()) return false;

            users.keySet().forEach(userUUID -> storage.saveUser(users.get(userUUID)));

            return true;

        });


    }

    public boolean loadUser(final UUID userUUID) {

        final User user = storage.loadUser(userUUID);

        if (user.hasGenerator()) {

            for (UUID uuid : user.getGenerators()) {

                if (generatorManager.containsGeneratorUUID(uuid)) break;

                final Generator generator = storage.loadGenerator(uuid);

                if (generator == null) {
                    user.removeGenerator(uuid);
                    continue;
                }

                generatorManager.loadGenerator(generator);

            }

        }

        users.put(userUUID, user);

        return true;

    }

    public void saveUser(final UUID userUUID) {

        final User user = users.get(userUUID);
        if (user == null) return;

        if (user.hasGenerator()) {

            final Generator generator = generatorManager.getGenerator(user.getGenerator(0));

            if (generator != null && !generator.hasOnlineMember(userUUID)) {

                for (UUID uuid : user.getGenerators()) {

                    generatorManager.saveGenerator(uuid);

                }

            }

        }

        storage.saveUser(user);

        users.remove(userUUID);

    }

    public void saveUser(final User user) {

        final UUID userUUID = user.getUserUUID();

        if (user.hasGenerator()) {

            final Generator generator = generatorManager.getGenerator(user.getGenerator(0));

            if (generator != null && !generator.hasOnlineMember(userUUID)) {

                for (UUID uuid : user.getGenerators()) {

                    generatorManager.saveGenerator(uuid);

                }

            }

        }

        storage.saveUser(user);

        users.remove(userUUID);

    }

    @Nullable
    public User getUser(final UUID userUUID) {
        return users.get(userUUID);
    }

    public User getUserFromStorage(final UUID userUUID) {
        return storage.loadUser(userUUID);
    }

    public void addUser(final User user) {
        if (users.containsKey(user.getUserUUID())) return;
        users.put(user.getUserUUID(), user);
    }

    public void removeUser(final UUID userUUID) {
        users.remove(userUUID);
    }

    public boolean containsUser(final UUID userUUID) {
        return users.containsKey(userUUID);
    }

    public Map<UUID, User> getUsers() {
        return users;
    }
}
