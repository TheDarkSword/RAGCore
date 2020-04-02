package it.revarmygaming.spigot.holograms;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Hologram {

    private final Location location;
    private final List<HologramLine> lines;

    public Hologram(Location location) {
        this.location = location;
        lines = new ArrayList<>();
    }

    public Hologram create() {
        Location loc = location.clone();
        for (HologramLine line : lines) {
            loc.add(0, 0.2,0);

            line.spawn(loc);
        }

        return this;
    }

    public Hologram destroy() {
        lines.forEach(HologramLine::despawn);
        return this;
    }

    public Hologram addLine(String text) {
        lines.add(new HologramLine(text));
        return this;
    }

    public Hologram removeLine(int i) {
        HologramLine line = lines.get(i);
        line.despawn();

        lines.remove(i);
        return this;
    }

    public Hologram setLine(int i, String text) {
        HologramLine line = lines.get(i);
        line.setText(text);

        return this;
    }

    public int getNumberOfLines() {
        return lines.size();
    }

    public Hologram clearText() {
        lines.forEach(HologramLine::despawn);
        lines.clear();

        return this;
    }

    private HologramLine getLine(int i) {
        return lines.get(i);
    }
}
