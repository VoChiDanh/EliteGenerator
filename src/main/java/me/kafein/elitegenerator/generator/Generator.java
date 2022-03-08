package me.kafein.elitegenerator.generator;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.kafein.elitegenerator.generator.feature.auto.autoChest.AutoChest;
import me.kafein.elitegenerator.generator.feature.boost.Boost;
import me.kafein.elitegenerator.generator.feature.permission.MemberPermission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class Generator {

    private Map<UUID, GeneratorMember> generatorMembers = new HashMap<>();
    private UUID ownerUUID;

    private Hologram hologram;

    private Boost boost;

    private boolean autoBreakBuyed;
    private boolean autoPickupBuyed;
    private boolean autoSmeltBuyed;
    private boolean autoChestBuyed;

    private boolean autoBreakEnabled;
    private boolean autoPickupEnabled;
    private boolean autoSmeltEnabled;
    private AutoChest autoChest;

    private String createDate;

    final private Location islandLocation;
    final private UUID generatorUUID;
    final private String generatorName;
    final private Location generatorLocation;
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

    public Map<UUID, GeneratorMember> getGeneratorMembersMap() {
        return generatorMembers;
    }

    public void setGeneratorMembers(Map<UUID, GeneratorMember> generatorMembers) {
        this.generatorMembers = generatorMembers;
    }

    public int getGeneratorMemberSize() {
        return generatorMembers.size();
    }

    @Nullable
    public Player getOwner() {
        return Bukkit.getPlayer(ownerUUID);
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    @Nullable
    public Boost getBoost() {
        return boost;
    }

    public void setBoost(Boost boost) {
        this.boost = boost;
    }

    public void clearBoost() {
        boost = null;
    }

    public boolean hasBoost() {
        return boost != null;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public boolean hasHologram() {
        return hologram != null;
    }

    public void clearHologram() {
        hologram.delete();
        hologram = null;
    }

    public boolean isAutoBreakBuyed() {
        return autoBreakBuyed;
    }

    public boolean isAutoPickupBuyed() {
        return autoPickupBuyed;
    }

    public boolean isAutoSmeltBuyed() {
        return autoSmeltBuyed;
    }

    public boolean isAutoChestBuyed() {
        return autoChestBuyed;
    }

    public void setAutoBreakBuyed(boolean autoBreakBuyed) {
        this.autoBreakBuyed = autoBreakBuyed;
    }

    public void setAutoPickupBuyed(boolean autoPickupBuyed) {
        this.autoPickupBuyed = autoPickupBuyed;
    }

    public void setAutoSmeltBuyed(boolean autoSmeltBuyed) {
        this.autoSmeltBuyed = autoSmeltBuyed;
    }

    public void setAutoChestBuyed(boolean autoChestBuyed) {
        this.autoChestBuyed = autoChestBuyed;
    }

    public boolean isAutoBreakEnabled() {
        return autoBreakEnabled;
    }

    public boolean isAutoPickupEnabled() {
        return autoPickupEnabled;
    }

    public boolean isAutoSmeltEnabled() {
        return autoSmeltEnabled;
    }

    public boolean isAutoChestEnabled() {
        return autoChest != null;
    }

    public void setAutoBreakEnabled(boolean autoBreakEnabled) {
        this.autoBreakEnabled = autoBreakEnabled;
    }

    public void setAutoPickupEnabled(boolean autoPickupEnabled) {
        this.autoPickupEnabled = autoPickupEnabled;
    }

    public void setAutoSmeltEnabled(boolean autoSmeltEnabled) {
        this.autoSmeltEnabled = autoSmeltEnabled;
    }

    public void setAutoChest(AutoChest autoChest) {
        this.autoChest = autoChest;
    }

    public void clearAutoChest() {
        this.autoChest = null;
    }

    @Nullable
    public AutoChest getAutoChest() {
        return autoChest;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevel(int var) {
        level += var;
    }

    public Location getGeneratorLocation() {
        return generatorLocation;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public UUID getGeneratorUUID() {
        return generatorUUID;
    }

    public Location getIslandLocation() {
        return islandLocation;
    }

}
