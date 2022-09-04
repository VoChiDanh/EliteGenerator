package me.kafein.elitegenerator.generator;

import lombok.Getter;
import lombok.Setter;
import me.kafein.elitegenerator.generator.feature.auto.autoChest.AutoChest;
import me.kafein.elitegenerator.generator.feature.boost.Boost;
import me.kafein.elitegenerator.generator.feature.permission.MemberPermission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

@Getter
@Setter
public class Generator {

    final private Location islandLocation;
    final private UUID generatorUUID;
    final private String generatorName;
    final private Location generatorLocation;
    private Map<UUID, GeneratorMember> generatorMembers = new HashMap<>();
    private UUID ownerUUID;
    private Object hologram;
    private boolean hologramEnabled;
    private Boost boost;
    private boolean autoBreakBuyed, autoBreakEnabled;
    private boolean autoPickupBuyed, autoPickupEnabled;
    private boolean autoSmeltBuyed, autoSmeltEnabled;
    private boolean autoChestBuyed;
    private AutoChest autoChest;
    private String createDate;
    private int level;

    public Generator(final Location islandLocation, final UUID generatorUUID, final String generatorName, final Location generatorLocation, final int level) {
        this.islandLocation = islandLocation;
        this.generatorUUID = generatorUUID;
        this.generatorName = generatorName;
        this.generatorLocation = generatorLocation;
        this.level = level;
    }

    @Nullable
    public GeneratorMember getGeneratorMember(final UUID memberUUID) {
        return generatorMembers.get(memberUUID);
    }

    public void addGeneratorMember(final GeneratorMember generatorMember) {
        generatorMembers.put(generatorMember.getMemberUUID(), generatorMember);
    }

    public void removeGeneratorMember(final UUID memberUUID) {
        generatorMembers.remove(memberUUID);
    }

    public boolean containsGeneratorMember(final UUID memberUUID) {
        return generatorMembers.containsKey(memberUUID);
    }

    public boolean containsMemberPermission(final UUID memberUUID, final MemberPermission memberPermission) {
        if (!containsGeneratorMember(memberUUID)) return false;
        if (memberUUID.equals(ownerUUID)) return true;
        return getGeneratorMember(memberUUID).containsPermission(memberPermission);
    }

    public void changeOwnerUUID(final UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public boolean hasOnlineMember() {
        for (UUID uuid : generatorMembers.keySet()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) return true;
        }
        return false;
    }

    public boolean hasOnlineMember(final UUID memberUUID) {
        for (UUID uuid : generatorMembers.keySet()) {
            if (uuid == memberUUID) continue;
            final Player player = Bukkit.getPlayer(uuid);
            if (player != null) return true;
        }
        return false;
    }

    public List<UUID> getGeneratorMembers() {
        return new ArrayList<>(generatorMembers.keySet());
    }

    public void setGeneratorMembers(Map<UUID, GeneratorMember> generatorMembers) {
        this.generatorMembers = generatorMembers;
    }

    public Map<UUID, GeneratorMember> getGeneratorMembersMap() {
        return generatorMembers;
    }

    public int getGeneratorMemberSize() {
        return generatorMembers.size();
    }

    public void clearBoost() {
        boost = null;
    }

    public boolean hasBoost() {
        return boost != null;
    }

    public Object getHologram() {
        return hologram;
    }

    public void setHologram(Object hologram) {
        this.hologram = hologram;
    }

    public boolean hasHologram() {
        return hologram != null;
    }

    public boolean isAutoChestEnabled() {
        return autoChest != null;
    }

    public void clearAutoChest() {
        this.autoChest = null;
    }

    @Nullable
    public AutoChest getAutoChest() {
        return autoChest;
    }

    public void setAutoChest(AutoChest autoChest) {
        this.autoChest = autoChest;
    }

    public void addLevel(int var) {
        level += var;
    }

}
