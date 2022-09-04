package me.kafein.elitegenerator.generator.feature.boost;

public class Boost {

    final private int level;
    private long time;

    public Boost(final int level, final long time) {
        this.level = level;
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeParsed() {
        final long time = Long.valueOf(this.time);
        final int days = (int) (time / 86400);
        final int hours = (int) (time / 3600) % 24;
        final int minutes = (int) (time / 60) % 60;
        final int seconds = (int) (time % 60);
        return (days > 0 ? (days > 9 ? days : "0" + days) + "." : "")
                + (hours > 0 ? (hours > 9 ? hours : "0" + hours) : "00") + "."
                + (minutes > 0 ? (minutes > 9 ? minutes : "0" + minutes) : "00") + "."
                + (seconds > 0 ? (seconds > 9 ? seconds : "0" + seconds) : "00");
    }

    public void addTime(long var) {
        this.time += var;
    }

    public void removeTime(long var) {
        this.time -= var;
    }

}
