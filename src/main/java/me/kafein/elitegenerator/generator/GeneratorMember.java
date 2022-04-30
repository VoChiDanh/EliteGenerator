package me.kafein.elitegenerator.generator;

import me.kafein.elitegenerator.generator.feature.permission.MemberPermission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GeneratorMember {

    final private List<MemberPermission> memberPermissions = new ArrayList<>();

    final private UUID memberUUID;

    public GeneratorMember(final UUID memberUUID) {
        this.memberUUID = memberUUID;
        for (MemberPermission memberPermission : MemberPermission.values()) {
            if (memberPermission.getValue()) memberPermissions.add(memberPermission);
        }
    }

    public boolean containsPermission(final MemberPermission memberPermission) {
        return memberPermissions.contains(memberPermission);
    }

    public void addPermission(final MemberPermission memberPermission) {
        memberPermissions.add(memberPermission);
    }

    public void removePermission(final MemberPermission memberPermission) {
        memberPermissions.remove(memberPermission);
    }

    public boolean isOnline() {
        return Bukkit.getPlayer(memberUUID) != null;
    }

    @Nullable
    public Player getMember() {
        return Bukkit.getPlayer(memberUUID);
    }

    public UUID getMemberUUID() {
        return memberUUID;
    }

}
