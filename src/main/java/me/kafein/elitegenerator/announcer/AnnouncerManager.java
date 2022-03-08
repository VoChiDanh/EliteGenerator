package me.kafein.elitegenerator.announcer;

import me.kafein.elitegenerator.announcer.impl.actionbar.ActionBarAnnounce;
import me.kafein.elitegenerator.announcer.impl.title.TitleAnnounce;

public class AnnouncerManager {

    final private static TitleAnnounce titleAnnounce = new TitleAnnounce();
    final private static ActionBarAnnounce actionBarAnnounce = new ActionBarAnnounce();

    public static ActionBarAnnounce getActionBarAnnounce() {
        return actionBarAnnounce;
    }

    public static TitleAnnounce getTitleAnnounce() {
        return titleAnnounce;
    }

}
